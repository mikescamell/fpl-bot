package fpl.bot.league.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Standings {

    public boolean hasNext;
    public int number;
    public List<Result> results;

}
