package com.wujiahui.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by NgCafai on 2019/7/29 17:23.
 */
@Controller
public class LoginController {
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register() {
        return "/site/register";
    }
}
