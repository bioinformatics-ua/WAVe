package pt.ua.bioinformatics.arabella;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** 
 * The Act class represents a sequence of operations to do with text content.
 * 
 * @author Carlos Davide Pinto
 * @author Instituto de Engenharia Electronica e Telematica de Aveiro
 */
class Act {

	/**
	 * Constructor.
	 * Creates an Act based on a XML act node.
	 * @param actnode The XML node
	 * @param mother A reference to the Spider that owns this Act
	 */
	public Act(Node actnode, Spider mother)
	{
		this.mother = mother;
		
		Node grepnode = ((Element) actnode).getElementsByTagName("grep").item(0);
		if ( grepnode == null ) this.grep = null; 
		else this.grep = grepnode.getTextContent();
		
		Node seeknode = ((Element) actnode).getElementsByTagName("seek").item(0);
		if ( seeknode == null ) this.seek = null; 
		else this.seek = seeknode.getTextContent();
		
		NodeList xfrmnodes = ((Element) actnode).getElementsByTagName("xfrm");
		if ( xfrmnodes.getLength() == 0)
			this.xfrm = null;
		else{
			this.xfrm = new String[ xfrmnodes.getLength() ];
			for( int i=0; i<xfrmnodes.getLength(); ++i )
				this.xfrm[i] = xfrmnodes.item(i).getTextContent();
		}

		Node xpndnode = ((Element) actnode).getElementsByTagName("xpnd").item(0);
		if ( xpndnode == null ) this.xpnd = null; 
		else this.xpnd = xpndnode.getTextContent();
		
		Node spltnode = ((Element) actnode).getElementsByTagName("splt").item(0);
		if ( spltnode == null ) this.splt = null;
		else this.splt = spltnode.getTextContent();
		
		this.save = ((Element) actnode).getElementsByTagName("save").item(0).getTextContent();	
	}
	
	/**
	 * Process texts.
	 * 
	 * @param content Texts to be processed
	 * @throws Exception 
	 */
	public void proc(String[] content)
	    throws Exception
	{
		//grep
		ArrayList<String> greped = new ArrayList<String>();
		if(this.grep == null){
			for(String s: content)
				if(s.length() > 0) greped.add( new String(s) );
		}
		else{
			for(String t: this.mother.get(this.grep))
			for(String s: content)
				if( s.contains(t) ) greped.add( new String(s) );
		}
		
		//--- DEBUG ---//
		if( this.mother.option.debug == true && this.grep != null )
			for(String s: greped)
				System.out.println( "    greped: " + s );
		//-------------//
		
		//seek
		ArrayList<String> sought = new ArrayList<String>();
		if(this.seek == null){
			for(String s: greped)
				if(s.length() > 0) sought.add( new String(s) );
		}
		else{
			Pattern p = Pattern.compile(this.seek);
			for(String s: greped){
				Matcher m = p.matcher(s);
				while( m.find() )
					if(m.group().length() > 0) sought.add(m.group());
			}
		}
		
		//--- DEBUG ---//
		if( this.mother.option.debug == true && this.seek != null )
			for(String s: sought)
				System.out.println( "    found: " + s );
		//-------------//
		
		//xfrm TODO optimize this?
		ArrayList<String> transformed;
		if( this.xfrm == null ){
			transformed = sought;
		}
		else{
			transformed = new ArrayList<String>();
			
			String[] rgx = new String[ this.xfrm.length ];
			String[] sub = new String[ this.xfrm.length ]; 
			for(int i=0; i<this.xfrm.length; ++i){
				String[] pair = this.xfrm[i].split( this.mother.option.xfrm_separator );
				if( pair.length == 2 ) {
					rgx[i] = pair[0];
					sub[i] = pair[1];
				}
				else {
					rgx[i] = pair[0];
					sub[i] = "";
				}
			}
			
			
			
			for(String s: sought){
				
				for( int i=0; i<rgx.length; ++i )
					s = s.replaceAll( rgx[i], sub[i] );
				
//				for(String x: this.xfrm){
//					String[] pair = x.split( this.mother.option.xfrm_separator );
//					if( pair.length == 2 ) s = s.replaceAll(pair[0], pair[1]);
//					else s = s.replaceAll(pair[0], "");
//				}
				
				transformed.add(s);
				
			}
		}
		
		//--- DEBUG ---//
		if( this.mother.option.debug == true && this.xfrm != null )
			for(String s: transformed)
				System.out.println( "    changed to: " + s );
		//-------------//
	
		
		////////////////////
		ArrayList<String> expanded;
		if(this.xpnd == null){
			expanded = transformed;
		}
		else{
			expanded = new ArrayList<String>();
			
			String key, val;
			String[] pair = this.xpnd.split( this.mother.option.xfrm_separator );
			if( pair.length == 2 ) {
				key = pair[0];
				val = pair[1];
			}
			else {
				key = pair[0];
				val = "";
			}
			
			for(String v: this.mother.get(val))
				for(String t: transformed){
					expanded.add( t.replaceAll(key, v) );
				}
		}
		
		
		
		////////////////////
		
		
		//splt
		ArrayList<String> split;
		if( this.splt == null ){
			split = expanded;
		}
		else{
			split = new ArrayList<String>();
			for(String s: expanded){
				String[] ss = s.split( this.splt );
				for(String e: ss)
					split.add(e);
			}	
		}
		
		//save
		//ArrayList<String> split = transformed;
		for(String s: split)
			if( !s.equals("") ) this.mother.put(this.save, s);
		
		//--- DEBUG ---//
		if( this.mother.option.debug == true && split.size() > 0 )
			System.out.println("    ---> [" + this.save + "]\n");
		//--- DEBUG ---//
	}
	
	/**
	 * Reference to the Spider that owns this Act
	 */
	protected Spider mother;
	
	/**
	 * Regular expression that will filter the texts
	 */
	protected String   grep;
	
	/**
	 * Regular expression that will be searched in the texts
	 */
	protected String   seek;
	/**
	 * List of transformations
	 */
	protected String[] xfrm;
	/**
	 * Regular expression that will be used to split texts
	 */
	
	protected String   xpnd;
	
	protected String   splt;
	/**
	 * Name of the container where the results o this processing will be saved
	 */
	protected String   save;
}
