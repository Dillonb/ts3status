package com.dillonbeliveau.ts3status.service;

import com.dillonbeliveau.ts3status.model.ParsedClient;
import com.dillonbeliveau.ts3status.repository.ParsedClientRepository;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TS3Service {
    @Autowired
    private TS3Query ts3Query;

    @Autowired
    private ParsedClientRepository parsedClientRepository;

    public Set<ParsedClient> getOnlineClientsUncached() {
        return ts3Query.getApi().getClients().stream()
                .filter(Client::isRegularClient)
                .map(ParsedClient::new)
                .collect(Collectors.toSet());
    }

    @Scheduled(fixedRate = 10_000)
    void refresh() {
        Set<ParsedClient> newOnlineClients = getOnlineClientsUncached();
        Set<ParsedClient> existingOnlineClients = new HashSet<ParsedClient>(parsedClientRepository.findByOnlineTrue());

        Set<ParsedClient> newlyOfflineClients = Sets.difference(existingOnlineClients, newOnlineClients);
        for (ParsedClient client : newlyOfflineClients) {
            client.setOnline(false);
        }

        for (ParsedClient client : Sets.union(newOnlineClients, newlyOfflineClients)) {
            parsedClientRepository.save(client);
        }
    }
}
