package com.example.controller;

import com.example.CustException.CustException;
import com.example.CustomClasses.PassWord;
import com.example.dummies.Booking;
import com.example.dummies.Custom;
import com.example.model.User;
import com.example.service.UserService;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userController")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/adduser")
    public String adduser(@RequestBody  User user) throws CustException {
        user.setUserType(0);
        userService.add(user);
        return "Added User Successfully";
    }
    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody User user) throws CustException {
        user.setUserType(1);
        userService.add(user);
        return "Added User Successfully";
    }
    @RequestMapping(value = "/login" , method=RequestMethod.POST)
    public String login(@RequestBody User u) throws CustException {
        System.out.println(u+"\n\n\n");
        if(userService.verifyUser(u.getUserId(), u.getPassword()))
            return "Welcome";
        else
            return "Invalid Id/Password";
    }

    @RequestMapping(value = "/verifyAdmin" , method=RequestMethod.POST)
    public String verifyAdminString(@RequestBody User u) throws CustException {
        System.out.println(u+"\n\n\n");
        if(userService.verifyAdmin(u.getUserId(), u.getPassword()))
            return "Welcome";
        else
            return "Invalid Id/Password";
    }


    @PostMapping("/forgotpassword")
    public String forgotPassword(@RequestBody User user)throws CustException{
        userService.forgotPassword(user);
        return "We have sent a OTP(One Time Password) to your registered mail id";
    }
    @PutMapping("/resetPassword")
    public String resetPassword(@RequestBody PassWord passWord) throws CustException {
        userService.resetPwd(passWord);
        return "Password Reset Successfully";
    }

    @PostMapping("/findById")
    public User findById(@RequestBody User u){
        System.out.println("uuuuuuuuuuuuuuuuuuuu");
        return userService.findById(u);
    }

    @PostMapping("/findById1")
    public User findById1(@RequestBody String i){
        return userService.findByIdInt(i);
    }

    @PostMapping("/getAllBookingHistory")
    public String getAllBookingHistory(@RequestBody Custom custom){
        System.out.println("aaaaaaa");
        return userService.getAllBookingHistory(custom);
    }

}
