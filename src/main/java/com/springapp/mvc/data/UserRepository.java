package com.springapp.mvc.data;

import com.springapp.mvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findById(int id) {
        try {
            return jdbcTemplate.queryForObject("select name, userid, username, email from users where userid = ?",
                new Object[]{id}, new BeanPropertyRowMapper<User>(User.class));
        }
        catch (Exception e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("select name, username, password from users where email = ?",
                new Object[]{email}, new BeanPropertyRowMapper<User>(User.class));
        }
        catch(Exception e) {
            return null;
        }
    }

    public long createUser(String username, String password, String email, String name) {
        System.out.println(username+":"+password+":"+email+":"+name+":");
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("users");
        insert.setColumnNames(Arrays.asList("username", "password", "email", "name"));
        insert.setGeneratedKeyName("userid");
        Map<String, Object> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        param.put("name", name);
        param.put("email", email);
        try{
            return  (Long) insert.executeAndReturnKey(param);
        }
        catch( DuplicateKeyException e){
            return -1;
        }
    }

    public int follow(int follower, int followed) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("followers");
        insert.setColumnNames(Arrays.asList("follower", "followed"));
        Map<String, Object> param = new HashMap<>();
        param.put("follower", follower);
        param.put("followed", followed);
        try{
            insert.execute(param);
            return 1;
        }
        catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public List<User> fetchFollowers(int userid) {
        try {
                return jdbcTemplate.query("select name, users.userid, users.username, users.email, users.name from followers inner join users on followers.follower=users.userid where followers.followed  = ?",
                    new Object[]{userid}, new BeanPropertyRowMapper<>(User.class));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
