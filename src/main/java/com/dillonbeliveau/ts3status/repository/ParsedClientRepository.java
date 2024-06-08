package com.dillonbeliveau.ts3status.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dillonbeliveau.ts3status.model.ParsedClient;

@Repository
public interface ParsedClientRepository extends PagingAndSortingRepository<ParsedClient, String> {
    List<ParsedClient> findAll();
    void save(ParsedClient clients);
    List<ParsedClient> findByOnlineTrue();
    List<ParsedClient> findByOnlineFalse();
}
