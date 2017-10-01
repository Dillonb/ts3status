package com.dillonbeliveau.ts3status.model;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class ParsedClient {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String idleFor;

    private String nickname;
    private String connected;
    private String idleSince;

    private String sOrNoS(long x) {
        if (x == 1) {
            return "";
        }
        else {
            return "s";
        }
    }

    public ParsedClient(Client client) {
        this.nickname = client.getNickname();
        this.connected = dateFormat.format(client.getLastConnectedDate());
        this.idleSince = dateFormat.format(Date.from(Instant.now().minusMillis(client.getIdleTime())));


        StringBuilder idleForText = new StringBuilder();
        Duration idleFor = Duration.ofMillis(client.getIdleTime());

        boolean atLeastOnePart = false;

        if (idleFor.toDaysPart() > 0) {
            idleForText.append(idleFor.toDaysPart()).append(" day").append(sOrNoS(idleFor.toDaysPart())).append(" ");
            atLeastOnePart = true;
        }

        if (idleFor.toHoursPart() > 0 || atLeastOnePart) {
            idleForText.append(idleFor.toHoursPart()).append(" hour").append(sOrNoS(idleFor.toHoursPart())).append(" ");
            atLeastOnePart = true;
        }

        if (idleFor.toMinutesPart() > 0 || atLeastOnePart) {
            idleForText.append(idleFor.toMinutesPart()).append(" minute").append(sOrNoS(idleFor.toMinutesPart())).append(" ");
            atLeastOnePart = true;
        }

        if (idleFor.toSecondsPart() > 0 || atLeastOnePart) {
            idleForText.append(idleFor.toSecondsPart()).append(" second").append(sOrNoS(idleFor.toSecondsPart())).append(" ");
        }

        this.idleFor = idleForText.toString();

    }

    public String getNickname() {
        return nickname;
    }

    public String getConnected() {
        return connected;
    }

    public String getIdleSince() {
        return idleSince;
    }

    public String getIdleFor() {
        return idleFor;
    }
}
