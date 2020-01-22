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

        //This section is a work in progrss
        Elements links = document.select(".matches .match a");
        for (Element link : links) {

            String relHref = link.absUrl("href");
            Document doc = Jsoup.connect(relHref).userAgent("JeanRean").get();

            int teamCount = 0;

            Element gameTime = doc.select(".match-page .teamsBox .timeAndEvent .time").first();
            Element gameDate = doc.select(".match-page .teamsBox .timeAndEvent .date").first();
            Element eventName = doc.select(".match-page .teamsBox .timeAndEvent .event a").first();
            Elements tNames = doc.select(".match-page .teamName");
            Element gameLink = doc.select(".match-page .streams .hltv-live a").first();
            Elements teamPlayers = doc.select(".match-page .lineups .player");

            ArrayList<String> players = new ArrayList<String>();

            System.out.println("----------------------------------------");

            String team1 ="";
            String team2 ="";

            for(Element tName : tNames){
                teamCount++;
                if(teamCount < 3){
                    if(teamCount == 1){
                        team1 = tName.text();
                    }
                    else if(teamCount == 2){
                        String gameHref = gameLink.absUrl("href");
                        team2 = tName.text();
                        String time = gameTime.text();
                        String date = gameDate.text();
                        String event = eventName.text();

                        System.out.println(team1 + " vs " + team2 + "\n\nTime: " + time + "\nDate: " + date + "\nEvent: " + event + "\n\nWhere to watch: " + gameHref);

                        players.add(teamPlayers.text());
                    }

                }

            }

            System.out.println("Players:");
            players.forEach((n) -> System.out.println(n));

        }

    }

}
