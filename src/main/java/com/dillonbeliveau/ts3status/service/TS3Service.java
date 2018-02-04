package com.dillonbeliveau.ts3status.service;

import com.dillonbeliveau.ts3status.model.ParsedClient;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TS3Service {
    @Autowired
    private TS3Api api;

    public List<ParsedClient> getAllClients() {
        return api.getClients().stream()
                .filter(Client::isRegularClient)
                .map(ParsedClient::new)
                .collect(Collectors.toList());
    }
}
