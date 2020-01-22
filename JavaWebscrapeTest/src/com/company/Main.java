package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        String url = "https://www.hltv.org/matches";
        Document document = Jsoup.connect(url).userAgent("JeanRean").get();

        Elements links = document.select(".matches .match a");
        for (Element link : links) {
            String relHref = link.absUrl("href");
            Document doc = Jsoup.connect(relHref).userAgent("JeanRean").get();

            Element gameTime = doc.select(".match-page .teamsBox .timeAndEvent .time").first();
            Element gameDate = doc.select(".match-page .teamsBox .timeAndEvent .date").first();
            Element eventName = doc.select(".match-page .teamsBox .timeAndEvent .event a").first();
            Elements tNames = doc.select(".match-page .teamName");
            Element gameLink = doc.select(".match-page .streams .hltv-live a").first();
            Elements teamPlayers = doc.select(".match-page .lineups .player");

            //Need to have something which sorts by date?

            String team1 ="";
            String team2 ="";
            int teamCount = 0;

            for(Element tName : tNames){
                teamCount++;
                if(teamCount < 3){
                    if(teamCount == 1){
                        team1 = tName.text();
                    }
                    else if(teamCount == 2){
                        team2 = tName.text();

                        Match match = new Match();

                        if(team1.equals("")){
                            match.team1 = "TBC";
                        }
                        else{
                            match.team1 = team1;
                        }
                        if(team2.equals("")){
                            match.team2 ="TBC";
                        }
                        else{
                            match.team2 = tName.text();
                        }
                        match.time = gameTime.text();
                        match.date = gameDate.text();
                        match.event = eventName.text();
                        match.players.add(teamPlayers.text());
                        try{
                            match.gameHref = gameLink.absUrl("href");
                        }
                        catch(Exception e){
                            match.gameHref = "Not Avaliable";
                        }

                        displayGameInfo(match.team1,match.team2,match.time,match.date,match.event,match.gameHref,match.players);
                    }
                }
            }
        }
    }

    public static void displayGameInfo(String team1, String team2, String time, String date, String event, String gameHref, ArrayList<String> players){
        System.out.println("----------------------------------------");
        System.out.println(team1 + " vs " + team2 + "\n\nTime: " + time + "\nDate: " + date + "\nEvent: " + event + "\n\nWhere to watch: " + gameHref + "\n\nPlayers: ");
        players.forEach(System.out::println);
        System.out.println("----------------------------------------\n\n");
    }
}
