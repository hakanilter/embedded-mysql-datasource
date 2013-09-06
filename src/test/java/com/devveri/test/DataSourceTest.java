package com.devveri.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User: hilter
 * Date: 9/7/13
 * Time: 12:57 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/devveri/test/config/test-beans.xml")
public class DataSourceTest {

    private static final String TEST_QUERY = "SELECT * FROM pet";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        List<Pet> list = jdbcTemplate.query(TEST_QUERY, new RowMapper<Pet>() {
            @Override
            public Pet mapRow(ResultSet rs, int i) throws SQLException {
                Pet pet = new Pet();
                pet.setName(rs.getString("name"));
                pet.setOwner(rs.getString("owner"));
                pet.setSpecies(rs.getString("species"));
                pet.setBirth(rs.getDate("birth"));
                pet.setDeath(rs.getDate("death"));
                return pet;
            }
        });

        assert list != null;
        assert list.size() > 0;
    }

}
