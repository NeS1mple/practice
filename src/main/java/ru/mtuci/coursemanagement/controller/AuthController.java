package ru.mtuci.coursemanagement.controller;

import ru.mtuci.coursemanagement.model.User;
import ru.mtuci.coursemanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    public AuthController(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(
                null,
                username,
                encodedPassword,
                "STUDENT"
        );
        users.save(user);
        return "User registered";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        User user = users.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return "Login successful";
    }
}
