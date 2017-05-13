//package com.journaldev.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GoogleSearchJava {

    //these store the links and span
    public static ArrayList<String> link_data = new ArrayList<String>();
    public static ArrayList<String> span_data = new ArrayList<String>();

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    public static void main(String[] args) throws IOException {

        //text file for output
        String FILENAME = "filename.txt";
        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter(FILENAME);
        bw = new BufferedWriter(fw);

        //Taking search term input from console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the search term.");
        String searchTerm = scanner.nextLine();
        System.out.println("Please enter the number of results:");
        int num = scanner.nextInt();
        scanner.close();


        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        //without proper User-Agent, we will get 403 error
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

        //below will print HTML data, save it to a file and open in browser to compare
        //System.out.println(doc.html());

        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
        //we need to change below accordingly
        Elements results = doc.select("h3.r > a");
        Elements results_1 = doc.select("span.st");

        for (Element result : results) {
            String linkHref = result.attr("href");
            String linkText = result.text();
            link_data.add(linkHref.substring(7, linkHref.indexOf("&")));
            System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(7, linkHref.indexOf("&")));

        }

        for (Element result_1: results_1) {
            span_data.add(result_1.text());
            System.out.println("Span:" + result_1.text());
        }

        //now interation for individual searches

        for(int i=0; i<link_data.size();i++)
        {
            System.out.println(i);
            //filter span data
            String span_temp = span_data.get(i);
            List<String> span_data_1 = Arrays.asList(span_temp.split("\\.\\.\\."));

            /*for(String span_print : span_data_1)
            {
                System.out.println(span_print);
            }*/

            //search
            Document doc_1 = Jsoup.connect(link_data.get(i)).userAgent("Mozilla/5.0").get();
            Elements para = doc_1.select("p");

            for(String span_data_temp : span_data_1) {

                System.out.println("Span::" + span_data_temp);

                int flag = 0;

                for (Element para_temp : para) {

                    String para_text = para_temp.text();
                    para_text = para_text.replaceAll("\\[(.*?)\\]", "");


                    if(i==1 && flag==0)
                    {
                        System.out.println(para_text);
                        flag = 1;
                    }
                    //System.out.println(para_text);
                    if (para_text.contains(span_data_temp)) {
                        List<String> lines = Arrays.asList(para_text.split("\\."));


                        try {

                            //String content = "This is the content to write into file\n";


                            for (String lines_itr : lines) {
                                System.out.println("Done:: " + lines_itr);
                                bw.write(lines_itr);
                                bw.write("\n");
                            }
                            System.out.println("Done Writing");

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    }
                }
            }
        }
        try {

            if (bw != null)
                bw.close();

            if (fw != null)
                fw.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

    }

}