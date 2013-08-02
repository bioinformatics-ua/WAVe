/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.ua.bioinformatics.wave.services;

import java.util.Comparator;
import pt.ua.bioinformatics.wave.domain.Gene;

/**
 *
 * @author pedrolopes
 */
public class GeneCompare implements Comparator {

    public int compare(Object o1, Object o2) {
       Gene g1 = (Gene) o1;
       Gene g2 = (Gene) o2;
       String s1 = g1.getHGNC();
       String s2 = g2.getHGNC();

       return s1.compareToIgnoreCase(s2);
    }
}
