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
    private String nickname;
    private Date connectedSince;
    private Date lastSeen;
    private Date idleSince;
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
    }

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
