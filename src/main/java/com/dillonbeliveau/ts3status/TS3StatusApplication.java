package com.dillonbeliveau.ts3status;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TS3StatusApplication {

    @Bean
    public TS3Config ts3Config() {
        TS3Config ts3Config = new TS3Config();
        ts3Config.setHost("cyphe.red");
        return ts3Config;
    }

    @Bean
    public TS3Query ts3Query(TS3Config ts3Config) {
        TS3Query ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();
        return ts3Query;
    }

    @Bean
    public TS3Api ts3Api(TS3Query ts3Query) {
        TS3Api api = ts3Query.getApi();
        api.login("dgb", "***REMOVED***");
        api.selectVirtualServerById(1);
        api.setNickname("ServerMonitorBot");
        api.sendChannelMessage("ServerMonitorBot online.");
        return api;
    }

    public static void main(String[] args) {
        SpringApplication.run(TS3StatusApplication.class);
    }
}
