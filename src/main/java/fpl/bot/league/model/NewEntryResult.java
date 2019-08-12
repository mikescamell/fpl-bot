package fpl.bot.league.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewEntryResult {

    public int id;
    public String entryName;
    public String playerFirstName;
    public String playerLastName;
    public String joinedTime;
    public int entry;
    public int league;

}