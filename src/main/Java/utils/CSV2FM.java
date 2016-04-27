package utils;

import java.io.*;
import java.net.URL;

/**
 * Created by ivan on 26/04/2016.
 */

public class CSV2FM {


    //private static int nbCol = 16;
    //private static int nbLig = 26;


    public static void transformCSV2FM(String inputCSV,int nbCol,int nbLig) {

        String inputPath = "src/main/resources/"+inputCSV+".csv";

        File outputFm = new File("src/main/resources/"+inputCSV+"_fms_functions.fml");
        if(!outputFm.exists())
            try {
                outputFm.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        File outputProduct = new File("src/main/resources/"+inputCSV+"_fms_products.fml");
        if(!outputProduct.exists())
            try {
                outputProduct.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        String outputFmPath = outputFm.getPath();
        String outputProductPath = outputProduct.getPath();
        //String outputFmPath = CSV2FM.class.getClassLoader().getResource(inputCSV+"_fms_functions.fml").getPath();
        //String outputProductPath = CSV2FM.class.getClassLoader().getResource(inputCSV+"_fms_products.fml").getPath();

        // File -> String
        String[][] table = new String[nbLig][nbCol];
        int i,j;

        try {
            InputStream ips = new FileInputStream(inputPath);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            i = 0;
            while ((ligne = br.readLine()) != null) {
                j = 0;
                for (String mot : ligne.split(";"))
                    table[i][j++] = mot;
                i++;
            }
            br.close();
        } catch (Exception e) {
            System.err.println(e.toString() + e.getMessage());
        }

        StringBuilder fms = new StringBuilder();
        for(int c=1;c<nbCol;c++){
            fms.append("FM(widget:Name Library");
            //fms.append("fm"+c+" = FM(widget:Name Library");
            for(int l=1;l<nbLig;l++){
                //System.out.println("l:"+l+" c:"+c);
                if(table[l][c].equals("Oui")) {
                    fms.append(" " + table[l][0]);
                }
            }
            fms.append("; Name:\""+table[0][c]+"\";");
            fms.append(" Library:\""+inputCSV+"\";");
            fms.append(")\n");
        }
        try{
            Writer writer = null;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFmPath), "utf-8"));
            writer.write(fms.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        StringBuilder products = new StringBuilder();
        for(int c=1;c<nbCol;c++){
            products.append(table[0][c]+" = [widget,");
            for(int l=1;l<nbLig;l++){
                if(table[l][c].equals("Oui"))
                    products.append(" "+table[l][0]+",");
            }
            products.deleteCharAt(products.length()-1);
            products.append("]\n");
        }
        try{
            Writer writer = null;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputProductPath), "utf-8"));
            writer.write(products.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
        }


        //System.out.println(fms.toString());
        //System.out.println(products.toString());

/*		for(int m=0;m<table.length;m++){
			for(int n=0;n<table[m].length;n++){
				System.out.print(table[m][n]+" ");
			}
			System.out.println("");
		}
*/
    }

}

