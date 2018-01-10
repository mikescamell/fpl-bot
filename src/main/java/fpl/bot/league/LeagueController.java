package fpl.bot.league;


import com.jakewharton.fliptables.FlipTableConverters;
import fpl.bot.api.fpl.FplOfficialGameDataService;
import fpl.bot.api.slack.SlackPoster;
import fpl.bot.league.model.ClassicLeagueStandingResponse;
import fpl.bot.league.model.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/league")
public class LeagueController {

    private static final Logger log = Logger.getLogger(LeagueController.class);

    @Autowired
    private FplOfficialGameDataService fplOfficialGameDataService;

    @Autowired
    private SlackPoster slackPoster;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, String> getLeague(@RequestParam(value = "text") String id, @RequestParam(value = "channel_id") String channelId) throws IOException {
        ClassicLeagueStandingResponse leagueResponse = fplOfficialGameDataService.getLeagueById(Integer.parseInt(id));

        String[] headers = {"Rank", "Team & Manager", "Points"};
        String[][] data = new String[leagueResponse.standings.results.size()][leagueResponse.standings.results.size()];
        for (int i = 0; i < leagueResponse.standings.results.size(); i++) {
            Result result = leagueResponse.standings.results.get(i);
            data[i] = new String[]{String.valueOf(result.rank), result.entryName + "\n" + result.playerName, String.valueOf(result.total)};
        }
        String table = FlipTableConverters.fromObjects(headers, data);

        File leagueFile = new File(leagueResponse.league.name.replaceAll(" ", "_") + ".txt");
        FileWriter fooWriter = new FileWriter(leagueFile, false);
        fooWriter.write(table);
        fooWriter.close();

        slackPoster.uploadFile(leagueFile, channelId);
        return Collections.singletonMap("response_type", "in_channel");
    }


}
