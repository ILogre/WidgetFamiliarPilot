package utils;

import java.io.*;
import java.net.URL;

/**
 * Created by ivan on 26/04/2016.
 */

public class CSV2FM {


    //private static int nbCol = 16;
    //private static int nbLig = 26;


    public static void transformCSV2FM(String inputCSV,int nbCol,int nbLig) throws IOException {

        String inputPath = "src/main/resources/"+inputCSV+".csv";

        File outputFm = new File("src/main/resources/"+inputCSV+"_fms_functions.fml");
        if(!outputFm.exists())
            try {
                outputFm.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        String outputFmPath = outputFm.getPath();


        // File -> String
        String[][] table = new String[nbLig][nbCol];
        int i,j;


        InputStream ips = new FileInputStream(inputPath);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String ligne;
        i = 0;
        while ((ligne = br.readLine()) != null) {
            j = 0;
            for (String mot : ligne.split(";"))
                if(j<nbCol)
                    table[i][j++] = mot;
            i++;
        }
        br.close();


        StringBuilder fms = new StringBuilder();
        for(int c=2;c<nbCol;c++){
            fms.append("FM(widget:Name Library Concern Input Output;");

            fms.append(" Name:\""+table[0][c]+"\";");
            fms.append(" Library:\""+inputCSV+"\";");
            //fms.append(" Unique:\""+table[0][c]+"."+inputCSV+"\"");
            String category = "";
            for(int l =1;l<nbLig;l++) {
                if(!table[l][0].equalsIgnoreCase(category)) {
                    category = table[l][0];
                    fms.append("; "+category+": ");
                }
                if(table[l][c].equals("Oui")) {
                    if(category.equalsIgnoreCase("Input") | category.equalsIgnoreCase("Output"))
                        fms.append("\""+table[l][1]+ "\" ");
                    else
                        fms.append(table[l][1]+" ");
                }
            }

            fms.append(";)\n");
        }
        try{
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFmPath), "utf-8"));
            writer.write(fms.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

}

