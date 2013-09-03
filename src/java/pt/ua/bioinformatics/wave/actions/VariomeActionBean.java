package pt.ua.bioinformatics.wave.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
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
@UrlBinding("/variome/{hgnc}/{$event}")
public class VariomeActionBean implements ActionBean {

    private ActionBeanContext context;
    private Gene gene = null;
    private String hgnc;
    private GeneList genelist = GeneList.INSTANCE;

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
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
    @DefaultHandler
    public Resolution csv() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            return new StreamingResolution("text", API.getJedis().get("wave:variome:" + hgnc + ":csv"));
        } catch (Exception ex) {
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
