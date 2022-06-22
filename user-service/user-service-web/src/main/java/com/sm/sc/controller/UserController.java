package com.sm.sc.controller;

import com.alibaba.fastjson.JSONObject;
import com.sm.sc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author lmwl
 */
@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserService.UserForm userForm) {

        return userService.login(userForm);
    }

    @GetMapping("/detail")
    public JSONObject detail(@RequestParam("id") Long id) {

        return userService.detail(id);
    }
}
