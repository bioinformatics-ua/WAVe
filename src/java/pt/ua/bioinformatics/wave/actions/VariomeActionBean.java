package pt.ua.bioinformatics.wave.actions;

import java.io.File;
import java.io.PrintWriter;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.services.GeneList;
import pt.ua.bioinformatics.wave.services.API;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/variome/{hgnc}/{refseq}/{$event}")
public class VariomeActionBean implements ActionBean {

    private ActionBeanContext context;
    private Gene gene = null;
    private String hgnc;
    private String refseq;
    private GeneList genelist = GeneList.INSTANCE;

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    public String getRefseq() {
        return refseq;
    }

    public void setRefseq(String refseq) {
        this.refseq = refseq;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public String getHgnc() {
        return hgnc;
    }

    public void setHgnc(String hgnc) {
        this.hgnc = hgnc;
    }

    /**
     * Handles call for Variant List Atom API.
     *
     * @return
     * @deprecated
     */
    public Resolution atomm() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            //System.out.println("[WAVe][Variant] loaded variant feed from Redis cache");
            return new StreamingResolution("text/xml", API.getJedis().get("wave:variome:" + hgnc + ":atom"));
        } catch (Exception ex) {
            try {
                gene = genelist.getGene(hgnc.toUpperCase());
                return new StreamingResolution("text/xml", API.getVariome(gene, "atom_1.0"));
            } catch (Exception e) {
                System.out.println("[VariantActionBean] Error " + e.toString());
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Handles call for Variant List JSON API.
     *
     * @return
     * @deprecated
     */
    public Resolution jsonn() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            //System.out.println("[WAVe][Variant] loaded variant feed from Redis cache");
            return new StreamingResolution("application/javascript", API.getJedis().get("wave:variome:" + hgnc + ":json"));
        } catch (Exception ex) {
            try {
                gene = genelist.getGene(hgnc.toUpperCase());
                return new StreamingResolution("application/javascript", API.getVariome(gene, "json"));
            } catch (Exception e) {
                System.out.println("[VariantActionBean] Error " + e.toString());
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Handles call for Variant List JSON API.
     *
     * @return
     */
    
    public Resolution csv2() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // return new StreamingResolution("text", API.getJedis().get("wave:variome:" + hgnc + ":csv"));
            gene = genelist.getGene(hgnc.toUpperCase());
            String response = API.getJedis().get("wave:variome:" + hgnc + ":csv");
            if (!response.equals("")) {
                return new StreamingResolution("text", response);
            } else {
                gene = genelist.getGene(hgnc.toUpperCase());
                return new StreamingResolution("text", API.getVariome(gene, "csv"));
            }

        } catch (Exception ex) {
            try {
                 System.out.println("[VariomeActionBean] Error " + ex.toString());
                gene = genelist.getGene(hgnc.toUpperCase());
                gene.setRefseq(refseq);
                return new StreamingResolution("text", API.getVariome(gene, "csv"));
            } catch (Exception e) {
                System.out.println("[VariomeActionBean] Error " + e.toString());
                throw new UnsupportedOperationException();
            }
        }
    }

    @DefaultHandler
    public Resolution csv() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // return new StreamingResolution("text", API.getJedis().get("wave:variome:" + hgnc + ":csv"));
            String path = getContext().getServletContext().getRealPath("/variomes/" + hgnc + "-" + refseq);
            File f = new File(path);
            if (!f.exists()) {
                PrintWriter out = new PrintWriter(path);
                gene = genelist.getGene(hgnc.toUpperCase());
                gene.setRefseq(refseq);
                String response = API.getVariome(gene, "csv");
                out.println(response.substring(0, response.length()-1));
                out.close();
                //System.out.println("File Saved at: " + path);
                return new ForwardResolution("/variomes/" + hgnc + "-" + refseq); //getContext().getServletContext().getContextPath() + "/variomes/" + hgnc + ".csv");
                
            } else {
               // System.out.println("Redirecting...");
              return new ForwardResolution("/variomes/" + hgnc + "-" + refseq); //getContext().getServletContext().getContextPath() + "/variomes/" + hgnc + ".csv");
            }

        } catch (Exception ex) {
             System.out.println("[VariomeActionBean] Error " + ex.toString());
               
            try {
                gene = genelist.getGene(hgnc.toUpperCase());
                return new StreamingResolution("text", API.getVariome(gene, "csv"));
            } catch (Exception e) {
                System.out.println("[VariomeActionBean] Error " + e.toString());
                throw new UnsupportedOperationException();
            }
        }
    }
}
