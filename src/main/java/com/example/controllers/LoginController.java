package com.example.controllers;

import com.example.entities.User;
import com.example.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               RedirectAttributes redirectAttributes) {
        if (userRepository.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            return "redirect:/register";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
            return "redirect:/register";
        }

        userRepository.save(user);
        return "redirect:/login";
    }


    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUsername", username);
            redirectAttributes.addFlashAttribute("message", "Login successful!");
            return "redirect:/home"; // Redirect to home page
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password!");
            return "redirect:/login"; // Redirect back to login page with error message
        }
    }
}
