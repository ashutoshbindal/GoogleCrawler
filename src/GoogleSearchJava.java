//package com.journaldev.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
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
        String FILENAME = "crawler.txt";
        String file_name = "triples_ollie.txt";
        String file_url = "urls.txt";
        String file_para = "para.txt";

        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter(FILENAME);
        bw = new BufferedWriter(fw);

        BufferedWriter bw1 = null;
        FileWriter fw1 = null;
        fw1 = new FileWriter(file_name);
        bw1 = new BufferedWriter(fw1);

        BufferedWriter bw2 = null;
        FileWriter fw2 = null;
        fw2 = new FileWriter(file_url);
        bw2 = new BufferedWriter(fw2);

        BufferedWriter bw3 = null;
        FileWriter fw3 = null;
        fw3 = new FileWriter(file_para);
        bw3 = new BufferedWriter(fw3);

        //Taking search term input from console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the search term.");
        String searchTerm = scanner.nextLine();
        System.out.println("Please enter the number of results:");
        int num = scanner.nextInt();
        scanner.close();


        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+" -site:wordpress.com"+"&num="+num;
        //without proper User-Agent, we will get 403 error

        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();


        //Document doc0 = Jsoup.connect("https://wordpress.com/read/blogs/110825788/posts/1601").userAgent("Mozilla/5.0").get();

        //below will print HTML data, save it to a file and open in browser to compare
        System.out.println(doc.html());

        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
        //we need to change below accordingly
        Elements results = doc.select("h3.r > a");
        Elements results_1 = doc.select("span.st");

        for (Element result : results) {
            String linkHref = result.attr("href");
            System.out.println(linkHref);
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
            System.out.println("size::" + span_data.size());
            //if(span_data.get(i).length()>=1)
            {

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

                    //System.out.println("Span::" + span_data_temp);

                    int flag = 0;

                    for (Element para_temp : para) {

                        String para_text = para_temp.text();
                        para_text = para_text.replaceAll("\\[(.*?)\\]", "");


                        /*if(i==1 && flag==0)
                        {
                            System.out.println(para_text);
                            flag = 1;
                        }*/
                        //System.out.println(para_text);
                        if ((para_text.replaceAll("[^a-zA-Z0-9.]" , "" )).contains(span_data_temp.replaceAll("[^a-zA-Z0-9.]" , "" ))) {
                            List<String> lines = Arrays.asList(para_text.split("\\."));


                            try {

                                //String content = "This is the content to write into file\n";


                                for (String lines_itr : lines) {
                                    //System.out.println("Done:: " + lines_itr);
                                    bw.write(lines_itr);
                                    bw.write(".");
                                    bw.write("\n");
                                    bw2.write(link_data.get(i));
                                    bw2.write("\n");
                                    bw3.write(para_text);
                                    bw3.write("\n");
                                }
                                System.out.println("Done Writing");

                            } catch (IOException e) {

                                e.printStackTrace();

                            }

                        }
                    }
                }
            }
            /*else
            {
                bw.write("\n");
                bw2.write(link_data.get(i));
                bw2.write("\n");
                bw3.write("\n");
            }*/
        }
        try {

            if (bw != null)
                bw.close();

            if (fw != null)
                fw.close();

            if (bw2 != null)
                bw2.close();

            if (fw2 != null)
                fw2.close();

            if (bw3 != null)
                bw3.close();

            if (fw3 != null)
                fw3.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

        //entity extraction from Ollie

        String command = "java -Xmx512m -jar ollie-app-latest.jar crawler.txt";

        try
        {
            Process proc = Runtime.getRuntime().exec(command);


            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {

                bw1.write(line);
                bw1.write("\n");
                //System.out.print(line + "\n");
            }

            proc.waitFor();

        }
        catch(Exception e){}

        //entity extraction from Ollie

        // run the following command
        /*
        String command = "java -cp \"*\" edu.stanford.nlp.tagger.maxent.MaxentTagger -model models/english-left3words-distsim.tagger -textFile crawler.txt -outputFormat tsv -outputFile triples_post.tag\n";

        try
        {
            Process proc = Runtime.getRuntime().exec(command);


            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {

                bw1.write(line);
                bw1.write("\n");
                //System.out.print(line + "\n");
            }

            proc.waitFor();

        }
        catch(Exception e){}
    */
        try {

            if (bw1 != null)
                bw1.close();

            if (fw1 != null)
                fw1.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }
    }

}