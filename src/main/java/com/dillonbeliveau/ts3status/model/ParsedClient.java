package com.dillonbeliveau.ts3status.model;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class ParsedClient {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String idleFor;
    private boolean isOnline;
    private String nickname;
    private Date dateConnected;
    private Date dateDisconnected;
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
        this.isOnline = true;
        this.nickname = client.getNickname();
        this.dateConnected = client.getLastConnectedDate();
        this.idleSince = dateFormat.format(Date.from(Instant.now().minusMillis(client.getIdleTime())));
        this.idleFor = getTimeSinceText(client.getIdleTime());
    }

    public String getNickname() {
        return nickname;
    }

    public String getConnected() {
        return dateFormat.format(dateConnected);
    }

    public String getDateDisconnected() {
        return dateFormat.format(dateDisconnected);
    }

    public String getIdleSince() {
        return idleSince;
    }

    public String getIdleFor() {
        return idleFor;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getOfflineSince() {
        return getTimeSinceText(new Date().getTime() - dateDisconnected.getTime());
    }

    public void setOnline(boolean online) {
        isOnline = online;
        if (isOnline)
            dateDisconnected = null;
        else
            dateDisconnected = new Date();
    }

    private String getTimeSinceText(long timeSinceMs) {
        StringBuilder idleForText = new StringBuilder();
        Duration idleFor = Duration.ofMillis(timeSinceMs);

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

        return idleForText.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsedClient that = (ParsedClient) o;
        return Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }
}
