package com.borbely.appauthdemo.controller;

import com.borbely.appauthdemo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/registration")
    public String addUser(Model model) {
        model.addAttribute("newuser", new AppUser());
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("newuser")
                           @Validated
                           AppUserForm appUserForm,
                           BindingResult bind) {

        List<AppUser> users = appUserRepository.findAll();

        for (var u: users) {
            if(appUserForm.getEmail().equals(u.getEmail())) {
                bind.rejectValue("email", "NotUnique", "Ez az e-mail cím már foglalt");
            }
        }

        if (bind.hasErrors()) {
            return "registration";
        }
        String encodedPw = passwordEncoder.encode(appUserForm.getPassword());

        AppUser appUser = new AppUser(appUserForm.getFirstName(), appUserForm.getLastName(),
                                      appUserForm.getEmail(), encodedPw);

        appUserRepository.save(appUser);

        return "redirect:/todos";
    }
}

