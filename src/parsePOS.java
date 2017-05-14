import java.io.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class parsePOS {

    public static void main(String[] args) throws IOException{

        BufferedReader br = null;
        FileReader fr = null;
        BufferedWriter bw1 = null;
        FileWriter fw1 = null;
        fw1 = new FileWriter("csv_entity_POS.csv");
        bw1 = new BufferedWriter(fw1);

        ArrayList<String> entities_all = new ArrayList<String>();
        int num_entities = 0;
        ArrayList<String> url_index = new ArrayList<String>();
        int url_num = 1;
        try {

            fr = new FileReader("triples_POS.txt");
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader("triples_post"));

            ArrayList<String> ent_tag_list = new ArrayList<String>();

            while ((sCurrentLine = br.readLine()) != null) {

                ent_tag_list.add(sCurrentLine);
                //System.out.println(sCurrentLine);
            }
            for (int i = 0; i < ent_tag_list.size() ; i++ ) {

                List<String> entities = Arrays.asList(ent_tag_list.get(i).split("\\s+"));

                if(entities.size() == 2){
                    if(entities.get(0).contains(".")){url_num++;}
                    if( entities.get(1).contains("NN") ) {
                        //System.out.println("found noun phrase");
                        String new_entity = "";
                        new_entity = new_entity + entities.get(0);
                        if( i < ent_tag_list.size() - 1){
                            List<String> entities_next = Arrays.asList(ent_tag_list.get(i+1).split("\\s+"));

                            int next_flag = 0;
                            if( entities_next.size() == 2 && entities_next.get(1).contains("NN")){ next_flag = 1;}
                            while(next_flag == 1){
                                i++;
                                new_entity = new_entity + "_" + entities_next.get(0);
                                if( i < ent_tag_list.size() - 1){
                                    entities_next = Arrays.asList(ent_tag_list.get(i+1).split("\\s+"));
                                    if( entities_next.size() == 2 && entities_next.get(1).contains("NN")){ next_flag = 1;}
                                    else{next_flag = 0;}
                                }
                                else{
                                    break;
                                }
                            }
                            entities_all.add(new_entity);
                            url_index.add(String.valueOf(url_num));
                            num_entities++;
                        }
                        else{
                            entities_all.add(new_entity);
                            url_index.add(String.valueOf(url_num));
                            num_entities++;
                        }
                    }
                }
            }


            try {
                for(int i = 0 ; i < num_entities ; i++){
                    //System.out.println("entity::" + entities_all.get(i));
                    bw1.write(entities_all.get(i));
                    //System.out.println(url_index.get(i));
                    bw1.write(";");

                    bw1.write(url_index.get(i));
                    bw1.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

                if (bw1 != null)
                    bw1.close();

                if (fw1 != null)
                    fw1.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }





    }
}