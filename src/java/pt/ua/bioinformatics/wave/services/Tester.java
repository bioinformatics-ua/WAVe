/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.ua.bioinformatics.wave.services;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.domain.Node;
import pt.ua.bioinformatics.wave.varcrawler.ExternalData;

/**
 *
 * @author pedrolopes
 */
public class Tester {

      public static void main(String[] args) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
          Settings.load();

          if(Settings.modules.get("api")  && Settings.modules.get("varcrawler")) {
            Gene gene = new Gene("COL3A1");
            ExternalData ed = new ExternalData();
            Node n = new Node("DiseaseCard");

            ed.readFeed(gene, "http://www.hgvbaseg2p.org/studies?q=#replaceme#&amp;format=atom", n);

          }
      }
}
