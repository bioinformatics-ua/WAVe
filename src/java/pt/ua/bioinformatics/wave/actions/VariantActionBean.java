package pt.ua.bioinformatics.wave.actions;

import java.util.ArrayList;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.services.GeneList;
import pt.ua.bioinformatics.wave.domain.Variant;
import pt.ua.bioinformatics.wave.services.API;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/variant/{hgnc}/{$event}")
public class VariantActionBean implements ActionBean {

    private ActionBeanContext context;
    private Gene gene = null;
    private ArrayList<Variant> variants = new ArrayList<Variant>();
    private String hgnc;
    private String type;
    private GeneList genelist = GeneList.INSTANCE;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public ArrayList<Variant> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<Variant> variants) {
        this.variants = variants;
    }

    /**
     * Forwards to a page with all variants.
     * @return
     */
    @DefaultHandler
    public Resolution all() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "all";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Forwards to a page with substitutions.
     * @return
     */
    public Resolution sub() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "sub";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Forwards to a page with deletions.
     * @return
     */
    public Resolution del() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "del";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Forwards to a page with duplications.
     * @return
     */
    public Resolution dup() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "dup";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Forwards to a page with insertions.
     * @return
     */
    public Resolution ins() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "ins";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Forwards to a page with inversions.
     * @return
     */
    public Resolution inv() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "inv";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Forwards to a page with conversions.
     * @return
     */
    public Resolution con() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "con";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Forwards to a page with deletions/insertions.
     * @return
     */
    public Resolution delins() {
        if (!API.isLoaded()) {
            API.load();
        }
        type = "delins";
        return new ForwardResolution("/variants.jsp");
    }

    /**
     * Handles call for Variant List Atom API.
     * @return
     */
    public Resolution atom() {
        try {
            gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/xml", API.getGeneVariants(gene, "atom_1.0"));
        } catch (Exception e) {
            System.out.println("[VariantActionBean] Error " + e.toString());
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Handles call for Variant List JSON API.
     * @return
     */
    public Resolution json() {
        try {
            gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/xml", API.getGeneVariants(gene, "json"));
        } catch (Exception e) {
            System.out.println("[VariantActionBean] Error " + e.toString());
            throw new UnsupportedOperationException();
        }
    }
}
