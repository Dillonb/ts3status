package com.dillonbeliveau.ts3status.service;

import com.dillonbeliveau.ts3status.model.ParsedClient;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class TS3Service {
    @Autowired
    private TS3Api api;

    private LoadingCache<CacheKey, List<Client>> clients;

    private enum CacheKey {
        Online
    }

    public TS3Service() {
        clients = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<CacheKey, List<Client>>() {
                    @Override
                    public List<Client> load(CacheKey cacheKey) {
                        switch (cacheKey) {
                            case Online:
                                return api.getClients();
                            default:
                                throw new IllegalArgumentException("Illegal cache key: " + cacheKey);

                        }
                    }
                });
    }


    public List<ParsedClient> getAllClients() {
        return clients.getUnchecked(CacheKey.Online).stream()
                .filter(Client::isRegularClient)
                .map(ParsedClient::new)
                .collect(Collectors.toList());
    }
}
