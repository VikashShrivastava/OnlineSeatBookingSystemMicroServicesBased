package com.example.service;

import com.example.CustException.CustException;
import com.example.CustomClasses.PassWord;
import com.example.EmailService.EmailSenderService;
import com.example.dummies.Booking;
import com.example.dummies.Custom;
import com.example.model.User;
import com.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    public UserService(EmailSenderService emailSenderService, UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.emailSenderService =emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }
    public void add(User user) throws CustException {

        Optional<User>us=userRepo.findById(user.getUserId());
        if(us.isPresent())
        {
            throw new CustException("User Already Exists");
        }
        emailSenderService.sendSimpleEmail(user.getUserEmail(), "Registration Successful","Thanks for registering");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
    protected String tokengenerate() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public boolean verifyUser(String id, String password) throws CustException {
        Optional<User>temp=userRepo.findById(id);
        
        if(temp.isEmpty())
        {
            throw new CustException("No such user found");
        }
        return passwordEncoder.matches(password,temp.get().getPassword());
    }
    protected String token;
    public void forgotPassword(User user) throws CustException {
        Optional<User>temp=userRepo.findById(user.getUserId());
        if(temp.isEmpty())
        {
            throw new CustException("Invalid ID/Email");
        }
        if(!user.getUserEmail().equals(temp.get().getUserEmail()))
        {
            throw new CustException("User Id and Email does not match");
        }
        token=tokengenerate();
        emailSenderService.sendSimpleEmail(user.getUserEmail(), "Reset password request","This is your otp to reset your password "+token);
        token=passwordEncoder.encode(token);
    }

    public void resetPwd(PassWord passWord) throws CustException {
        if(passwordEncoder.matches(passWord.getToken(),token ))
        {
            System.out.println(passWord.getUserId());
            System.out.println(passWord.getPassword());
            passWord.setPassword(this.passwordEncoder.encode(passWord.getPassword()));
            System.out.println(passWord.getPassword());
            userRepo.updatepassword(passWord.getPassword(),passWord.getUserId());
            //token = "";
            token="";
        }
        else
        {
            throw new CustException("Invalid OTP");
        }
    }

    public User findById(User u) {
        return userRepo.findById(u.getUserId()).get();
    }

    public User findByIdInt(String i) {
        return userRepo.findById(i).get();
    }

    public String getAllBookingHistory(Custom custom) {
        System.out.println("bbbbbbb");
        String str = restTemplate.postForObject("http://localhost:8003/getUserHistory" , custom, String.class);

        //ResponseEntity<String> str = restTemplate.postForObject("http://localhost:8003/getUserHistory" , custom, ResponseEntity.class);
        System.out.println("str ==" + str);
        System.out.println("hhhhhh");
        return str;

    }

    public boolean verifyAdmin(String userId, String password) throws CustException {
            Optional<User>temp=userRepo.findById(userId);

            if(temp.isEmpty())
            {
                throw new CustException("No such user found");
            }
            return temp.get().getUserType() == 1 && passwordEncoder.matches(password,temp.get().getPassword());
        }
}


