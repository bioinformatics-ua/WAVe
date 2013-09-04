package pt.ua.bioinformatics.wave.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.tools.Indexer;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/cache")
public class CacheActionBean implements ActionBean {

    private ActionBeanContext context;
 
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
//            addressUA = API.getUA(query, 29951);
            Indexer.cacheGeneInfo();
        } catch (Exception e) {
            System.out.println("[CacheActionBean][] Unable to load indexer\n\t" + e.toString() + "\n");
        } finally {
  //          return new RedirectResolution(addressUA, false);
        }
        return new StreamingResolution("text","loading");
    }
}
