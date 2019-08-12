package fpl.bot.league.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassicLeagueStandingResponse {

    public NewEntries newEntries;
    public ClassicLeague league;
    public Standings standings;
    public int updateStatus;

}