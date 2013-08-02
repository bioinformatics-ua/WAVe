package pt.ua.bioinformatics.wave.actions;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.GeneList;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/content/{hgnc}/{$event}")
public class ContentActionBean implements ActionBean {

    private ActionBeanContext context;
    private Gene gene = null;
    private String hgnc;
    private GeneList genelist = GeneList.INSTANCE;
    private String description = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeneList getGenelist() {
        return genelist;
    }

    public void setGenelist(GeneList genelist) {
        this.genelist = genelist;
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

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    /**
     * Handles simple gene views.
     *
     * @return
     */
    @DefaultHandler
    public Resolution view() {
        hgnc = API.sanitize(hgnc);
        try {
            gene = genelist.getGene(hgnc);
        } catch (Exception e) {
            System.out.println("[View] Unable to load " + hgnc + " info\n\t" + e.toString() + "\n");
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ForwardResolution("/content.jsp");
    }

    /**
     * Handles AJAX calls to load gene description from Freebase
     *
     * @deprecated
     */
    public Resolution free() {
        return new StreamingResolution("text/html", API.getFreebase(hgnc));
    }
}
