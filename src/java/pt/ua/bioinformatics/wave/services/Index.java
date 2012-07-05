/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.wave.services;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.wave.domain.Item;

/**
 *
 * @author pedrolopes
 */
public class Index {

    public static ArrayList<Item> get(String what) {
        ArrayList<Item> items = new ArrayList<Item>();
        DB db = API.getDb();
        GeneList gl = GeneList.INSTANCE;

        try {
            db.connect();
            ResultSet rs = db.indexGet(what, 1000);
            while (rs.next()) {
                items.add(new Item(rs.getString("v"),rs.getInt("refnodeid"), rs.getString("gene"), gl.getGene(rs.getString("gene"))));
            }
            db.close();
        } catch (Exception ex) {
            System.out.println("[Index] unable to get data for " + what + "\n\t" + ex.toString());
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public static ArrayList<Item> search(String what) {
        ArrayList<Item> items = new ArrayList<Item>();
        DB db = API.getDb();
        try {
            db.connect();
            ResultSet rs = db.indexSearch(what, 30);
            while (rs.next()) {
                items.add(new Item(rs.getString("v"), rs.getInt("refnodeid"), rs.getString("gene")));
            }
            db.close();
        } catch (Exception ex) {
            System.out.println("[Index] unable to get data for " + what + "\n\t" + ex.toString());
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);

        }
        return items;
    }
}
