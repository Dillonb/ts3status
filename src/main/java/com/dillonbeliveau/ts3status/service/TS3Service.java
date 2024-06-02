package com.dillonbeliveau.ts3status.service;

import com.dillonbeliveau.ts3status.model.ParsedClient;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class TS3Service {
    @Autowired
    private TS3Query ts3Query;

    private final LoadingCache<CacheKey, List<ParsedClient>> onlineClients;
    private final HashMap<String, ParsedClient> allClients = new HashMap<>();

    private enum CacheKey {
        Online
    }

    public TS3Service() {
        onlineClients = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    @Override
                    public List<ParsedClient> load(CacheKey cacheKey) {
                        switch (cacheKey) {
                            case Online:
                                List<ParsedClient> clients = ts3Query.getApi().getClients().stream()
                                        .filter(Client::isRegularClient)
                                        .map(ParsedClient::new)
                                        .collect(Collectors.toList());

                                allClients.values().stream()
                                        .filter(ParsedClient::isOnline)
                                        .forEach(c -> c.setOnline(false));

                                clients.forEach(c -> allClients.put(c.getNickname(), c));
                                return clients;
                            default:
                                throw new IllegalArgumentException("Illegal cache key: " + cacheKey);

                        }
                    }
                });
    }

    @Scheduled(fixedRate = 10_000)
    public List<ParsedClient> getOnlineClients() {
        return onlineClients.getUnchecked(CacheKey.Online);
    }

    public List<ParsedClient> getOfflineClients() {
        return allClients.values().stream().filter(Predicate.not(ParsedClient::isOnline)).toList();
    }
}
