<%-- 
    Document   : help
    Created on : Mar 25, 2010, 11:08:47 AM
    Author     : pedrolopes
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<s:layout-render name="/layout/extras.jsp">
    <s:layout-component name="title">Help WAVe</s:layout-component>
    <s:layout-component name="content">
        <div id="help">
            <h1>Help</h1>
            <div id="pink">
                <h2>Gene Workspace</h2>
                The gene workspace the main <b>WAVe</b> workspace. At this workspace,
                anyone can access available (and relevant) information regarding a given gene.<br />
                The interface layout is divided in three main sections (as shown in the following figure).
                <div class="image">
                    <img src="<c:url value="/resources/images/help_workspace.png" />" width="500" alt="WAVe Gene Workspace layout" />
                </div>
                <ul>
                    <li>On top (red) there is the main application header. This header is common to the
                        entire WAVe application and contains the <b>WAVe</b> logo, a search box and the information menu.</li>
                    <li>At the left (blue), there is the sidebar. This sidebar displays the set
                        of information related to a given gene organized in a tree interface. Each tree node
                        corresponds to a distinct data type. Each data type (each node),
                        is composed of a set of leafs: these leafs can be direct links to external applications
                        or a new node (representing a particular resource group). <br />
                        For instance, the node Ontology contains the set of information related to
                        the Gene Ontology. This node is divided in three nodes, each representing a distinct
                        Gene Ontology data type: molecular function, cellular component and biological process.</li>
                    <li>At the right (green), there is the main content display section. This is where external
                        applications are shown allowing a direct interaction with the original content.</li>
                </ul>
                <h3>Navigation</h3>
                <h4>Gene Tree</h4>
                <div class="image">
                    <img src="<c:url value="/resources/images/help_tree.png" />" alt="WAVe Gene tree" />
                </div>
                Traversing the <em>tree</em> will give you access to several nodes and multiple data sources.
                Each node provides access to a different type of information that is made available online by various sources. <br />
                For instance, the node <em>Locus</em> will give you direct access to information from three entities in the same spot: GeneCards, HGNC and Entrez Gene.
                <h4>Toolbox</h4>
                <div class="image">
                    <img src="<c:url value="/resources/images/help_toolbox.png" />" alt="Width toggle handle" />
                </div>
                For a better browsing experience, WAVe has a <em>Toolbox</em> on top of the gene navigation tree that provides quick access to important actions:
                <ul>
                    <li>Expand or Collapse the LiveView window for a broader look on the integrated resource</li>
                    <li>Open LiveView in a new window</li>
                    <li>View GeneMesh (when available)</li>
                    <li>Open the gene summary page in LiveView</li>
                    <li>Link for the gene tree information feed</li>
                </ul>
            </div>
            <div id="gray">
                <h2>Search</h2>
                You can search for miscellaneous data types in <b>WAVe</b>'s database using three methods: on WAVe's entry page, in the Quick Gene Search box or directly in the URL.
                <div class="image">
                    <img src="<c:url value="/resources/images/help_search.png" />" alt="WAVe search" />
                </div>
                In any of the search boxes the process is straightforward: simply starting typing the query in any <em>GO</em> box and WAVe will autocomplete your query
                with results available in <b>WAVe</b>.<br />Here's the list of data types available in search and their corresponding identifiers:
                <ul>
                    <li>Gene - HGNC symbol (<a href="http://bioinformatics.ua.pt/WAVe/search/BRCA2" title="BRCA2 | Search | WAVe">BRCA2</a>)</li>
                    <li>Disease - OMIM ID (<a href="http://bioinformatics.ua.pt/WAVe/search/137800" title="137800 | Search | WAVe">137800</a>)</li>
                    <li>Protein - UniProt ID (<a href="http://bioinformatics.ua.pt/WAVe/search/P51587" title="P51587 | Search | WAVe">P51587</a>)</li>
                    <li>Study - HGVbaseG2P ID (<a href="http://bioinformatics.ua.pt/WAVe/search/HGVST306" title="HGVST306 | Search | WAVe">HGVST306</a>)</li>
                    <li>Pharmacogenomics - PharmGKB ID (<a href="http://bioinformatics.ua.pt/WAVe/search/PA25412" title="PA25412 | Search | WAVe">PA25412</a>)</li>
                    <li>Pathway - KEGG or Reactome IDs (<a href="http://bioinformatics.ua.pt/WAVe/search/REACT_216" title="REACT_216 | Search | WAVe">REACT_216</a>)</li>
                    <li>Ontology -AmiGO ID (<a href="http://bioinformatics.ua.pt/WAVe/search/GO:0003697" title="GO:0003697 | Search | WAVe">GO:0003697</a>)</li>
                </ul><br />
                <b>Tip: </b>To view all genes, try searching for <em><a href="http://bioinformatics.ua.pt/WAVe/gene/*" title="WAVe | View all genes">*</a></em>!<br />
                <b>Tip:</b> To improve your <b>WAVe</b> navigation efficiency, you directly
                access any gene by adding its HGNC symbol to <b>WAVe</b>'s URL. Let's say
                you want to access <em>BRCA2</em> in <b>WAVe</b>, you just need to type
                in the address <em><a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA2" title="BRCA2 | WAVe" target="_blank">http://bioinformatics.ua.pt/WAVe/gene/BRCA2</a></em>.
                <br /><br />
                <h3>Results</h3>
                <div class="image">
                    <img src="<c:url value="/resources/images/help_browse.png" />" alt="WAVe search results" /><br />
                </div><p>This interface will show a summary of all genes matching your search criteria and will display, for each gene,
                    how many LSDBs there are and how many variants are available in <b>WAVe</b>. <br />
                    <b>Note:</b> The view all page shows all known genes, you can filter for genes that have at least one LSDB by selecting <em>ON</em> in the top checkbox.
                    <br />
                    <b>Tip: </b>For quicker searches, you can filter your results in the inline filter box.
                    <br />
                    Gene results are aggregated in a Gene Mesh. This mesh connects genes to a common search topic and is available in the gene's workspace, at the breadcrumbs.
                    The gene browsing feature is enabled when you provided a search term that
                    has more than one result. For instance, searching for <em>BRCA</em> (<em><a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA" title="WAVe | Search | BRCA" target="_blank">http://bioinformatics.ua.pt/WAVe/gene/BRCA</a></em>) will
                    provide two results: <a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA1" title="WAVe | View | BRCA1" target="_blank">BRCA1</a> with 4 LSDBs and 1713 variants, and <a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA2" title="WAVe | View | BRCA2" target="_blank">BRCA2</a> with 6 LSDBS and 1009 variants.
                <h3>Variant browser</h3>
                One of <b>WAVe</b>'s main features is the Variant browser. For any available gene, you can easily browse all genomic variants found online
                for that gene.<br /> For a variant to be included in <b>WAVe</b> it must be
                available online in a publicly-available locus-specific database and it must offered in the
                <a href="http://www.hgvs.org/mutnomen/recs-DNA.html" title="HGVS Nomenclature" target="_blank">HGVS-normalized DNA coding sequence description format</a>.<br />
                <b>Tip:</b> You have direct access to the page where the variant was found by clicking it's description.<br />
                <b>Tip:</b> The <em>copies</em> field in the gene variants table represents how many copies have been found for that specific variant. If the variant
                was found in more than one LSDB, you will be shown a menu to choose to which LSDB you wish to go when you click the variant description.<br />
                <b>Note:</b> For variants integrated from LOVD applications, the number of copies may not be coherent with the number of times the variant was reported in the LOVD instance.
                This is due to distinct variant counting mechanisms used in LOVD and <b>WAVe</b>.
            </div>
            <div id="white">
                <h2>API</h2>
                <b>WAVe</b>'s API provides all available information in an easy-to-use format.
                <h3>Gene data</h3>
                Gene tree data is available in Atom, RSS and JSON formats:
                <ul>
                    <li>Atom format - <a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA2/atom" title="BRCA2 Feed | WAVe" target="_blank">http://bioinformatics.ua.pt/WAVe/gene/BRCA2/atom</a></li>
                    <li>RSS format - <a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA2/rss" title="BRCA2 Feed | WAVe" target="_blank">http://bioinformatics.ua.pt/WAVe/gene/BRCA2/rss</a></li>
                    <li>JSON format - <a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA2/json" title="BRCA2 Feed | WAVe" target="_blank">http://bioinformatics.ua.pt/WAVe/gene/BRCA2/json</a></li>
                </ul>
                <h4>Organization</h4>
                Atom and RSS formats organize data in entries and categories. In WAVe, these are used as follows:
                <ul>
                    <li>Categories match the main tree data types (for example, Locus or Disease)</li>
                    <li>Entry titles are described using the entry node name and its identifier, divided with a ":" (for example, "HGVbaseGEN2PHEN:HGVST214" corresponds to the entry HGVST214 in the HGVbaseG2P node for the Study data type)</li>
                    <li>Entries are linked directly to the connected resource within WAVe, using the UniversalAccess API detailed below</li>
                </ul>
                <h3>Variant data</h3>
                Aggregated variants are also available through WAVe API. Accessing variant data is as simple as gene data. For example, COL3A1 variants:
                <ul>
                    <li>Atom format - <a href="http://bioinformatics.ua.pt/WAVe/variant/COL3A1/atom" target="_blank">http://bioinformatics.ua.pt/WAVe/variant/COL3A1/atom</a></li>
                    <li>RSS format - <a href="http://bioinformatics.ua.pt/WAVe/variant/COL3A1/rss" target="_blank">http://bioinformatics.ua.pt/WAVe/variant/COL3A1/rss</a></li>
                </ul>
                <h3>Search</h3>
                Search results can also be retrieved programmatically, where genes matching the search term are organized in a content feed. For example, searching for "COL"
                <ul>
                    <li>Atom format - <a href="http://bioinformatics.ua.pt/WAVe/search/COL/atom">http://bioinformatics.ua.pt/WAVe/search/COL/atom</a></li>
                    <li>RSS format - <a href="http://bioinformatics.ua.pt/WAVe/search/COL/rss">http://bioinformatics.ua.pt/WAVe/search/COL/rss</a></li>
                </ul>
                <h3>UniversalAccess</h3>
                UniversalAccess consists of an address resolver that uses a unique identifier to load external content directly in WAVe's LiveView.<br>
                <b>Note:</b> Links retrieved in any of the previous API methods use this method to point directly at WAVe when callbacks are triggered.
                <br />
                With this API, links in content feeds are always loaded in WAVe, providing rich gene-related context instead of single application pages. Some examples:
                <ul>
                    <li><a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA2/uniprot:P51587">http://bioinformatics.ua.pt/WAVe/gene/BRCA2/uniprot:P51587</a> - loads UniProt page for P51587 within WAVe</li>
                    <li><a href="http://bioinformatics.ua.pt/WAVe/gene/BRCA2/omim:114480">http://bioinformatics.ua.pt/WAVe/gene/BRCA2/omim:114480</a> - loads NCBI OMIM page for 114480 within WAVe</li>
                </ul>
                <b>Note: </b>These methods can also be used for LSDBs and variants. In these cases, internal WAVe identifiers and keywords are used.
            </div>
            <h2>Data sources</h2>
            <div id="sources">
                <p>
                    In WAVe, we provide access to valuable information in an elegant fashion. Though, this would not be possible
                    if others had not gathered, curated and made that data available for the scientific community.
                </p>
                <div class="boxgrid caption">
                    <a href="http://www.ebi.ac.uk" title="EBI" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/ebi.png" />" alt="EBI" title="European Molecular Biology Laboratory - European Bioinformatics Institute" /></a>
                    <div class="cover boxcaption">
                        <h4>EMBL-EBI</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.ensembl.org" title="Ensembl Genome Browser" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/ensembl.png" />" alt="Ensembl" title="Ensembl Genome Browser" /></a>
                    <div class="cover boxcaption">
                        <h4>Ensembl</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.genecards.org" title="GeneCards" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/genecards.png" />" alt="GeneCards" title="GeneCards" /></a>
                    <div class="cover boxcaption">
                        <h4>GeneCards</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.geneontology.org/" title="The Gene Ontology" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/go.png" />" alt="Gene Ontology" title="The Gene Ontology" /></a>
                    <div class="cover boxcaption">
                        <h4>Gene Ontology</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.genenames.org/" title="HUGO Gene Nomenclature Committee" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/hgnc.png" />" alt="HGNC" title="HUGO Gene Nomenclature Committee" /></a>
                    <div class="cover boxcaption">
                        <h4>HGNC</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.hgvs.org/" title="Human Genome Variation Society" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/hgvs.png" />" alt="HGVS" title="Human Genome Variation Society" /></a>
                    <div class="cover boxcaption">
                        <h4>HGVS</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://bioinf.uta.fi/IDbases/" title="IDbases" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/idbases.png" />" alt="IDbases" title="IDbases" /></a>
                    <div class="cover boxcaption">
                        <h4>IDbases</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.genome.jp/kegg/" title="Kyoto Encyclopedia of Genes and Genomes" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/kegg.png" />" alt="KEGG" title="Kyoto Encyclopedia of Genes and Genomes" /></a>
                    <div class="cover boxcaption">
                        <h4>KEGG</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.lovd.nl" title="Leiden Open-source Variation Database" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/lovd.png" />" alt="LOVD" title="Leiden Open-source Variation Database" /></a>
                    <div class="cover boxcaption">
                        <h4>LOVD</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.ncbi.nlm.nih.gov/" title="National Center for Biotechnology Information" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/ncbi.png" />" alt="NCBI" title="National Center for Biotechnology Information" /></a>
                    <div class="cover boxcaption">
                        <h4>NCBI</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.pdb.org/" title="Protein Data Bank" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/pdb.png" />" alt="PDB" title="Protein Data Bank" /></a>
                    <div class="cover boxcaption">
                        <h4>PDB</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.pharmgkb.org/" title="The Pharmacogenomics Knowledge Base" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/pharmgkb.png" />" alt="PharmGKB" title="The Pharmacogenomics Knowledge Base" /></a>
                    <div class="cover boxcaption">
                        <h4>PharmGKB</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.ncbi.nlm.nih.gov/pubmed" title="PubMed" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/pubmed.png" />" alt="PubMed" title="PubMed" /></a>
                    <div class="cover boxcaption">
                        <h4>PubMed</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://bioinformatics.ua.pt/quext/" title="Query Expansion Tool" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/quext.png" />" alt="QuExT" title="Query Expansion Tool" /></a>
                    <div class="cover boxcaption">
                        <h4>QuExT</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.reactome.org/" title="Reactome" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/reactome.png" />" alt="Reactome" title="Reactome" /></a>
                    <div class="cover boxcaption">
                        <h4>Reactome</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.isb-sib.ch/" title="Swiss Institute of Bioinformatics" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/sib.png" />" alt="SIB" title="Swiss Institute of Bioinformatics" /></a>
                    <div class="cover boxcaption">
                        <h4>SIB</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.umd.be/" title="Universal Mutation Database" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/umd.png" />" alt="UMD" title="Universal Mutation Database" /></a>
                    <div class="cover boxcaption">
                        <h4>UMD</h4>
                    </div>
                </div>
                <div class="boxgrid caption">
                    <a href="http://www.uniprot.org" title="UniProt" target="_blank"><img width="150" height="150" src="<c:url value="/resources/images/logos/uniprot.png" />" alt="UniProt" title="UniProt" /></a>
                    <div class="cover boxcaption">
                        <h4>UniProt</h4>
                    </div>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
