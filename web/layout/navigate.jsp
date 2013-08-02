<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:layout-definition>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
        "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        <head>
            <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/favicon.ico" />">
            <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
            <link href="http://bioinformatics.ua.pt/WAVe/gene/${actionBean.gene.HGNC}/atom" rel="alternate" type="application/atom+xml" title="${actionBean.gene.HGNC} | WAVe" />
            <meta name="description" content="WAVe | Web Analysis of the Variome | View | ${actionBean.gene.HGNC} | ${actionBean.gene.name} | WAVe is a variome integration application, focused on providing a centralized access to online available locus-specific databases and genomic variants.">
            <meta name="keywords" content="WAVe, web analysis of the variome, ${actionBean.gene.HGNC}, ${actionBean.gene.name}, wavegene, aveiro, bioinformatics, http://bioinformatics.ua.pt, diseasecard, gen2phen, g2p, http://gen2phen.org, university of aveiro, universidade de aveiro, variome, variation, variant, hvp, human variome project, hgvs, lsdb, locus specific database, trembl, swissprot, expasy, uniprot, ncbi, ensembl, gene expression, genewave, g2pwave, biowave, genetic, genomic, lovd, integration, analysis, data integration, service composition, towards a portable personal health record">
            <title><s:layout-component name="title">WAVe</s:layout-component></title>
            <s:layout-component name="scripts">
                <jsp:include page="/layout/scripts.jsp" />
            </s:layout-component>            
        </head>
        <body>
            <div id="header">
                <s:layout-component name="header">
                    <jsp:include page="/layout/header.jsp"/>
                </s:layout-component>
                <!-- <a id="opened" class="toggler" href="#"></a>-->
            </div>
            <div id="container">
                <!-- item box (Toggle + Feed) -->
                <div id="toolbox">
                    <div class="expand">
                        <a id="opened" title="Expand LiveView" class="toggler tool" href="#"><span>Expand View</span></a>
                    </div>
                    <div class="external">
                        <a id="external" class="tool" title="LiveView in new window" href="<c:url value="/content/${actionBean.gene.HGNC}" />" target="_blank"><span>Link</span></a>
                    </div>
                    <div class="mesh">
                        <c:choose>
                            <c:when test="${actionBean.single}">
                                <a class="tool frame" title="View ${actionBean.gene.HGNC} information" value="<c:url value="/content/${actionBean.gene.HGNC}" />" target="frame"><span>Link</span></a>
                            </c:when>
                            <c:otherwise>
                                <a id="toolmesh" class="tool" title="View gene mesh" href="#" ><span>Mesh</span></a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="infocard">
                        <a class="tool frame" title="View ${actionBean.gene.HGNC} information" value="<c:url value="/content/${actionBean.gene.HGNC}" />" target="frame"><span>Link</span></a>
                    </div>
                    <div class="feed">
                        <a href="<c:url value="/gene/${actionBean.gene.HGNC}/atom" />" class="tool" title="Open ${actionBean.gene.HGNC} feed"><span>${actionBean.gene.HGNC}</span></a>
                    </div>
                </div>
                <div id="sidebar">
                    <s:layout-component name="sidebar"/>
                </div>
                <div id="content" >
                    <div id="framer">
                        <s:layout-component name="frame">
                            <iframe width="100%" height="100%" frameborder="none" id="frame" src="<c:url value="/content.jsp" />"></iframe>
                        </s:layout-component>
                    </div>
                </div>
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
        <script type="text/javascript" src="<c:url value="/resources/javascript/navigate.js" />"></script>
    </html>
</s:layout-definition>
