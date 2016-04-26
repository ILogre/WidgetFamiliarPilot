package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by ivan on 26/04/2016.
 */

public class CSV2FM {

    private static String INPUT_FILE = CSV2FM.class.getClassLoader().getResource("fms.csv").getPath();
    private static String OUTPUT_FM_FILE = CSV2FM.class.getClassLoader().getResource("fms_functions.fml").getPath();
    private static String OUTPUT_PRODUCTS_FILE = CSV2FM.class.getClassLoader().getResource("fms_products.fml").getPath();
    private static int nbCol = 13;
    private static int nbLig = 13;
    private static int nbLigSup = 14;


    public static void main(String[] args) {
        // File -> String
        String[][] table = new String[nbLigSup][nbCol];
        int i,j;

        try {
            InputStream ips = new FileInputStream(INPUT_FILE);
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
        for(int c=1;c<13;c++){
            fms.append("fm"+c+" = FM(widget:Name");
            for(int l=1;l<nbLig;l++){
                if(table[l][c].equals("Oui"))
                    fms.append(" "+table[l][0]);
            }
            for(int l=nbLig;l<nbLigSup;l++)
                fms.append(" "+table[l][0]);
            fms.append("; Name:\""+table[0][c]+"\";");
            for(int l=nbLig;l<nbLigSup;l++)
                fms.append(" "+table[l][0]+":"+table[l][c]+";");
            fms.append(")\n");
        }
        try{
            Writer writer = null;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FM_FILE), "utf-8"));
            writer.write(fms.toString());
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
            for(int l=nbLig;l<nbLigSup;l++)
                products.append(" "+table[l][0]+", "+table[l][c]+",");
            products.deleteCharAt(products.length()-1);
            products.append("]\n");
        }
        try{
            Writer writer = null;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_PRODUCTS_FILE), "utf-8"));
            writer.write(products.toString());
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
        }


        System.out.println(fms.toString());
        System.out.println(products.toString());

/*		for(int m=0;m<table.length;m++){
			for(int n=0;n<table[m].length;n++){
				System.out.print(table[m][n]+" ");
			}
			System.out.println("");
		}
*/
    }

}

