package csd226.lab2.controllers;

import csd226.lab2.data.Account;
import csd226.lab2.repositories.AccountRepository;
import csd226.lab2.security.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import csd226.lab2.data.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtTokenUtil jwtUtil;

    @PostMapping("/test_form")
    public String test_form(@ModelAttribute Account account, Model model) {
        model.addAttribute("email", account);
        return "result";
    }
    @PostMapping(path="/auth/login")
    public ResponseEntity<?> login(@ModelAttribute Account acc, Model model) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            acc.getEmail(), acc.getPassword())
            );

//            Account account = new Account();
//            account.setId(1);
//            account.setEmail(authentication.getPrincipal().toString());

            Account account = (Account) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(account);

            AuthResponse response = new AuthResponse(account.getEmail(), accessToken);

            return ResponseEntity.ok().body(response);
        } catch( Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(path="/auth/login_old")
    public ResponseEntity<?> loginOld(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );

//            Account account = new Account();
//            account.setId(1);
//            account.setEmail(authentication.getPrincipal().toString());

            Account account = (Account) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(account);

            AuthResponse response = new AuthResponse(account.getEmail(), accessToken);

            return ResponseEntity.ok().body(response);
        } catch( Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


//    @PostMapping(value = "/auth/login")
//    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request){
//        try {
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getEmail(), request.getPassword())
//            );
//
//            Account account = new Account();
//            account.setId(1);
//            account.setEmail(authentication.getPrincipal().toString());
//
//            String accessToken = jwtUtil.generateAccessToken(account);
//
//            AuthResponse response = new AuthResponse(account.getEmail(), accessToken);
//
//            return ResponseEntity.ok().body(response);
//        } catch( Exception ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @GetMapping("/login")
    public ResponseEntity<String> getLogin(){ // map a URL to a method
        String s=
//                +"<script src=\"https://unpkg.com/htmx.org@1.9.10\" integrity=\"sha384-D1Kt99CQMDuVetoL1lrYwg5t+9QdHe7NLX/SoJYkXDFfX37iInKRy5xLSi8nO7UC\" crossorigin=\"anonymous\"></script>"+
//                "<div hx-get=\"/protectedPage\" hx-target=\"#protectedContent\">" +
//                "    Get protected content" +
//                "</div>" +
//                "<div id=\"protectedContent\"></div>" +
//                "<div hx-get=\"/publiccontent\" hx-target=\"#content\">" +
//                "    Get content" +
//                "</div>" +
//                "<div id=\"content\"></div>" +
                "<form hx-post=\"/signup\" hx-target=\"this\" hx-swap=\"outerHTML\">" +
                "    <div>" +
                "        <label>Username</label>" +
                "        <input type=\"text\" name=\"username\" value=\"user\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Password</label>" +
                "        <input type=\"password\" name=\"password\" value=\"password\">" +
                "    </div>" +
                "    <button class=\"btn\">Submit</button>" +
                "    <button class=\"btn\" hx-get=\"/signup\">Cancel</button>" +
                "</form>";
        return ResponseEntity.ok(s);
    }
    @GetMapping("/signin")
    public ResponseEntity<String> getSignin(){ // map a URL to a method
        String s="<form hx-post=\"/auth/login\" hx-target=\"this\" hx-swap=\"outerHTML\">" +
                "    <div>" +
                "        <label>First Name</label>" +
                "        <input type=\"text\" name=\"firstname\" value=\"Grace\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Last Name</label>" +
                "        <input type=\"text\" name=\"lastname\" value=\"Viaje\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Email Address</label>" +
                "        <input type=\"email\" name=\"email\" value=\"grace@gmail.com\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Password</label>" +
                "        <input type=\"password\" name=\"password\" value=\"gracespassword\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Confirm Password</label>" +
                "        <input type=\"password\" name=\"confirmPassword\" value=\"gracespassword\">" +
                "    </div>" +
                "    <button class=\"btn\">Submit</button>" +
                "    <button class=\"btn\" hx-get=\"/signin\">Cancel</button>" +
                "</form>";
        return ResponseEntity.ok(s);
    }


    @PostMapping("/signup")
    public ResponseEntity<String> createAccount(@RequestBody Account signUpFormData) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(signUpFormData.getPassword());

        signUpFormData.setPassword(password);

        Account savedAccount = accountRepository.save(signUpFormData);

        return ResponseEntity.ok("createAccount(): " + signUpFormData.getEmail());
    }
//    @PostMapping("/signup")
//    public ResponseEntity<String> createAccount(@RequestBody Account signUpFormData){
//        return ResponseEntity.ok("createAccount() : "+signUpFormData.getUsername());
//    }


}
