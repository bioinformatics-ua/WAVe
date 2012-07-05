package pt.ua.bioinformatics.wave.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.wave.services.Settings;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/status")
public class StatusActionBean implements ActionBean {

    private ActionBeanContext context;
    private String version = "";
    private String dbBuild = "";
    private String host = "";
    private boolean api = false;
    private boolean geneverse = false;
    private boolean database = false;
    private boolean arabella = false;
    private boolean varcrawler = false;
    private boolean realtime = false;
    private int variants = 0;
    private int genes = 0;
    private int pointers = 0;

    public int getGenes() {
        return genes;
    }

    public void setGenes(int genes) {
        this.genes = genes;
    }

    public int getPointers() {
        return pointers;
    }

    public void setPointers(int pointers) {
        this.pointers = pointers;
    }

    public int getVariants() {
        return variants;
    }

    public void setVariants(int variants) {
        this.variants = variants;
    }

    public boolean isRealtime() {
        return realtime;
    }

    public void setRealtime(boolean realtime) {
        this.realtime = realtime;
    }

    public boolean isArabella() {
        return arabella;
    }

    public void setArabella(boolean arabella) {
        this.arabella = arabella;
    }

    public boolean isDatabase() {
        return database;
    }

    public void setDatabase(boolean database) {
        this.database = database;
    }

    public boolean isGeneverse() {
        return geneverse;
    }

    public void setGeneverse(boolean geneverse) {
        this.geneverse = geneverse;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isVarcrawler() {
        return varcrawler;
    }

    public void setVarcrawler(boolean varcrawler) {
        this.varcrawler = varcrawler;
    }

    public boolean isApi() {
        return api;
    }

    public void setApi(boolean api) {
        this.api = api;
    }

    public String getDbBuild() {
        return dbBuild;
    }

    public void setDbBuild(String dbBuild) {
        this.dbBuild = dbBuild;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @DefaultHandler
    public Resolution view() {
        try {
            if (!Settings.isLoaded()) {
                Settings.load();
            }
            version = Settings.getVersion();
            dbBuild = Settings.getDbBuild();
            api = Settings.getModules().get("api");
            arabella = Settings.getModules().get("arabella");
            geneverse = Settings.getModules().get("geneverse");
            database = Settings.getModules().get("database");
            varcrawler = Settings.getModules().get("varcrawler");
            realtime = Settings.getModules().get("realtime");
            setVariants(Settings.getVariants());
            setGenes(Settings.getGenes());
            setPointers(Settings.getPointers());
        } catch (Exception ex) {
            System.out.println("[StatusActionBean] unable to load WAVe status");
        }
        return new ForwardResolution("/status.jsp");
    }
}
