package com.dillonbeliveau.ts3status.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TS3Controller {
    @RequestMapping("/")
    public String clients(Model model) {
        return "clients";
    }
}