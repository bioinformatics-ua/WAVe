<%--
    Document   : deafult
    Created on : 17/Fev/2010, 15:45:37
    Author     : pedrolopes
--%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="IE" value="${fn:contains(header['User-Agent'],'MSIE')}" />
<s:layout-definition>
    <html>
        <head>
            <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/favicon.ico" />">
            <link rel="search" type="application/opensearchdescription+xml" href="<c:url value="/opensearch.xml" />" title="WAVe gene search" />
            <title>
                <s:layout-component name="title">
                    WAVe | Web Analysis of the Variome
                </s:layout-component>
            </title>
            <meta name="description" content="WAVe | Web Analysis of the Variome: WAVe is a variome integration application, focused on providing a centralized access to online available locus-specific databases and genomic variants.">
            <meta name="keywords" content="WAVe, web analysis of the variome, wavegene, aveiro, bioinformatics, http://bioinformatics.ua.pt, diseasecard, gen2phen, g2p, http://gen2phen.org, university of aveiro, universidade de aveiro, variome, variation, variant, hvp, human variome project, hgvs, lsdb, locus specific database, trembl, swissprot, expasy, uniprot, ncbi, ensembl, gene expression, genewave, g2pwave, biowave, genetic, genomic, lovd, integration, analysis, data integration, service composition, towards a portable personal health record">
            <s:layout-component name="scripts">
                <jsp:include page="/layout/scripts.jsp" />
            </s:layout-component>
        </head>
        <body>
            <c:choose>
                <c:when test="${IE}">
                    <div id="debug">
                        WAVe usage experience is optimal with <a href="http://www.google.com/chrome" target="_blank">Google Chrome</a>, <a href="http://www.apple.com/safari/download/" target="_blank">Apple Safari</a> or <a href="http://www.mozilla.com" target="_blank">Mozilla Firefox</a>.
                    </div>
                    <div id="header">
                        <s:layout-component name="header">
                            <jsp:include page="/layout/header.jsp"/>
                        </s:layout-component>
                    </div>
                </c:when>
                <c:otherwise>
                    <div id="header" class="fadein">
                        <s:layout-component name="header">
                            <jsp:include page="/layout/header.jsp"/>
                        </s:layout-component>
                    </div>
                </c:otherwise>
            </c:choose>
            <div id="container">
                <div id="main">
                    <s:layout-component name="main" />
                    <c:choose>
                        <c:when test="${IE}">
                            <div class="example">
                                <p class="sample">
                                    Try one of these searches: <a href="<c:url value="/gene/COL3A1" />" title="COL3A1 | Gene | WAVe" class="ex">COL3A1</a>, <a href="<c:url value="/search/P11532" />" title="P11532 | Protein | WAVe" class="ex">P11532</a>, <a href="<c:url value="/search/137800" />" title="137800 | Disease | WAVe" class="ex">137800</a> or <a href="<c:url value="/gene/*" />" title="All genes | WAVe" class="ex">view all genes</a>.</p>
                                <br /><br /><b>WAVe</b> is a human variome integration application, focused on providing unified and enriched
                                access to public locus-specific databases and genomic variation datasets.
                                <br /><br/><br /><b>UPDATE</b>: WAVe has been updated to version 1.3, please refer to <a href="<c:url value="/about" />" title="About | WAVe">the about page</a> for relevant changes.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="example fadein">
                                <p class="sample">
                                    Try one of these searches: <a href="<c:url value="/gene/COL3A1" />" title="COL3A1 | Gene | WAVe" class="ex">COL3A1</a>, <a href="<c:url value="/search/P11532" />" title="P11532 | Protein | WAVe" class="ex">P11532</a>, <a href="<c:url value="/search/137800" />" title="137800 | Disease | WAVe" class="ex">137800</a> or <a href="<c:url value="/gene/*" />" title="All genes | WAVe" class="ex">view all genes</a>.</p>
                                <br /><br /><b>WAVe</b> is a human variome integration application, focused on providing unified and enriched
                                access to public locus-specific databases and genomic variation datasets.
                                <br /><br /><br/><b>UPDATE</b>: WAVe has been updated to version 1.3, please refer to <a href="<c:url value="/about" />" title="About | WAVe">the about page</a> for relevant changes.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:choose>
                    <c:when test="${IE}">
                        <div id="footer">
                            <s:layout-component name="footer">
                                <jsp:include page="/layout/footer.jsp"/>
                            </s:layout-component>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="footer" class="fadein">
                            <s:layout-component name="footer">
                                <jsp:include page="/layout/footer.jsp"/>
                            </s:layout-component>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <noscript>
                <div id="js">
                    WAVe requires JavaScript. <a href="http://www.enable-javascript.com/" target="_blank">Learn here how to enable it</a>.
                </div>
            </noscript>
        </body>
        <script type="text/javascript">
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-12230872-4']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();
        </script>
        <script type="text/javascript" src="<c:url value="/resources/javascript/index.js" />"></script>
    </html>
</s:layout-definition>