package com.dillonbeliveau.ts3status.controller;

import com.dillonbeliveau.ts3status.service.TS3Service;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TS3Controller {
    @Autowired
    private TS3Service ts3Service;

    @RequestMapping("/")
    public String clients(Model model) {
        model.addAttribute("clients", ts3Service.getAllClients());
        return "clients";
    }
}