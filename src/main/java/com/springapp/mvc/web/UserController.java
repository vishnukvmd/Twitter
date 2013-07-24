package com.springapp.mvc.web;

import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vishnu
 * Date: 11/7/13
 * Time: 2:39 PM
 */
@Controller
public class UserController {
    private final UserRepository repository;
    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User fetchUser(@PathVariable("id") int id) throws IOException {
        return repository.findById(id);
    }

    @RequestMapping(value = "/users/{id}/followers", method = RequestMethod.GET)
    @ResponseBody
    public List<User> fetchFollowers(HttpServletResponse response, @PathVariable("id") int userid) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return repository.fetchFollowers(userid);
    }

    @RequestMapping(value = "/users/{id}/follows", method = RequestMethod.GET)
    @ResponseBody
    public List<User> fetchFollows(HttpServletResponse response, @PathVariable("id") int userid) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return repository.fetchFollows(userid);
    }

    @RequestMapping(value = "/users/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(@RequestBody Map<String,Object> requestParameters) throws IOException {
        return repository.follow((int)requestParameters.get("follower"), (int)requestParameters.get("followed"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String createUser(@RequestBody final User user){
        System.out.println(user.getName());
        int id = repository.createUser(user.getUsername(), user.getName(), user.getEmail(), user.getPassword());
        if (id != -1){
            return "Success";
        }
        return "Fail";
    }
}