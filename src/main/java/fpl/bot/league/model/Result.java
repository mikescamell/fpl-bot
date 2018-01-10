package fpl.bot.league.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

    public int id;
    @JsonProperty("entry_name")
    public String entryName;
    public int eventTotal;
    @JsonProperty("player_name")
    public String playerName;
    public String movement;
    public boolean ownEntry;
    public int rank;
    public int lastRank;
    public int rankSort;
    public int total;
    public int entry;
    public int league;
    public int startEvent;
    public int stopEvent;

}
