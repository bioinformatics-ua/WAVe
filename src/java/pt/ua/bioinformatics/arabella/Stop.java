package pt.ua.bioinformatics.arabella;

import java.io.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * The Stop class is a representation of a web location, in other words where to
 * go and what to do with the information available there.
 * 
 * @author Carlos Davide Pinto
 * @author Instituto de Engenharia Electronica e Telematica de Aveiro
 */
class Stop extends Thread {

	/**
	 * WebRetriever represents a threaded request to a webserver
	 * 
	 * @author Carlos Davide Pinto
	 * @author Instituto de Engenharia Electr�nica e Telem�tica de Aveiro
	 */
	class WebRetriever extends Thread {

		public WebRetriever(String url, String postdata) {
			this.url = url;
			this.postdata = postdata;
			this.response = null;
		}

		// public String getResponse(){
		// return this.response;
		// }

		@Override
		public void run() {
			this.response = getFromWeb(this.url, this.postdata);
		}

		protected String url;
		protected String postdata;
		protected String response;
	}

	
	/**
	 * Constructor. Creates a Stop based on a xml stop node.
	 * 
	 * @param stopnode
	 *            The stop node of the XML configuration file.
	 * @param mother
	 *            A reference to the Spider who owns this Stop
	 */
	public Stop(Node stopnode, Spider mother) {
		this.mother = mother;

		this.id = ((Element) stopnode).getAttribute("id");

		this.loc = stopnode.getFirstChild().getTextContent();
		this.loc_attr_type = ((Element) stopnode.getFirstChild())
				.getAttribute("type");
		this.loc_attr_post = ((Element) stopnode.getFirstChild())
				.getAttribute("post");
		this.loc_attr_keys = ((Element) stopnode.getFirstChild())
				.getAttribute("keys");
		this.loc_attr_vals = ((Element) stopnode.getFirstChild())
				.getAttribute("vals");
		
		String ri = ((Element) stopnode.getFirstChild())
				.getAttribute("ri");
		
		String to = ((Element) stopnode.getFirstChild())
		        .getAttribute("to");

		try{
		    this.loc_attr_ri = Integer.parseInt(ri);
		}
		catch (NumberFormatException e) {
			this.loc_attr_ri = 0;
		}
		if(this.loc_attr_ri < 0) this.loc_attr_ri = 0;
		
		try{
			this.loc_attr_to = Integer.parseInt(to);
		}
		catch (NumberFormatException e) {
			this.loc_attr_to = 0;
		}
		if(this.loc_attr_to < 0) this.loc_attr_to = 0;
		
		NodeList actnodes = ((Element) stopnode).getElementsByTagName("act");
		this.acts = new Act[actnodes.getLength()];
		for (int i = 0; i < actnodes.getLength(); ++i)
			this.acts[i] = new Act(actnodes.item(i), this.mother);
		
	}

	// ////////////

	/**
	 * Threaded execution of a Stop. This method honors dependencies between
	 * Stops (it waits for the completion of all Stops that it depends on) then
	 * calls {@link Stop#exec()}.
	 */
	@Override
	public void run()
	{
		try {
			sleep(200); /// TODO: better way of handling the thread liveness problem
			            //  wait until all threads are alive
			
			for (Stop s : this.mother.deps.get(this))
				s.join();
		
			
			this.exec();
		} catch (InterruptedException i) {
			System.out.println("Stop.run : InterruptedException -- "
					+ i.getMessage());
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Executes the actions of this Stop.
	 * 
	 * @throws Exception
	 *             A problem occurred retrieving content.
	 */
	public void exec() throws Exception {
		String[] content = this.getContent();

		// --- DEBUG/ ---//
		if (this.mother.option.debug == true) {
			System.out.println("stop id = " + this.id + "\n content ("
					+ content.length + ")");
			for (String s : content)
				System.out.println("  - (" + s.length() + ") "
						+ (s.length() > 60 ? s.substring(0, 55) + "..." : s));
		}
		// --- /DEBUG ---//

		for (Act a : this.acts)
			a.proc(content);
	}

	/**
	 * Gets web content.
	 * 
	 * @param url
	 * @param postdata
	 * @return A String that is the response to a request
	 */
	private String getFromWeb(String url, String postdata) {
		if (url == null || postdata == null) {
			System.out.println("Something null");
			return new String();
		}

		// TODO: FIX BUG
		// even though it says in the documentation that openConnection().getInputStream()
		// should behave exactly an openStream(), there was a bug in java that would break
		// the GET method for some sites
		// so the alternative was to break the POST method instead
		// maybe we should check if the bug has been fixed
		try {
						
			java.net.URL u = new java.net.URL(url);
			//java.net.URLConnection conn = u.openConnection();
			
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) u.openConnection();
			//javax.net.ssl.HttpsURLConnection con = (javax.net.ssl.HttpsURLConnection)conn;
			
			conn.setRequestProperty("User-Agent", "Arabella-Web-Spider/1.2.0");
			conn.setConnectTimeout(this.loc_attr_to);
			

			if (!postdata.equals("")) {
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(postdata);
				wr.flush();
			}

//			BufferedReader page = new BufferedReader(new InputStreamReader(conn
//					.getInputStream()));
			BufferedReader page = new BufferedReader(new InputStreamReader(u.openStream()));
			StringBuffer sbtext = new StringBuffer();
			String line;
			while ((line = page.readLine()) != null)
				sbtext.append(line).append("\n");

			return sbtext.toString();
		} catch (Exception e) {
			System.out.println("=== EXCEPTION: " + e.getClass().getSimpleName()
					+ " : " + e.getMessage() + " ===");
			return new String();
		}
	}

	/**
	 * Gets the contents specified by this Stop's loc field.
	 * 
	 * @return A String with the contents
	 * @throws Exception
	 *             Retriever threads were interrupted, or the container needed
	 *             by this Stop does not exist
	 */
	private String[] getContent() throws Exception {
		String[] content = null;

		if (this.loc_attr_type.equals("HTTP")) {
			// get contents from the web

			String[] urls;
			String[] posts;

		    if( !this.loc_attr_vals.equals("") ){

//				String[] keyvalues;
//				keyvalues = this.mother.get(this.loc_attr_vals);
//				urls = new String[keyvalues.length];
//				posts = new String[keyvalues.length];
//
//				// key/value expansion
//				for (int i = 0; i < urls.length; ++i) {
//					urls[i] = this.loc.replaceAll(this.loc_attr_keys, keyvalues[i]);
//					posts[i] = this.loc_attr_post.replaceAll(this.loc_attr_keys,
//							keyvalues[i]);
//				}
				
				String[] inurls = new String[1];
				inurls[0] = this.loc;
				urls = expand( inurls );
				String[] inposts = new String[1];
				inposts[0] = this.loc;
				posts = expand( inposts );
			}
			else{
                urls = new String[1]; urls[0] = this.loc;
                posts = new String[1]; posts[0] = this.loc_attr_post; 
			}
			
			// launch the retriever threads
			content = new String[urls.length];
			WebRetriever[] wrts = new WebRetriever[urls.length];
			for (int i = 0; i < urls.length; ++i) {
				wrts[i] = new WebRetriever(urls[i], posts[i]);
				wrts[i].start();
				Thread.sleep(this.loc_attr_ri);
			}
			for (int i = 0; i < urls.length; ++i)
				wrts[i].join();
			for (int i = 0; i < urls.length; ++i)
				content[i] = wrts[i].response;

			// DUMP
			if (this.mother.option.dump)
				dump(wrts);
			// /////
		}

		else if (this.loc_attr_type.equals("LINK")) {
			// get contents from the web, following the links in a container
			
			String[] links;
			
			if( !this.loc_attr_vals.equals("") )
			    links = expand(this.mother.get(this.loc));
			else
			    links = this.mother.get(this.loc);
			
			content = new String[ links.length ];

			WebRetriever[] wrts = new WebRetriever[links.length];
			for (int i = 0; i < links.length; ++i) {
				wrts[i] = new WebRetriever(links[i], "");
				wrts[i].start();
				Thread.sleep(this.loc_attr_ri);
			}
			for (int i = 0; i < links.length; ++i)
				wrts[i].join();
			for (int i = 0; i < links.length; ++i)
				content[i] = wrts[i].response;

			// DUMP
			if (this.mother.option.dump)
				dump(wrts);
			// /////
		}

		else if (this.loc_attr_type.equals("CNTN")) {
			// get contents from a container directly
			content = this.mother.get(this.loc);

		}
		/*
		 * else if ( this.loc_attr_type.equals("FSYS") ) { // get content from
		 * the local filesystem // open a file and read it // return the
		 * contents // not implemented yet !!! // TODO open file for reading
		 * content = new String[1]; content[0] =
		 * "Contents of the file bla bla bla"; }
		 */
		return content;
	}

	/**
	 * Generates dumps of retrieved content.
	 * 
	 * @param wrts
	 *            Array of WebRetrievers created by this object
	 */
	private void dump(WebRetriever[] wrts) {
		// create directory for this stop
		String stop_root = this.mother.dump_root_fname + File.separatorChar
				+ this.id.replaceAll("[\\\\/><\\*\\?\":\\|]+", "_");
		File stopdir = new File(stop_root);
		stopdir.mkdir();

		// create a request log
		// File requestlog = new File( stop_root + "/request.log" );

		try {
			// create a request log and save all files
			FileWriter reqlog_stream = new FileWriter(stop_root
					+ "/request.log");
			BufferedWriter reqlog = new BufferedWriter(reqlog_stream);

			for (int i = 0; i < wrts.length; ++i) {
				reqlog.write(i
						+ " :: "
						+ wrts[i].url
						+ (wrts[i].postdata.equals("") ? "" : " POST "
								+ wrts[i].postdata) + "\n");
				FileWriter response_stream = new FileWriter(stop_root + "/" + i
						+ ".htm");
				BufferedWriter response = new BufferedWriter(response_stream);
				response.write(wrts[i].response);
				response.close();
			}

			reqlog.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	
	private String[] expand( String[] in )
	    throws Exception
	{
		String[] keyvalues = this.mother.get(this.loc_attr_vals);
		String[] expanded = new String[ keyvalues.length * in.length ];
		
		for( int i = 0; i < in.length; ++i )
			for( int j = 0; j < keyvalues.length; j++ )
				expanded[ i * keyvalues.length + j ] = in[ i ].replaceAll(this.loc_attr_keys, keyvalues[ j ]);
		
		return expanded;
	}
	/**
	 * Reference to the Spider who owns this Stop
	 */
	protected Spider mother;

	/**
	 * Identifier for this Stop
	 */
	protected String id;

	/**
	 * Location for the contents
	 */
	protected String loc;
	/**
	 * Type of operation required to retrieve contents
	 */
	protected String loc_attr_type;
	/**
	 * PostData to send with a request
	 */
	protected String loc_attr_post;
	/**
	 * Regular expression to be replaced in the URL
	 */
	protected String loc_attr_keys;
	/**
	 * Name of the container from wich the values will be taken to replace the
	 * Regular expression loc_attr_keys
	 */
	protected String loc_attr_vals;

	/**
	 * Request interval in miliseconds.
	 */
	protected int loc_attr_ri;

	/**
	 * Request timeout int miliseconds
	 */
	protected int loc_attr_to;

	
	
	/**
	 * The list of Acts that this Stop performs on the data once it is retrieved
	 */
	protected Act[] acts;

	/**
	 * The list of Stops this Stop depends on
	 */
	protected Stop[] deps;
}
