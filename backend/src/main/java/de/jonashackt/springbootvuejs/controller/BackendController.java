package de.jonashackt.springbootvuejs.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import de.jonashackt.springbootvuejs.domain.User;
import de.jonashackt.springbootvuejs.domain.Point;
import de.jonashackt.springbootvuejs.exception.UserNotFoundException;
import de.jonashackt.springbootvuejs.repository.UserRepository;
import de.jonashackt.springbootvuejs.repository.PointRepository;
import de.jonashackt.springbootvuejs.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;

@Controller
@RequestMapping("/api")
public class BackendController {
    private final UserService service;
    public BackendController(UserService service){
        this.service=service;
    }

    private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

    public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";
    public static final String SECURED_TEXT = "{\"table\":[{\"paramR\":3,\"coordX\":3,\"coordY\":3,\"hit\":false},{\"paramR\":2,\"coordX\":2,\"coordY\":2,\"hit\":true},{\"paramR\":1,\"coordX\":1,\"coordY\":1,\"hit\":true}]}";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;

    @ResponseBody
    @RequestMapping(path = "/hello")
    public String sayHello() {
        LOG.info("GET called on /hello resource");
        return HELLO_TEXT;
    }

    @ResponseBody
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<?> addNewUser( @RequestBody User user) {
    if (service.saveUser(user)) {
        System.out.println("user registered: " + user.getUsername());
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    } else {
        System.out.println("user exists: " + user.getUsername());
        return new ResponseEntity<>("User already exists.", HttpStatus.CONFLICT);
    }
}
    /*
    
    @ResponseStatus(HttpStatus.CREATED)
    public long addNewUser (@PathVariable("username") String username, @PathVariable("password") String password) {
        User savedUser = userRepository.save(new User(username, password));

        LOG.info(savedUser.toString() + " successfully saved into DB");

        return savedUser.getId();
    }
*/  
    @ResponseBody
    @GetMapping(path = "/auth")
    public Long auth(@RequestParam String username){
        if(username == "spring")return new Long(0);
        return userRepository.findByUsername(username).getId();
    }
    @ResponseBody
    @GetMapping(path = "/user/{id}")
    public User getUserById(@PathVariable("id") long id) {

        return userRepository.findById(id).map(user -> {
            LOG.info("Reading user with id " + id + " from database.");
            return user;
        }).orElseThrow(() -> new UserNotFoundException("The user with the id " + id + " couldn't be found in the database."));
    }

    @ResponseBody
    @RequestMapping(path="/secured/{userId}", method = RequestMethod.GET)
    public String getSecured( @PathVariable("userId") Long userId){
        if(userId==null)userId=new Long(0);
        LOG.info("GET successfully called on /secured resource");
        Gson gson = new Gson();
        return  "{\"table\":" + gson.toJson(pointRepository.findByUserId(userId)) + "}";
    }
    @ResponseBody
    @RequestMapping(path="/secured/{userId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String putSecured( @PathVariable("userId") long userId, @RequestBody Point point){
        LOG.info("POST successfully called on /secured resource");
        point.setUserId(userId);
        try{
            /*long paramR=Long.parseLong(request.getParameter("paramR"));
            long coordX=Long.parseLong(request.getParameter("coordX"));
            double coordY=Double.parseDouble(request.getParameter("coordY"));
            LOG.info("got paramR:" + paramR);
            LOG.info("got coordX:" + coordX);
            LOG.info("got coordY:" + coordY);*/
            LOG.info("got data:" + point.toString());
            point.check();
            Point savedPoint = pointRepository.save(point);
            LOG.info(savedPoint.toString() + " successfully saved into DB");
            return "saved";
        }
        catch(Exception e){
            LOG.info("catched exception" + e.getMessage());
            return "incorrect parameters";
        }
       
    }
}
