package pt.ua.bioinformatics.arabella;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Spider is the public interface of this package.
 * Instances of this class are configured through a set
 * of options and a representation a map of the searcheable
 * Web. These configurations are found in an XML based
 * configuration file, specified at construction time or at any later
 * time.
 * 
 * @author Carlos Davide Pinto
 * @author Instituto de Engenharia Electronica e Telematica de Aveiro
 */
public class Spider {

	/**
	 * MyResolver exists solely to resolve the DTD files inside jar files.
	 * 
	 * @author Carlos Davide Pinto
	 * @author Instituto de Engenharia Electr�nica e Telem�tica de Aveiro
	 */
	class MyResolver implements EntityResolver {
		public InputSource resolveEntity (String publicId, String systemId)
		{
		//	if ( systemId.equals("http://www.ieeta.pt/bioinformatics/arabella/config-1.0.dtd") ||
			//     publicId.equals("-//IEETA//DTD ARABELLA CONFIG 1.0//EN")) {
				// return a special input source
				return new InputSource( Spider.class.getResourceAsStream("config_1_0.dtd") );
		//	} else {
				// use the default behaviour
		//		return null;
		//	}
		}
	}

	/**
	 * MyErrorHandler prevents a Spider from being misconfigured.
	 * It will abort on any error or warning.
	 * 
	 * @author Carlos Davide Pinto
	 * @author Instituto de Engenharia Electr�nica e Telem�tica de Aveiro
	 */
	class MyErrorHandler implements ErrorHandler {
		@Override
		public void error(SAXParseException exception) throws SAXException {
			throw exception;			
		}
		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
		    throw exception;
		}
		@Override
		public void warning(SAXParseException exception) throws SAXException {
			throw exception;			
		}
	}
	
    /**
     * Constructor.
     * Instantiates a Spider and configures it based on 
     * the XML file specified as the argument.
     * 
     * @param configfilepath Path to the configuration file
     * @throws ParserConfigurationException Invalid configuration
     * @throws SAXException Invalid configuration
     * @throws IOException Error reading configuration file
     */
	public Spider( String configfilepath )
		throws ParserConfigurationException, SAXException, IOException
	{
		// remember your creator!!! :)
		this.configfilepath = configfilepath;
		
		// parse and validate the configuration file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		factory.setIgnoringComments(true);
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		documentBuilder.setEntityResolver( new MyResolver() );
		documentBuilder.setErrorHandler( new MyErrorHandler() );
		Document config = documentBuilder.parse( configfilepath );
			
		// create options
		// if option node does not exist use defaults from dtd/xsd
		Node optionnode = config.getElementsByTagName("option").item(0);
		if ( optionnode != null )
			this.option = new Option(optionnode);
		else
			this.option = new Option( (Node) config.createElement("option") );
		
		// create the webmap
		Node webmapnode = config.getElementsByTagName("webmap").item(0); 
		this.webmap = new Webmap(webmapnode, this);
		
		// initialize containers in a thread safe map
		this.results = Collections.synchronizedMap( new HashMap<String, ArrayList<String>>() );
		for( String s: getContainersIds( config ) )
			this.results.put( s , new ArrayList<String>() );
		
		// calculate where each Stop saves
		Map<Stop,ArrayList<String>> saves = new HashMap<Stop,ArrayList<String>>();
		
		for(Stop s: this.webmap.stops){
			
			saves.put(s, new ArrayList<String>());
			for(Act a: s.acts)
				saves.get(s).add(a.save);
			
		}
		
		// calculate thread dependencies based on the above
		this.deps = new HashMap<Stop,ArrayList<Stop>>();
		
		for(Stop s1: this.webmap.stops){
			
			this.deps.put(s1, new ArrayList<Stop>());
			
			ArrayList<String> needed_containers = new ArrayList<String>();
			
			if( s1.loc_attr_type.equals("HTTP") ) 
				needed_containers.add(s1.loc_attr_vals);
			else if( s1.loc_attr_type.equals("CNTN") || s1.loc_attr_type.equals("LINK") )
				needed_containers.add(s1.loc);
			
			for(Act a1: s1.acts)
				if( a1.grep != null ) needed_containers.add(a1.grep);

			for( String needed_container: needed_containers )
				for(Stop s2: this.webmap.stops)
					if(saves.get(s2).contains(needed_container))
						this.deps.get(s1).add(s2);

			/*
			String needed_container;
			if( s1.loc_attr_type.equals("HTTP") ) 
				needed_container = s1.loc_attr_vals;
			else if( s1.loc_attr_type.equals("CNTN") || s1.loc_attr_type.equals("LINK") )
				needed_container = s1.loc;
			else
				needed_container = null;
			
			if(needed_container != null)
			    for(Stop s2: this.webmap.stops)
				    if(saves.get(s2).contains(needed_container))
				    	this.deps.get(s1).add(s2);
				*/	
			
		}
	}
	
	/**
	 * Stores text in a container.
	 * 
	 * @param containerId ID of the container
	 * @param content The text to be saved
	 */
	public synchronized void put(String containerId, String content)
	{
		if( !this.results.containsKey(containerId) )
			this.results.put( containerId , new ArrayList<String>() );
		
		if( this.option.no_repeat == true && this.results.get(containerId).contains(content) ){
			if(this.option.debug) 
				System.out.println( "=== SKIPPING INSERSION : " + content + " ===" );
		}
		else
			this.results.get(containerId).add( new String(content) );
			
	}
	
	/**
	 * Retrieves contents from a container.
	 * 
	 * @param containerId ID of container to retrieve contents from
	 * @return a String[] with the contents of the container 
	 * @throws Exception container does not exist
	 */
	public synchronized String[] get(String containerId) throws Exception
	{
		// REQUIRES: containerId must be an existent container
		if ( this.results.get(containerId) == null )
			throw new Exception( "Error: container '" + containerId + "' does not exist." );
	  
		return this.results.get(containerId).toArray(new String[0]);
	}
	
	/**
	 * Retrieves the names of the containers in the configuration file.
	 * 
	 * @param config Parsed configuration file 
	 * @return a String[] containing the names of all the containers in the configuration
	 */
	private static String[] getContainersIds(Document config)
	{
		NodeList containers = config.getElementsByTagName("save");
		ArrayList<String> containerIds = new ArrayList<String>();
		for( int i=0; i<containers.getLength(); ++i ){
			if (!containerIds.contains(containers.item(i).getTextContent()))
				containerIds.add( containers.item(i).getTextContent() );
		} 
		
		String[] s = new String[ containerIds.size() ];
		s = containerIds.toArray(s);
		
		return s;
	}
	
	/**
	 * Execute spider actions.
	 * Web content is retrieved, processed and the results of the search
	 * are stored in the {@link Spider#results} map. 
	 * @throws Exception if an error occurs 
	 */
	public void go()
		throws Exception
	{
		// print the config
		if( this.option.debug == true ) printConfig();
			
		// DUMP preparation 
		if( this.option.dump ){
			
			File dump_dir = new File(this.option.dump_dir);
			if( !dump_dir.exists() )
			    throw new Exception( "dump directory "+ this.option.dump_dir  +" does not exist." );
			
			String dump_root_fname = this.option.dump_key + "_";
			for( String s : this.get(this.option.dump_key) )
				dump_root_fname = dump_root_fname + s + "_";
			dump_root_fname = dump_root_fname + (new Date()).toString();
			// replace characters not allowed in containers
			dump_root_fname = dump_root_fname.replaceAll("[\\\\/><\\*\\?\":\\|]+", "_");
            dump_root_fname = this.option.dump_dir + dump_root_fname;

            // this cannot be in printConfig because it has a timestamp
            if(this.option.debug)
				System.out.println( "dumps stored in: " + dump_root_fname );
            ////
            
			this.dump_root_fname = dump_root_fname;			
			File dump_root = new File( this.dump_root_fname );
			if( !dump_root.mkdir() )
				throw new Exception("unable to crate directory `" + this.dump_root_fname + "'.");
			
			 BufferedReader cfgrd = new BufferedReader( new InputStreamReader( new FileInputStream(new File(this.configfilepath) ) ) );
			 BufferedWriter cfgwr = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(new File(this.dump_root_fname + "/config.xml") ) ) );
			 
			 String line = "";
			 while ( (line = cfgrd.readLine()) != null ){
			     cfgwr.write(line + "\n");
			 }
			 cfgwr.flush();
		}
		///////////////////
		
		if( !this.option.dry_run ){
			Properties systemProperties = System.getProperties();
            // set proxy
			if( !this.option.proxy.equals("") ){
				String proxy[] = this.option.proxy.split(":");
				if( proxy.length != 2 ) throw new Exception( "Bad proxy configuration" );
				
				systemProperties.setProperty("http.proxyHost",proxy[0]);
				systemProperties.setProperty("http.proxyPort",proxy[1]);
			}

			
			if( this.option.threaded ){
	            for(Stop s: this.webmap.stops)
	            	s.start();
	            for(Stop s: this.webmap.stops)
	            	s.join();
			}
			else{
				for(Stop s: this.webmap.stops)
				    s.exec();	
			}
			
			
			System.out.println( systemProperties.getProperty("http.proxyHost") );
			System.out.println( systemProperties.getProperty("http.proxyPort") );
		}
		    
	}
	
	private void printConfig() {
		System.out.println("=== CURRRENT CONFIG ===\n");
		
		// OPTIONS
		System.out.println( "- OPTIONS -" );
		
		System.out.println( "debug = " + this.option.debug );
		System.out.println( "no_repeat = " + this.option.no_repeat );
		System.out.println( "xfrm_separator = " + this.option.xfrm_separator );
		
		System.out.println( "dry_run = " + this.option.dry_run );
		System.out.println( "dump = " + this.option.dump );
		System.out.println( "dump_dir = " + this.option.dump_dir );
		System.out.println( "dump_key = " + this.option.dump_key );
		System.out.println( "threaded = " + this.option.threaded );
		
		System.out.println( "proxy = " + this.option.proxy );
		
		System.out.println( "-----------\n"); 
		
		// WEBMAP
		System.out.println( "- WEBMAP -");
		for(Stop s: this.webmap.stops){
			System.out.println( "stop id=\"" + s.id + "\" {" );
			System.out.println( "    location {" );
			System.out.println( "        (" + s.loc_attr_type + ") " + s.loc );
		   if( !s.loc_attr_post.equals("") )
			System.out.println( "        POST: " + s.loc_attr_post );
		   if( !(s.loc_attr_keys.equals("") || s.loc_attr_vals.equals("")) )
			System.out.println( "        '" + s.loc_attr_keys + "' = " + s.loc_attr_vals );
			System.out.println( "    }" );
		 for(Act a: s.acts){
		    System.out.println( "    action {" );
		   if( a.grep != null )
			System.out.println( "        grep: " + a.grep ); 
		   if( a.seek != null )
			System.out.println( "        seek: " + a.seek );
		   if( a.xfrm != null ){	
		    System.out.println( "        transform: ");
		    for( String e: a.xfrm )
			System.out.println( "            " + e );
		   }
		   if( a.splt != null )
			System.out.println( "        split by: " + a.splt );
			System.out.println( "        save in: [" + a.save + "]");
			System.out.println( "    }" );
		 }
			System.out.println( "}\n" );
		}
		System.out.println( "----------\n" );
	
		// THREAD DEPENDENCIES
		System.out.println( "- DEPENDENCIES -");
		for( Stop s: this.webmap.stops ){
			System.out.print( s.id + "  :  " );
			if( this.deps.get(s).size() == 0 ) System.out.print("{none}");
			for( Stop dep: this.deps.get(s)  )
				System.out.print( dep.id + ", " );
			System.out.println();
		}
		System.out.println( "----------------\n");
		
		System.out.println( "=======================\n" );
	}

	/**
	 * Contains all the configuration options for this Spider.
	 */
	protected Option option;
	/**
	 * Object based representation of the navigation/processing map. 
	 */
	protected Webmap webmap;
	/**
	 * Results for the execution of this Spider.
	 * Each key in the map is the container name, while each value is a reference
	 * to an ArrayList containing the texts stored in that container.
	 */
	protected Map<String, ArrayList<String>> results;
	/**
	 * Dependencies between stops (needed for threaded Spiders).
	 * Each key is a reference to a Stop whose dependencies are
	 * the Stops in the respective value (ArrayList).
	 */
	protected Map<Stop, ArrayList<Stop>> deps;
	/**
	 * Dump root directory name (needed for dumps).
	 */
	protected String dump_root_fname;
	/**
	 * Path to the configuration file
	 */
	protected String configfilepath;
}
