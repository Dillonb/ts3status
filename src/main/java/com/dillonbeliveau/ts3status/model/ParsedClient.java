package com.dillonbeliveau.ts3status.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
public class ParsedClient {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @JsonProperty("idleFor")
    private String idleFor;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("connectedSince")
    private Date connectedSince;
    @JsonProperty("lastSeen")
    private Date lastSeen;
    @JsonProperty("idleSince")
    private Date idleSince;
    @JsonProperty("online")
    private boolean online;

    @Id
    @JsonProperty("uniqueId")
    private String uniqueId;

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
        this.connectedSince = client.getLastConnectedDate();
        this.idleSince = Date.from(Instant.now().minusMillis(client.getIdleTime()));
        this.idleFor = getTimeSinceText(client.getIdleTime());
        this.uniqueId = client.getUniqueIdentifier();
        this.lastSeen = new Date();
        this.online = true;
    }

    public ParsedClient() {}

    public String getNickname() {
        return nickname;
    }

    public String getConnectedSince() {
        return dateFormat.format(connectedSince);
    }

    public long getConnectedSinceTimestamp() {
        return connectedSince.getTime();
    }

    public String getLastSeen() {
        return dateFormat.format(lastSeen);
    }

    public long getLastSeenTimestamp() {
        return lastSeen.getTime();
    }

    public String getIdleSince() {
        return dateFormat.format(this.idleSince);
    }

    public long getIdleSinceTimestamp() {
        return this.idleSince.getTime();
    }

    public String getIdleFor() {
        return idleFor;
    }

    public String getOfflineFor() {
        return getTimeSinceText(new Date().getTime() - lastSeen.getTime());
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
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
        return Objects.equals(uniqueId, that.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueId);
    }
}
