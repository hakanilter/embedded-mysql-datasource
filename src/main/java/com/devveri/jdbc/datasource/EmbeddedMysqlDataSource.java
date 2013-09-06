package com.devveri.jdbc.datasource;

import com.mysql.management.driverlaunched.ServerLauncherSocketFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This class extends DriverManagerDataSource and enables shutdown
 * and auto delete features for embedded mysql instance
 * Its very useful for Spring integration tests
 *
 * User: hilter
 * Date: 9/3/13
 * Time: 11:29 PM
 */
public class EmbeddedMysqlDataSource extends DriverManagerDataSource {

    private static final String PARAM_SERVER_BASEDIR = "server.basedir";

    private boolean deleteBeforeOpen;
    private boolean deleteAfterClose;

    public EmbeddedMysqlDataSource() {

    }

    public boolean isDeleteBeforeOpen() {
        return deleteBeforeOpen;
    }

    public void setDeleteBeforeOpen(boolean deleteBeforeOpen) {
        this.deleteBeforeOpen = deleteBeforeOpen;
    }

    public boolean isDeleteAfterClose() {
        return deleteAfterClose;
    }

    public void setDeleteAfterClose(boolean deleteAfterClose) {
        this.deleteAfterClose = deleteAfterClose;
    }

    /**
     * Shutdowns mysql instance and removes database files if necessary
     */
    public void init() {
        if (deleteBeforeOpen) {
            File databaseDir = new File(getServerBaseDir());
            ServerLauncherSocketFactory.shutdown(databaseDir, null);
            deleteDirectory(databaseDir);
        }
    }

    /**
     * Shutdowns mysql instance and removes db files
     */
    public void shutdown() {
        File databaseDir = new File(getServerBaseDir());
        ServerLauncherSocketFactory.shutdown(databaseDir, null);
        if (deleteAfterClose) {
            deleteDirectory(databaseDir);
        }
    }

    /**
     * Extracts database path from connection url
     * @return  Database directory
     */
    private String getServerBaseDir() {
        Map<String, String> parameters = getParameters(getUrl());
        if (parameters.containsKey(PARAM_SERVER_BASEDIR)) {
            return parameters.get(PARAM_SERVER_BASEDIR);
        }
        return "";
    }

    /**
     * Deletes a dir recursively deleting anything inside it.
     * @param   dir The dir to delete
     * @return  true if the dir was successfully deleted
     */
    private boolean deleteDirectory(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }

        String[] files = dir.list();
        for (int i = 0, len = files.length; i < len; i++) {
            File f = new File(dir, files[i]);
            if (f.isDirectory()) {
                deleteDirectory(f);
            } else {
                f.delete();
            }
        }
        return dir.delete();
    }

    /**
     * Extracts url parameters as a map
     * @param url   Database connection url
     * @return      Returns Map contains parameters
     */
    private Map<String, String> getParameters(String url) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (url != null && url.contains("?")) {
            String parameterStr = url.substring(url.indexOf("?") + 1);
            for (String parameter : parameterStr.split("&")) {
                String[] values = parameter.split("=");
                if (values.length == 2) {
                    parameters.put(values[0], values[1]);
                }
            }
        }
        return parameters;
    }

}
