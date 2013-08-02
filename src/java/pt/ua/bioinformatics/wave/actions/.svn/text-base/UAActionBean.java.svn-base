package pt.ua.bioinformatics.wave.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.services.API;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/ua/{query}")
public class UAActionBean implements ActionBean {

    private ActionBeanContext context;
    private String query;
    private String addressUA;

    public String getAddressUA() {
        return addressUA;
    }

    public void setAddressUA(String addressUA) {
        this.addressUA = addressUA;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    /**
     * Handles call for UniversalAccess API.
     * @return
     */
    @DefaultHandler
    public Resolution get() {
        try {
            addressUA = API.getUA(query, 29951);

        } catch (Exception e) {
            System.out.println("[GeneActionBean][get] Unable to register API and resolve " + query + "\n\t" + e.toString() + "\n");
        } finally {
            return new RedirectResolution(addressUA, false);
        }
    }
}
