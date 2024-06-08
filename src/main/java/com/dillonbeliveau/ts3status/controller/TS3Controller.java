package com.dillonbeliveau.ts3status.controller;

import com.dillonbeliveau.ts3status.repository.ParsedClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TS3Controller {
    @Autowired
    private ParsedClientRepository parsedClientRepository;

    @RequestMapping("/")
    public String clients(Model model) {
        model.addAttribute("clients", parsedClientRepository.findByOnlineTrue());
        model.addAttribute("offlineClients", parsedClientRepository.findByOnlineFalse());
        return "clients";
    }
}