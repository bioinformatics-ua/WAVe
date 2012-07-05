package pt.ua.bioinformatics.wave.actions;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.ua.bioinformatics.wave.domain.Item;
import pt.ua.bioinformatics.wave.services.API;
import pt.ua.bioinformatics.wave.services.Index;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/autocomplete")
public class AutocompleteActionBean implements ActionBean {

    private ActionBeanContext context;
    private String term;
    private ArrayList<Item> items;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String q) {
        this.term = q;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    /**
     * Main resolution handler.
     * @return
     */
    @DefaultHandler
    public Resolution get() {
        JSONArray list = new JSONArray();
        try {
            items = Index.search(term);
            for (Item i : items) {
                JSONObject item = new JSONObject();
                switch (i.getNode()) {
                    case 1:
                        item.put("category", "Gene");
                        break;
                    case 91:
                        item.put("category", "Protein");
                        break;
                    case 92:
                        item.put("category", "Protein");
                        break;
                    case 83:
                        item.put("category", "Disease");
                        break;
                    case 71:
                        item.put("category", "Pharma");
                        break;
                    case 89:
                        item.put("category", "Pathway");
                        break;
                    case 99:
                        item.put("category", "Study");
                        break;
                    case 96:
                        item.put("category", "Ontology");
                        break;
                    case 97:
                        item.put("category", "Ontology");
                        break;
                    case 98:
                        item.put("category", "Ontology");
                        break;
                    case 90:
                        item.put("category", "Pathway");
                        break;
                    default:
                        item.put("category", "");
                        break;
                }
                item.put("label", i.getValue());
                list.add(item);
            }
        } catch (Exception e) {
            System.out.println("[GeneActionBean] Unable to find gene " + term + "\n\t" + e.toString());
            Logger.getLogger(AutocompleteActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return new StreamingResolution("application/json", list.toJSONString());
    }
}
