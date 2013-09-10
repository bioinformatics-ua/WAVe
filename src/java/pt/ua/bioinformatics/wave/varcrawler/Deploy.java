package pt.ua.bioinformatics.wave.varcrawler;

import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.Index;
import pt.ua.bioinformatics.wave.services.Settings;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class Deploy {

    public static void main(String[] args) {

        if (!Settings.isLoaded()) {
            Settings.load();
        }
        if (!API.isLoaded()) {
            API.load();
        }
    /*    if (Settings.getModules().get("varcrawler")) {
            Creator c = new Creator();
            if (c.create()) {
                System.out.println("[Creator] Configuration information loaded succesfully");
            } else {
                System.out.println("[Builder] Failed to load configuration information");
            }

            /*Builder b = new Builder();
            if (b.loadLSDBs()) {
            System.out.println("[Builder] LSDBs data loaded succesfully");
            } else {
            System.out.println("[Builder] Failed to load LSDBs data");
            }/*
            ExternalData ext = new ExternalData();
            if (ext.loadExternalData()) {
            System.out.println("[Builder] GeNS data loaded successfully");
            } else {
            System.out.println("[Builder] Failed to load GeNS data");
            }/*            }/*
            
            if (b.correctGenes()) {
            System.out.println("[Deploy] Gene naming corrected");
            }*/
            /*
            if (b.cacheGenes()) {
                System.out.println("[Deploy] Gene cache updated");
            }
            if (b.cacheVariants()) {
                System.out.println("[Deploy] Variant cache updated");
            } /*
            if (b.buildIndex()) {
            System.out.println("[Deploy] Data index built");
            }
            
            // testing area
            //Index.get("P51587");
             */
        /*   
        } else {
            System.out.println("[WAVe Deploy] VarCrawler module is disabled ");
        }*/
         Builder b = new Builder();
            b.cacheVariants();
    }
}
