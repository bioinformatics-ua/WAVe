package pt.ua.bioinformatics.arabella;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Option class has the configurable options of a Spider.
 *  
 * @author Carlos Davide Pinto
 * @author Instituto de Engenharia Electronica e Telematica de Aveiro
 */
class Option {

	/**
	 * Constructor.
	 * Generates the options for this Spider based
	 * on the option node of the XML config file.
	 * @param optionnode
	 */
	public Option(Node optionnode) {
		// configure Arabella with the values from the option element
		this.debug = boolConv( ((Element) optionnode).getAttribute("debug") );
		this.xfrm_separator = ((Element) optionnode).getAttribute("xfrm_separator");
		this.no_repeat = boolConv( ((Element) optionnode).getAttribute("no_repeat") );
		
		this.dump = boolConv( ((Element) optionnode).getAttribute("dump") );
		this.dump_dir = ((Element) optionnode).getAttribute("dump_dir");
		this.dump_key = ((Element) optionnode).getAttribute("dump_key");
		this.dry_run = boolConv( ((Element) optionnode).getAttribute("dry_run") );
		
		this.threaded = boolConv( ((Element) optionnode).getAttribute("threaded") );
		
		this.proxy = ((Element) optionnode).getAttribute("proxy");
	}

	// validation performed by XML parser 
	/**
	 * Converts a String to a boolean value
	 */
	private static boolean boolConv( String sb ){
		if( sb.equals("true") )
			return true;
		else
			return false;
	}
	
	/**
	 * String used to separate regular expression from its replacement in the xfrm element.
	 */
	protected String xfrm_separator;
	/**
	 * If true, skip insertion of texts that are already in a container
	 */
	protected boolean no_repeat;
	/**
	 * If true, print debugging output
	 */
	protected boolean debug;
	/**
	 * If true, the Spider does not run, only checks configuration 
	 */
	protected boolean dry_run;
	/**
	 * If true, generate a dump of all the content retrieved from the web
	 */
	protected boolean dump;
	/**
	 * Path to the directory under which the dump directories will be created
	 */
	protected String  dump_dir;
	/**
	 * Name of the container which has the search keys 
	 */
	protected String  dump_key;
	/**
	 * If true, this Spider will run using multiple threads
	 */
    protected boolean threaded;
    /**
     * 
     */
    protected String  proxy;
}
