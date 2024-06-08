package com.dillonbeliveau.ts3status;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class TS3StatusApplication {
    @Bean
    public TS3Config ts3Config(
            @Value("${ts3.server.host}") String ts3Host,
            @Value("${ts3.server.user}") String ts3User,
            @Value("${ts3.server.pass}") String ts3Password,
            @Value("${ts3.server.nick}") String ts3Nick) {
        TS3Config ts3Config = new TS3Config();
        ts3Config.setHost(ts3Host);
        ts3Config.setReconnectStrategy(ReconnectStrategy.constantBackoff(1000));
        ts3Config.setConnectionHandler(new ConnectionHandler() {
            @Override
            public void onConnect(TS3Query ts3Query) {
                TS3Api api = ts3Query.getApi();
                api.login(ts3User, ts3Password);
                api.selectVirtualServerById(1);
                api.setNickname(ts3Nick);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
            }
        });
        return ts3Config;
    }

    @Bean
    public DataSource DataSource(@Value("${sqlite.jdbc.url}") String url) {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(url);
        return dataSource;
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
