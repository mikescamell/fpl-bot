package fpl.bot.league.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassicLeague {

    public int id;
    public String name;
    public String shortName;
    public String created;
    public boolean closed;
    public boolean forumDisabled;
    public boolean makeCodePublic;
    public String leagueType;
    public String scoring;
    public boolean reprocessStandings;
    public int adminEntry;
    public int startEvent;

}