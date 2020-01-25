package com.company;

//Jsoup needs to be imported and the jsoup JAR needs to be added in order for this to work
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {

        //Gives source for where to scrape and document is created
        String url = "https://www.hltv.org/matches";
        Document document = Jsoup.connect(url).userAgent("JeanRean").get();

        //game id and arraylist for match are initialised
        int id = 0;
        ArrayList<Match> matchList = new ArrayList<Match>();

        //Finds all 'links' whicch each link to new match
        Elements links = document.select(".matches .match a");

        //for each link in links, do the following
        for (Element link : links) {

            //creates the hyperlink for the specific match in order to get more information, makes new document to access and store
            String relHref = link.absUrl("href");
            Document doc = Jsoup.connect(relHref).userAgent("JeanRean").get();

            //Creates new element(s) for each item on match (time, date, eventName, teamNames, gameLink and players)
            Element gameTime = doc.select(".match-page .teamsBox .timeAndEvent .time").first();
            Element gameDate = doc.select(".match-page .teamsBox .timeAndEvent .date").first();
            Element eventName = doc.select(".match-page .teamsBox .timeAndEvent .event a").first();
            Elements tNames = doc.select(".match-page .teamName");
            Element gameLink = doc.select(".match-page .streams .hltv-live a").first();
            Elements teamPlayers = doc.select(".match-page .lineups .player");

            //initialises team names as blank so that they can be edited, team count as 0 because the two teams appear multiple times on the page
            String team1 ="";
            String team2 ="";
            int teamCount = 0;

            //for teamName do the following
            for(Element tName : tNames){

                //increments teamCount and only does until it = 2 so that it doesn't display the same team name over and over
                teamCount++;
                if(teamCount < 3){
                    if(teamCount == 1){

                        //sets the first team name
                        team1 = tName.text();
                    }
                    else if(teamCount == 2){

                        //sets the second team name
                        team2 = tName.text();

                        //creates a new match
                        Match match = new Match();

                        //Sets undeclared matches to tbd, otherwise declares the team name
                        if(team1.equals("")){
                            match.team1 = "TBD";
                        }
                        else{
                            match.team1 = team1;
                        }
                        if(team2.equals("")){
                            match.team2 ="TBD";
                        }
                        else{
                            match.team2 = tName.text();
                        }
                        //Solves potential of game not being displayed
                        try{
                            match.gameHref = gameLink.absUrl("href");
                        }
                        catch(Exception e){
                            match.gameHref = "Not Avaliable";
                        }

                        //declares the remaining assets of the match object
                        match.time = gameTime.text();
                        match.date = gameDate.text();
                        match.event = eventName.text();
                        match.players.add(teamPlayers.text());
                        match.gameId = id;

                        //increments the game id (does this for each match) and adds the completed match to the ArrayList
                        id++;
                        matchList.add(match);

                        //Calls the function 'displayGameInfoThubnail' which shows a shortened version of the match
                        displayGameInfoThumbnail(match.gameId,match.team1,match.team2,match.time);
                    }
                }
            }
        }

        //Initialises invalid as true for the while loop
        boolean invalid = true;

        //while invalid is true do the following
        while(invalid){

            //Buffer reader to read the input
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter game ID to view more info, or 'exit' to exit:");

            //try the following, if not catch
            try {
                //Finds out if the input is 'exit'
                String userInputS = br.readLine();
                if(userInputS.equals("exit")){
                    invalid = false;
                }
                //otherwise checks that it is the correct format of int
                else{
                    //Converts to int
                    int userInput = Integer.parseInt(userInputS);
                    //searches every maatch in matchList to see if the number matches a match id
                    for(Match match : matchList){

                        if(match.gameId == userInput){
                            //invalid is not set to false so that the loop will not end, giving the user a chance to enter more than one match to view
                            displayGameInfo(match.gameId,match.team1,match.team2,match.time,match.date,match.event,match.gameHref,match.players);
                        }
                    }
                }
            }
            //Catch exceptoion if none of the above is applicable
            catch(NumberFormatException nfe) {
                //Error line invalid, keeps invalid true so that it loops
                System.err.println("Invalid Input!");
                invalid = true;
            }
        }

    }

    //'Function' for displaying the game info thumbnail. Takes in the game id, team1 name, team 2 name and game time
    public static void displayGameInfoThumbnail(int id, String t1, String t2, String time){
        System.out.println("----------------------------------------");
        System.out.println(id + "\t" + t1 + " vs " + t2 + "\t\tTime: " + time);
        System.out.println("----------------------------------------\n");
    }

    //Function for displaying the full game info. Takes in the same as thumbnail, with event, watchlink and players added on
    public static void displayGameInfo(int id, String t1, String t2, String time, String date, String event, String href, ArrayList<String> players){
        System.out.println("----------------------------------------");
        System.out.println(id + "\n" + t1 + " vs " + t2 + "\n\nTime: " + time + "\nDate: " + date + "\nEvent: " + event + "\n\nWhere to watch: " + href + "\n\nPlayers: ");
        players.forEach(System.out::println);
        System.out.println("----------------------------------------\n");
    }
}
