package com.dillonbeliveau.ts3status.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dillonbeliveau.ts3status.repository.ParsedClientRepository;
import com.dillonbeliveau.ts3status.model.ParsedClient;

@Controller
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    ParsedClientRepository clientRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    ResponseEntity<List<ParsedClient>> allClients() {
        return new ResponseEntity<>(clientRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/online", method = RequestMethod.GET)
    ResponseEntity<List<ParsedClient>> onlineClients() {
        return new ResponseEntity<>(clientRepository.findByOnlineTrue(), HttpStatus.OK);
    }

    @RequestMapping(value = "/offline", method = RequestMethod.GET)
    ResponseEntity<List<ParsedClient>> offlineClients() {
        return new ResponseEntity<>(clientRepository.findByOnlineFalse(), HttpStatus.OK);
    }
}
