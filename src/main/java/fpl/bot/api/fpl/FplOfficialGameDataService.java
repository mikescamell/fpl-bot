package fpl.bot.api.fpl;

import fpl.bot.league.model.ClassicLeagueStandingResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class FplOfficialGameDataService {
    private static final Logger log = Logger.getLogger(FplOfficialGameDataService.class);

    private FplOfficialGameData fplOfficialGameData;

    @PostConstruct
    public void updateGameData() {
        log.info("Updating FPL Official game data..");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FplOfficialGameData> responseEntity = restTemplate.exchange("https://fantasy.premierleague.com/api/bootstrap-static", HttpMethod.GET, null, FplOfficialGameData.class);
        fplOfficialGameData = responseEntity.getBody();
    }

    public List<FplOfficialPlayer> getPlayers() {
        updateGameData();
        return fplOfficialGameData.getFplOfficialPlayers();
    }

    public FplOfficialPlayer getPlayer(int id) {
        return fplOfficialGameData.getFplOfficialPlayers().stream().filter(p -> p.getId() == id).findFirst().get();
    }

    public FplOfficialTeam getTeam(int teamCode) {
        return fplOfficialGameData.getFplOfficialTeams().stream().filter(p -> p.getCode() == teamCode).findFirst().get();
    }

    public FplOfficialTeam getTeamById(int id) {
        return fplOfficialGameData.getFplOfficialTeams().stream().filter(p -> p.getId() == id).findFirst().get();
    }

    public ClassicLeagueStandingResponse getLeagueById(int leagueId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClassicLeagueStandingResponse> responseEntity = restTemplate.exchange(String.format("https://fantasy.premierleague.com/api/leagues-classic/%d/standings/?page_new_entries=1&page_standings=1&phase=1", leagueId), HttpMethod.GET, null, ClassicLeagueStandingResponse.class);
        ClassicLeagueStandingResponse classicLeagueStandingResponse = responseEntity.getBody();
        return classicLeagueStandingResponse;
    }
}
