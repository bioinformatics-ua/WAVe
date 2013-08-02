<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<s:layout-render name="/layout/extras.jsp">
    <s:layout-component name="title">
        About WAVe
    </s:layout-component>
    <s:layout-component name="content">        
        <h1>About</h1>
        <div id="pink">
        <p>
            <b>WAVe | Web Analysis of the Variome</b> is a web-based application developed to overcome
            a severe lacking of integration applications related to the Human Variome. <br />
            <b>WAVe</b> was born with the main purpose of integrating locus specific databases and
            gathering available genomic variants in a single working environment.<br />
            <b>WAVe</b>'s goals and requirements are directly connected with the European <a href="http://www.gen2phen.org" title="GEN2PHEN Project" target="_blank">GEN2PHEN Project</a>.
        </p>
        <p>
            <b>WAVe</b> contains essential gene-related information in a simple tree (just like the ones you are used to in your operating system!). <br />
            <b>WAVe</b>'s gene tree enriches gathered genomic datasets with the most relevant gene-related data available.
        </p>
        </div><div id="gray">
        <h2>Version 1.6 Updates</h2>
        <h3>Features</h3>
        <ul>
            <li><strong>Updated UI</strong></li>
            <li><strong>Updated API</strong></li>
            <li><strong>Added dbSNP data</strong></li>
        </ul>

        <h2>Information</h2>

            <b>WAVe</b> currently covers all genes in HGNC approved genes list. However, only about 10% of those genes possess curated variome information. Curated genes can be filtered in the gene browsing page.<br />
            WAVe autonomous variant gather(only variants described in the <a href="http://www.hgvs.org/mutnomen/" title="HGVS Variant Nomenclature" target="_blank">HGVS</a> format are considered).<br />
            <a href="http://bioinformatics.ua.pt/WAVe/gene/DMD" title="WAVe | View | DMD" target="_blank">DMD</a> is the gene
            with more unique variants available, reaching almost 2500 distinct variants.<br />More information is available at the <a href="http://bioinformatics.ua.pt/?p=152" title="WAVe | Web Analysis of the Variome" target="_blank">UA.PT Bioinformatics homepage</a>.

        </div>
        <div id="white">
        <h2>Acknowledgement</h2>

            <b>WAVe</b> was developed at the <a href="http://bioinformatics.ua.pt" title="UA.PT Bioinformatics" target="_blank">University of Aveiro's Bioinformatics Group</a>. <br />
            This work is being funded by the EC (FP7/2007-2013) under grant agreement 200754 (project <a href="http://www.gen2phen.org" title="GEN2PHEN Project" target="_blank">GEN2PHEN</a>).

        </div>
    </s:layout-component>
</s:layout-render>
