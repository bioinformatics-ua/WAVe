package pt.ua.bioinformatics.arabella;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Webmap class represents the searcheable Web of its owner Spider
 * 
 * @author Carlos Davide Pinto
 * @author Instituto de Engenharia Electronica e Telematica de Aveiro
 */
class Webmap {

	/**
	 * Constructor.
	 * Generates an object based representation of the navigation/processing map.
	 * 
	 * @param webmapnode The webmap node of the XML configuration file
	 * @param mother A reference to the Spider object that owns this webmap 
	 */
	public Webmap(Node webmapnode, Spider mother) 
	{
		NodeList stopnodes = ((Element)webmapnode).getElementsByTagName("stop");
		this.stops = new Stop[ stopnodes.getLength() ];
		for( int i=0; i<stopnodes.getLength(); ++i)
			this.stops[i] = new Stop(stopnodes.item(i), mother);	
	}

	/**
	 * The list of stops in this Spider's webmap.
	 */
	protected Stop[] stops;
}
