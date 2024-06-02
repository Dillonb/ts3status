package com.dillonbeliveau.ts3status.controller;

import com.dillonbeliveau.ts3status.service.TS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TS3Controller {
    @Autowired
    private TS3Service ts3Service;

    @RequestMapping("/")
    public String clients(Model model) {
        model.addAttribute("clients", ts3Service.getOnlineClients());
        model.addAttribute("offlineClients", ts3Service.getOfflineClients());
        return "clients";
    }
}