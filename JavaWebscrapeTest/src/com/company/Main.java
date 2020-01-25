package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        String url = "https://www.hltv.org/matches";
        Document document = Jsoup.connect(url).userAgent("JeanRean").get();

        int id = 0;
        ArrayList<Match> matchList = new ArrayList<Match>();

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
            id++;
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
                        try{
                            match.gameHref = gameLink.absUrl("href");
                        }
                        catch(Exception e){
                            match.gameHref = "Not Avaliable";
                        }
                        match.time = gameTime.text();
                        match.date = gameDate.text();
                        match.event = eventName.text();
                        match.players.add(teamPlayers.text());
                        match.gameId = id;

                        matchList.add(match);

                        displayGameInfoThumbnail(match.gameId,match.team1,match.team2,match.time);
                    }
                }
            }
        }

        boolean invalid = true;

        while(invalid){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter game ID to view more info, or 'exit' to exit:");

            try {
                String userInputS = br.readLine();
                if(userInputS.equals("exit")){
                    invalid = false;
                }
                else{
                    int userInput = Integer.parseInt(userInputS);
                    for(Match match : matchList){

                        if(match.gameId == userInput){
                            displayGameInfo(match.gameId,match.team1,match.team2,match.time,match.date,match.event,match.gameHref,match.players);
                        }
                    }
                }
            }
            catch(NumberFormatException nfe) {
                System.err.println("Invalid Format!");
                invalid = true;
            }
        }

    }

    public static void displayGameInfoThumbnail(int id, String t1, String t2, String time){
        System.out.println("----------------------------------------");
        System.out.println(id + "\t" + t1 + " vs " + t2 + "\t\tTime: " + time);
        System.out.println("----------------------------------------\n");
    }


    public static void displayGameInfo(int id, String t1, String t2, String time, String date, String event, String href, ArrayList<String> players){
        System.out.println("----------------------------------------");
        System.out.println(id + "\n" + t1 + " vs " + t2 + "\n\nTime: " + time + "\nDate: " + date + "\nEvent: " + event + "\n\nWhere to watch: " + href + "\n\nPlayers: ");
        players.forEach(System.out::println);
        System.out.println("----------------------------------------\n\n");
    }
}
