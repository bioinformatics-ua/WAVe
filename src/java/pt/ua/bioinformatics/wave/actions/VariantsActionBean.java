package pt.ua.bioinformatics.wave.actions;

import java.util.ArrayList;
import java.util.Collections;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.ua.bioinformatics.wave.domain.Gene;
import pt.ua.bioinformatics.wave.services.GeneList;
import pt.ua.bioinformatics.wave.domain.Leaf;
import pt.ua.bioinformatics.wave.domain.Type;
import pt.ua.bioinformatics.wave.domain.Variant;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.VariantCompare;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/variants/{hgnc}/{$event}")
public class VariantsActionBean implements ActionBean {

    private ActionBeanContext context;
    private Gene gene = null;
    private ArrayList<Variant> variants = new ArrayList<Variant>();
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

    public ArrayList<Variant> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<Variant> variants) {
        this.variants = variants;
    }

    /**
     * Forwards to a page with all variants.
     *
     * @return
     */
    @DefaultHandler
    public Resolution all() {
        if (!API.isLoaded()) {
            API.load();
        }
       /* try {
            // System.out.println("[WAVe][Variants] loading variants from Redis cache");
            return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":all"));
        } catch (Exception ex) {*/
            JSONObject stream = new JSONObject();
            JSONArray variantList = new JSONArray();
            if (!API.isLoaded()) {
                API.load();
            }
            gene = genelist.getGene(hgnc.toUpperCase());
            variants = gene.getVariants("all");
            Collections.sort(variants, new VariantCompare());
            for (Variant v : variants) {
                if (v.getVariant().startsWith(":")) {
                    v.setVariant(v.getVariant().replace(":", ""));
                }
                JSONArray vList = new JSONArray();
                String vars = "";
                if (v.getNumberOfSources() == 1) {
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        if (!v.getRefseq().equals("")) {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getRefseq() + ":" + v.getVariant() + "</a>");
                        } else {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getVariant() + "</a>");

                        }
                    }
                } else if (v.getNumberOfSources() > 1) {
                    vars += "<a class='var' rel='source' href='#' title='";
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        vars += "<a href=\"" + l.getName() + "\">" + l.getN() + " at " + l.getValue().substring(0, 20) + "...</a><br />";
                    }
                    if (!v.getRefseq().equals("")) {
                        vars += "'>" + v.getRefseq() + ":" + v.getVariant() + "</a>";
                    } else {
                        vars += "'>" + v.getVariant() + "</a>";
                    }
                    vList.add(vars);
                }
                boolean found = false;
                for (Type t : API.getTypes().values()) {
                    if (v.getChangeType() == t.getId()) {
                        vList.add(t.getName());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    vList.add("Unknown");
                } else {
                    found = false;
                }
                vList.add(String.valueOf(v.getNumberOfSources()));
                vList.add(String.valueOf(v.getN()));
                variantList.add(vList);
            }
            stream.put("aaData", variantList);

            return new StreamingResolution("text/javascript", stream.toJSONString());
        //}
    }

    /**
     * Forwards to a page with substitutions.
     *
     * @return
     */
    public Resolution sub() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // System.out.println("[WAVe][Variants] loading sub variants from Redis cache");
          //  return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":sub"));
            gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/javascript", generateVars("sub"));
        } catch (Exception ex) {
            return new StreamingResolution("text/javascript", "{}");
        }
    }

    /**
     * Forwards to a page with deletions.
     *
     * @return
     */
    public Resolution del() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // System.out.println("[WAVe][Variants] loading del variants from Redis cache");
          //  return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":del"));
            gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/javascript", generateVars("del"));
        } catch (Exception ex) {
            return new StreamingResolution("text/javascript", "{}");            
        }
    }

    /**
     * Forwards to a page with duplications.
     *
     * @return
     */
    public Resolution dup() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // System.out.println("[WAVe][Variants] loading dup variants from Redis cache");
            //return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":dup"));
            gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/javascript", generateVars("dup"));
        } catch (Exception ex) {
            return new StreamingResolution("text/javascript", "{}");
        }
    }

    /**
     * Forwards to a page with insertions.
     *
     * @return
     */
    public Resolution ins() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // System.out.println("[WAVe][Variants] loading ins variants from Redis cache");
           // return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":ins"));
             gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/javascript", generateVars("ins"));
        } catch (Exception ex) {
            return new StreamingResolution("text/javascript", "{}");
        }
    }

    /**
     * Forwards to a page with inversions.
     *
     * @return
     */
    public Resolution inv() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // System.out.println("[WAVe][Variants] loading inv variants from Redis cache");
            //return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":inv"));
            gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/javascript", generateVars("inv"));
        } catch (Exception ex) {
            return new StreamingResolution("text/javascript", "{}");
        }
    }

    /**
     * Forwards to a page with conversions.
     *
     * @return
     */
    public Resolution con() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // System.out.println("[WAVe][Variants] loading con variants from Redis cache");
            //return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":con"));
             gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/javascript", generateVars("con"));
        } catch (Exception ex) {
            return new StreamingResolution("text/javascript", "{}");
        }
    }

    /**
     * Forwards to a page with deletions/insertions.
     *
     * @return
     */
    public Resolution delins() {
        if (!API.isLoaded()) {
            API.load();
        }
        try {
            // System.out.println("[WAVe][Variants] loading delins variants from Redis cache");
            //return new StreamingResolution("text/javascript", API.getJedis().get("wave:variant:" + hgnc + ":delins"));
             gene = genelist.getGene(hgnc.toUpperCase());
            return new StreamingResolution("text/javascript", generateVars("delins"));
        } catch (Exception ex) {
            return new StreamingResolution("text/javascript", "{}");
        }
    }

    /**
     * Generates JSON object containing gene variants according to their change
     * type, for usage with jQuery DataTables plugin.
     *
     * @param type the Variant change type
     * @return
     */
    String generateVars(String type) {
        JSONObject stream = new JSONObject();
        JSONArray variantList = new JSONArray();
        Type t = API.getTypes().get(type);
        variants = gene.getVariants(type);
        Collections.sort(variants, new VariantCompare());
        try {
            for (Variant v : variants) {
                JSONArray vList = new JSONArray();
                String vars = "";
                if (v.getNumberOfSources() == 1) {
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        if (!v.getRefseq().equals("")) {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getRefseq() + ":" + v.getVariant() + "</a>");
                        } else {
                            vList.add("<a class=\"var\" href=\"" + l.getName() + "\" title=\"All at " + l.getValue().substring(0, 20) + "...\">" + v.getVariant() + "</a>");

                        }
                    }
                } else if (v.getNumberOfSources() > 1) {
                    vars += "<a class='var' rel='source' href='#' title='";
                    for (Leaf l : v.getSources()) {
                        if (l.getName().contains("home.php")) {
                            String[] tmp = l.getName().split("home.php");
                            String[] getgene = l.getName().split("=");
                            String lsdb_gene = getgene[1];
                            l.setName(tmp[0] + "variants.php?select_db=" + lsdb_gene + "&action=search_all&search_Variant/DNA=" + v.getVariant());
                        }
                        vars += "<a href=\'" + l.getName() + "\'>" + l.getN() + " at " + l.getValue().substring(0, 20) + "...</a><br />";
                    }
                    if (!v.getRefseq().equals("")) {
                        vars += "'>" + v.getRefseq() + ":" + v.getVariant() + "</a>";
                    } else {
                        vars += "'>" + v.getVariant() + "</a>";
                    }
                    vList.add(vars);
                }
                vList.add(t.getName());
                vList.add(String.valueOf(v.getNumberOfSources()));
                vList.add(String.valueOf(v.getN()));
                variantList.add(vList);
            }
            stream.put("aaData", variantList);
        } catch (Exception ex) {
            System.out.println("[VariantActionBean] unable to generate variant info " + ex.toString());
        }

        return stream.toString();
    }
}
