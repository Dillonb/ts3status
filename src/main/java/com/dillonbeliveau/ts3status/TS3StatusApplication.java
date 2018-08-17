package com.dillonbeliveau.ts3status;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TS3StatusApplication {

    @Bean
    public TS3Config ts3Config() {
        TS3Config ts3Config = new TS3Config();
        ts3Config.setHost("cyphe.red");
        ts3Config.setReconnectStrategy(ReconnectStrategy.constantBackoff(1000));
        ts3Config.setConnectionHandler(new ConnectionHandler() {
            @Override
            public void onConnect(TS3Query ts3Query) {
                TS3Api api = ts3Query.getApi();
                api.login("dgb", "***REMOVED***");
                api.selectVirtualServerById(1);
                api.setNickname("ServerMonitorBot");
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
            }
        });
        return ts3Config;
    }

    @Bean
    public TS3Query ts3Query(TS3Config ts3Config) {
        TS3Query ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();
        return ts3Query;
    }

    public static void main(String[] args) {
        SpringApplication.run(TS3StatusApplication.class);
    }
}
