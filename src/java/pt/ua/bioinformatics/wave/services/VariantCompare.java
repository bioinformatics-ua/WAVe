/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.ua.bioinformatics.wave.services;

import java.util.Comparator;
import pt.ua.bioinformatics.wave.domain.Variant;

/**
 *
 * @author pedrolopes
 */
public class VariantCompare implements Comparator {
    public int compare(Object o1, Object o2) {
       Variant v1 = (Variant) o1;
       Variant v2 = (Variant) o2;
       String s1 = v1.getVariant();
       String s2 = v2.getVariant();

       return s2.compareToIgnoreCase(s1);
    }
}
