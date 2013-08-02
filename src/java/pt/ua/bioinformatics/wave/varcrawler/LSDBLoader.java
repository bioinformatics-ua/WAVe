package pt.ua.bioinformatics.wave.varcrawler;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.wave.services.DB;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.Settings;

/**
 * Utility to update known LSDBs.
 * <p><ol>
 *  <li>Loads all LSDBs into DB</li>
 *  <li>Removes all LOVD LSDBs</li>
 *  <li>Adds updated LOVD LSDBs</li>
 * </ol></p>
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class LSDBLoader {

    static void loadLOVD() {
        DB db = new DB();
        Gene gene;
        CSVReader reader;

        try {
            boolean b = false;
            reader = new CSVReader(new FileReader(Settings.getPath() +  "lovd.csv"), '\t','"',1);
            List<String[]> lsdbs = reader.readAll();

            for (String[] lsdb : lsdbs) {
                String info = lsdb[1];
                String address = lsdb[7];
                String[] genes = lsdb[8].split(",");
                String title = lsdb[6];
                String url = "";
                String year = lsdb[0].substring(0,4);
                
                for (String g : genes) {
                    gene = new Gene(g);

                    if (gene.getHGNC().startsWith("MT")) {
                        url = address + "home.php?select_db=" + gene.getHGNC().replace("-", "");
                    } else {
                        url = address + "home.php?select_db=" + gene.getHGNC();
                    }

                    db.connect();
                    PreparedStatement stat = null;
                    stat = db.getConnection().prepareStatement("INSERT INTO wave10_lsdb (address, info, gene, type, title, year) VALUES( ? , ? , ? ,'LOVD', ? ,?)");
                    stat.setString(1, url);
                    stat.setString(2, info);
                    stat.setString(3, gene.getHGNC());
                    stat.setString(4, title);
                    stat.setString(5,year);
                    stat.executeUpdate();

                    //db.insert("LSDB", "INSERT INTO wave#build#_lsdb (address, info, refgeneid, refnodeid, title) VALUES('" + address + "home.php?select_db=" + gene.getHGNC() + "','" + info + "'," + gene.getId() + ",1,'" + title + "')");
                    //System.out.println(gene.getHGNC() + ":" + address +"home.php?select_db=" + gene.getHGNC() + ":" + info);
                    db.close();
                }
            }
            System.out.println("Loaded all LOVD LSDBs");
        } catch (Exception e) {
            System.out.println(e.toString());
            Logger.getLogger(LSDBLoader.class.getName()).log(Level.SEVERE, null, e);

        } finally {
            db.close();
        }
    }

    static void loadAll() {
        DB db = new DB();
        Gene gene;
        CSVReader reader;

        try {
            reader = new CSVReader(new FileReader(Settings.getPath() + "lsdb_base_list.csv"), ',');
            List<String[]> lsdbs = reader.readAll();

            for (String[] lsdb : lsdbs) {

                String hgnc = lsdb[0];
                String address = lsdb[1];
                String info = lsdb[2];
                String lsdbType = "";
                gene = new Gene(hgnc);

                if (address.contains("bioinf.uta.fi")) {
                    lsdbType = "MUTbase";
                } else if (address.contains("umd.be")) {
                    lsdbType = "UMD";
                } else if (address.contains("select_db")) {
                    lsdbType = "LOVD";
                } else {
                    lsdbType = "unknown";
                }

                db.connect();
                db.insert("LSDB", "INSERT INTO wave#build#_lsdb (address, info, gene, type) VALUES('" + address + "','" + info + "','" + gene.getHGNC() + "','" + lsdbType + "')");
                db.close();
            }
            System.out.println("Loaded all LSDBs");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            Logger.getLogger(LSDBLoader.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    static void loadKC() {
        DB db = new DB();
        Gene gene;
        CSVReader reader;

        try {
            reader = new CSVReader(new FileReader(Settings.getPath() + "KCList.csv"), ',', '"', 1);
            List<String[]> lsdbs = reader.readAll();

            for (String[] lsdb : lsdbs) {
                String symbol = lsdb[0];
                String title = lsdb[2];
                String url = lsdb[3];
                String year = lsdb[4];
                String type = lsdb[5];
                String number = lsdb[6];
                String attr = lsdb[7];
                title = title.replace("'", "&#039;");

                gene = new Gene(symbol);

                if (url.contains("bioinf.uta.fi")) {
                    type = "IDBases";
                } else if (url.contains("umd.be")) {
                    type = "UMD";
                } else if (url.contains("select_db")) {
                    type = "LOVD";
                } else {
                    type = "Unknown";
                }

                //Node n = new Node(lsdbType);

                db.connect();
                db.insert("LSDB", "INSERT INTO wave.wave#build#_list (gene, kcid, title, url, YEAR, attributes, type) VALUES('" + symbol + "','" + number + "','" + title + "','" + url + "','" + year + "','" + attr + "','" + type + "')");
                db.close();
            }
            System.out.println("Loaded all LSDBs");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    static void lister() {
        DB db = new DB();
        DB dbx = new DB();

        try {
            db.connect();
            ResultSet biglist = db.getData("SELECT * FROM wave10_lsdb");
            while (biglist.next()) {
                dbx.connect();
                int results = 0;
                ResultSet byGene = dbx.getData("SELECT * FROM wave10_list WHERE gene LIKE '" + biglist.getString("gene") + "' AND url LIKE '%" + biglist.getString("address") + "'");

                while (byGene.next()) {
                    results++;
                }

                dbx.close();

                if (results == 0) {
                    //insert
                    System.out.println("Adding " + biglist.getString("address"));
                    dbx.connect();
                    PreparedStatement stat = db.getConnection().prepareStatement("INSERT INTO wave.wave10_list (gene, title, url, year, info, type) VALUES(?,?,?,?,?,?)");
                    stat.setString(1, biglist.getString("gene"));
                    stat.setString(2, biglist.getString("title"));
                    stat.setString(3, biglist.getString("address"));
                    stat.setString(4, biglist.getString("year"));
                    stat.setString(5, biglist.getString("info"));
                    stat.setString(6, biglist.getString("type"));
                    stat.executeUpdate();
                    dbx.close();
                } else if (results == 1) {
                    //update
                    System.out.println("Updating " + biglist.getString("address"));
                    dbx.connect();
                    PreparedStatement stat = db.getConnection().prepareStatement("UPDATE wave.wave10_list SET info = ? ,type = ? WHERE gene LIKE ? AND url LIKE ? ");
                    
                    stat.setString(1, biglist.getString("info"));
                    stat.setString(2, biglist.getString("type"));
                    stat.setString(3, biglist.getString("gene"));
                    stat.setString(4, biglist.getString("address"));
                    stat.executeUpdate();
                    dbx.close();
                }
            }
            db.close();

        } catch (Exception e) {
            Logger.getLogger(LSDBLoader.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    static void cleanup() {
        DB db = new DB();
        try {

            db.connect();
            db.insert("LSDB", "DELETE FROM wave#build#_lsdb WHERE type like 'LOVD'");
            db.close();

            System.out.println("Cleaned up LOVD LSDBs");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    public static void main(String[] args) {
         if (!Settings.isLoaded()) {
            Settings.load();
        }
        if (!API.isLoaded()) {
            API.load();
        }
        //loadAll();
        //cleanup();
        loadLOVD();
        //loadKC();
        //lister();

    }
}
