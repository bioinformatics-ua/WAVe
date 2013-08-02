/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.wave.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import pt.ua.bioinformatics.wave.domain.Variant;

/**
 *
 * @author pedrolopes
 */
public class Updater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here

        ArrayList<Variant> variants = new ArrayList<Variant>();
        DB db = new DB();
        DB dbx = new DB();

        try {
            db.connect();

            ResultSet rs = db.getData("SELECT *"
                    + " FROM wave#build#_variant");

            System.out.println("Variants read");
            while (rs.next()) {

                int changeType = 12;
                String v = rs.getString("variant");


                if (v.matches(".*con.*")) {
                    changeType = 17;
                }
                if (v.matches(".*inv.*")) {
                    changeType = 16;
                }
                if (v.matches(".*ins.*") || v.contains("+")) {
                    changeType = 15;
                }
                if (v.matches(".*del.*") || v.matches(".*-.*")) {
                    changeType = 13;
                }
                if (v.matches(".*delins.*") || v.matches(".*indel.*")) {
                    changeType = 22;
                }                
                if (v.matches(".*dup.*")) {
                    changeType = 14;
                }                
                if (v.matches(".*>.*") || v.matches(".*&gt;.*")) {
                    changeType = 12;
                }

                //insert variant into database
                if (changeType == 12) {
                    v = v.replace(">", "&gt;");
                }

                dbx.connect();
                dbx.update(v + " " + changeType, "UPDATE wave#build#_variant SET refchangetypeid = " + changeType + " WHERE wave#build#_variant.id = " + rs.getInt("id"));
                dbx.close();

                //variants.add(new Variant(rs.getInt("id"), rs.getString("variant"), rs.getInt("refchangetypeid")));
            }
            System.out.println("Variants loaded");

            db.close();

            /* int changeType = 0;

            for (Variant v : variants) {
            // test change type
            if (v.getVariant().matches(".*>.*") || v.getVariant().matches(".*&gt;.*")) {
            changeType = 12;
            }
            if (v.getVariant().matches(".*del.*")) {
            changeType = 13;
            }
            if (v.getVariant().matches(".*dup.*")) {
            changeType = 14;
            }
            if (v.getVariant().matches(".*ins.*")) {
            changeType = 15;
            }
            if (v.getVariant().matches(".*inv.*")) {
            changeType = 16;
            }
            if (v.getVariant().matches(".*con.*")) {
            changeType = 17;
            }

            db.connect();

            db.update(v.getVariant(), "UPDATE variant SET refchangetypeid = " + changeType + " WHERE variant.id = " + v.getId());

            db.close();*/



        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        }
    }
}
