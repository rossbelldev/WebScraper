package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) throws Exception {
        String url = "https://www.hltv.org/matches";
        Document document = Jsoup.connect(url).userAgent("JeanRean").get();

        Elements matches = document.select(".matches .upcoming-matches .upcoming-match");
        for (Element match : matches) {
            //Need to have a way to break up the teams, time, event, remove the A. then print between the --
            Element name = document.select(".matches .upcoming-matches .upcoming-match .team").first();
            String nameOnly = name.text();

            System.out.println("------------------------------------------------------");
            System.out.println(nameOnly);
            System.out.println(match.text());
            System.out.println("------------------------------------------------------\n");
        }

        /* Shows each match for the number of days which there are (not the ideal thing)
        Elements days = document.select(".matches .match-day");
        for(Element day : days){
            Elements matches = document.select(".matches .upcoming-matches .upcoming-match");
            for (Element match : days) {
                System.out.println(match.text());
            }
        }
        */

    }

}
