package pt.ua.bioinformatics.arabella;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Tester {

	public static void main(String[] args) throws Exception {
		/*
		 * //try{ if(args.length != 3) throw new Exception(
		 * "usage: Tester <configfile> <key> <value>" );
		 * 
		 * Spider spd = new Spider( args[0] ); spd.put(args[1], args[2]);
		 * spd.go();
		 * 
		 * System.out.println("\n------------ Results -------------\n");
		 * 
		 * String[] names = spd.results.keySet().toArray(new String[0]);
		 * Arrays.sort(names);
		 * 
		 * int counter = 0; for(String s: names ){ String[] tt = spd.get(s);
		 * System.out.println( s + " (" + tt.length +")" ); for(String t: tt)
		 * System.out.println( "  '-> " + t ); System.out.println(); if(
		 * tt.length > 0 ) ++counter; }
		 * 
		 * System.out.println( counter + " of " + spd.results.keySet().size() +
		 * " with content" ); //}
		 * 
		 * //catch(Exception e){ //System.out.println( e.getMessage() ); //} //
		 */

	/*	BufferedReader page = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File("/tmp/morbidmap"))));
		// StringBuffer sbtext = new StringBuffer();
		Set<String> set = new HashSet<String>();
		String line = page.readLine();
		int counter = 0;

		while (line != null) {

			String procline = line.replaceAll(".*,\\s(\\d{6})\\s\\(.*", "$1");

			if (procline.matches("\\d{6}")) {
				set.add(procline);
//				System.out.println(procline);
			}

			line = page.readLine();
			++counter;
		}

		System.out.println(counter + ":" + set.size());
		
		String[] omims = set.toArray(new String[0]);
		Arrays.sort(omims);
		
		counter = omims.length;
		
	*/	PrintStream fout = new PrintStream( new FileOutputStream(new File("c:\\vars2.xml")) );
		String gene = "DMD";
		//int index = 1;
		
		fout.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		fout.println("<genes>"); 
	//	for (String omim : omims) {
			fout.println("  <gene>");
			fout.println("    <hgncID>" + gene + "</hgncID>"); 

			Spider spd = new Spider("cfg/sampleconfig.xml");
			//spd.put("omim", omim);
			spd.go();

			fout.println("    <variants>");
			for (String e : spd.get("vars")) {
//				System.out.println(e);
				fout.println("        <variant>" + e + "</variant>");
			}
			fout.println("    <variants>");
			System.out.println( spd.get("vars").length + " vars found " );

			fout.println("  </gene>");
			//System.out.println(index++ +" of "+counter+ "done");
		//}
		fout.println("</genes>"); 
	}
}
