package com.dillonbeliveau.ts3status.service;

import com.dillonbeliveau.ts3status.model.ParsedClient;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class TS3Service {
    @Autowired
    private TS3Query ts3Query;

    // Hack: single element LoadingCache
    private enum CacheKey {
        Dummy
    }
    private final LoadingCache<CacheKey, Set<ParsedClient>> onlineClientsCache;

    private Set<ParsedClient> allClients = new HashSet<>();
    private Set<ParsedClient> offlineClients = new HashSet<>();

    public TS3Service() {
        onlineClientsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    @Override
                    public Set<ParsedClient> load(CacheKey cacheKey) {
                        return ts3Query.getApi().getClients().stream()
                                .filter(Client::isRegularClient)
                                .map(ParsedClient::new)
                                .collect(Collectors.toSet());
                    }
                });
    }

    @Scheduled(fixedRate = 10_000)
    void refresh() {
        Set<ParsedClient> onlineClients = onlineClientsCache.getUnchecked(CacheKey.Dummy);

        // Build a new set of all clients, giving priority to the new online clients.
        Set<ParsedClient> allClientsNew = new HashSet<>(onlineClients);
        allClientsNew.addAll(allClients);
        allClients = allClientsNew;

        offlineClients = Sets.difference(allClients, onlineClients);
    }

    public Set<ParsedClient> getOnlineClients() {
        return onlineClientsCache.getUnchecked(CacheKey.Dummy);
    }

    public Set<ParsedClient> getOfflineClients() {
        return offlineClients;
    }
}
