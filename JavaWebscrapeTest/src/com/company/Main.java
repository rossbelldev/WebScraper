package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) throws Exception {
        String url = "https://www.hltv.org/matches";
        Document document = Jsoup.connect(url).userAgent("JeanRean").get();

        Elements links = document.select(".matches .match a");
        for (Element link : links) {

            String relHref = link.absUrl("href");
            Document doc = Jsoup.connect(relHref).userAgent("JeanRean").get();

            Elements tNames = doc.select(".match-page .teamName");
            for(Element tName : tNames){
                System.out.println(tName.text());
            }



        /*
        Elements matches = document.select(".matches .upcoming-matches .upcoming-match");
        for (Element match : matches) {
            //Need to have a way to break up the teams, time, event, remove the A. then print between the --
            Element name = document.select(".matches .upcoming-matches .upcoming-match .team").first();
            String nameOnly = name.text();

            Elements links = document.select(".matches .match a");
            for(Element link : links){
                //Need to reorganise completely. Needs to have
                String relHref = link.absUrl("href");
                Document doc = Jsoup.connect(relHref).userAgent("JeanRean").get();
                Elements tNames = doc.select(".match-page .teamName");

                for(Element tName : tNames){
                    System.out.println(tName.text());
                    System.out.println("\n");
                }
            }
            */






            /*
            System.out.println("------------------------------------------------------");
            System.out.println(nameOnly);
            System.out.println(match.text());
            System.out.println(relHref);
            System.out.println("------------------------------------------------------\n");
             */
        }

    }

}
