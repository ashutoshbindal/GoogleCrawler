import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class parseOllie
{
    public static void main(String[] args) throws IOException
    {
        //String data = "one|two|three|four"+"\n"+"one|two|three|four";
        PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter("csv_triples_ollie.csv")));

        try (BufferedReader br = new BufferedReader(new FileReader("triples_ollie.txt")))
        {
            String line;
            int counter = 1;
            //System.out.println(line.length());
            while ((line = br.readLine()) != null)
            {
                if(line.length() > 0)
                {

                    if(line.charAt(0)=='0' && line.charAt(1)=='.')
                    {
                        int a = line.indexOf(':');
                        String conf = line.substring(0,a);
                        double confidence = Double.parseDouble(conf);

                        int b = line.indexOf(';');
                        String entity1 = line.substring(a+3,b);
                        entity1 = entity1.replaceAll(",", "");
                        entity1 = entity1.replaceAll("\\s+", "_");

                        line = line.substring(b+2);
                        String relation = line.substring(0, line.indexOf(';'));
                        relation = relation.replaceAll(",", "");
                        relation = relation.replaceAll("\\s+", "_");

                        String entity2 = line.substring(line.indexOf(';')+2, line.length());
                        entity2 = entity2.replaceAll(",", "");
                        entity2 = entity2.replaceAll("\\s+", "_");

                        //System.out.println(confidence + ":" + entity1 + ":" + relation + ":" + entity2 + "::" + line);
                        String csv = entity1 + ";" + relation + ";" + entity2;
                        f.print(counter);
                        f.print(";");
                        f.print(csv);
                        f.print(";");
                        f.println(confidence);
                    }

                }
                if(line.length() == 0)
                {
                    counter = counter + 1;
                }

            }
        }

        f.close();

    }
}
