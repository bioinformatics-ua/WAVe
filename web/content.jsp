<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>WAVe</title>
        <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/content.css" />" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/typo.css" />" />
        
        <!-- load default javascripts -->
        <script src="<c:url value="/resources/javascript/jquery.min.js" />" type="text/javascript"></script>
        <script src="<c:url value="/resources/javascript/slideshow.js" />" type="text/javascript"></script>
    </head>
    <body>
        <h1><c:out value="${actionBean.hgnc}" escapeXml="true" /> summary</h1>
        <div id="summary">
            <c:if test="${actionBean.gene.name != ''}">

                <p class="name">${actionBean.gene.name}</p>
            </c:if>
            <c:choose>
                <c:when test="${actionBean.gene.numberOfLsdbs > 0}">
                    <p class="genomic">
                        <span class="variant">${actionBean.gene.numberOfVariants}</span> unique / <b>${actionBean.gene.totalVariants}</b> total variants in <span class="lsdb">${actionBean.gene.numberOfLsdbs}</span> locus-specific databases
                    </p>
                </c:when>
                <c:otherwise>
                    <p class="novariome">WAVe has not found curated variome information regarding ${actionBean.hgnc}</p>
                </c:otherwise>
            </c:choose>           
            <c:choose>
                <c:when test="${actionBean.gene.sequence.id != 0}">
                    <p class="sequence">
                        ${actionBean.gene.sequence.info} Locus Reference Genomic | <a href="ftp://ftp.ebi.ac.uk/pub/databases/lrgex/${actionBean.gene.sequence.name}.xml">${actionBean.gene.sequence.name}</a>
                    </p>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>
            <div>
                <p class="shortcut">Shortcuts</p>
                <div class="shortcuts">
                    <c:choose>
                        <c:when test="${actionBean.gene.protein.id != 0}">
                            <div class="uniprot"><a href="http://www.uniprot.org/uniprot/${actionBean.gene.protein.name}" title="${actionBean.gene.protein.name} in Uniprot" target="frame"><span>${actionBean.gene.protein.name} in Uniprot</span></a></div>
                        </c:when>
                    </c:choose>
                            <div class="diseasecard"><a href="http://bioinformatics.ua.pt/diseasecard/search/id/${actionBean.hgnc}" target="_blank" title="${actionBean.hgnc} in DiseaseCard"><span>${actionBean.hgnc} in DiseaseCard</span></a></div>
                    <div class="gen2phenkc"><a href="http://www.gen2phen.org/gene/${actionBean.hgnc}" title="${actionBean.hgnc} in GEN2PHEN Knowledge Centre"><span>${actionBean.hgnc} in GEN2PHEN Knowledge Centre</span></a></div>
                </div>
            </div>
        </div>
        <div id="pics">
            <div class="images">
                <div class="image">
                    <img src="<c:url value="/resources/images/slide3.png" />" alt="Search" width="400" height="200" />
                    <div class="caption">
                        <span class="title">Search</span><br />
                        Search for genes in any <b>GO</b> search box.
                    </div>
                </div>
                <div class="image">
                    <img src="<c:url value="/resources/images/slide2.png" />" alt="Toolbox" width="400" height="200"/>
                    <div class="caption">
                        <span class="title">Toolbox</span><br/>
                        Explore the toolbox for more <b>LiveView</b> and <b>GeneMesh</b> features.
                    </div>
                </div>
                <div class="image">
                    <img src="<c:url value="/resources/images/slide1.png" />" alt="LiveView" width="400" height="200" />
                    <div class="caption">
                        <span class="title">LiveView</span><br/>
                        <em>Browse</em> the navigation tree for variome connections in <b>LiveView</b>.
                    </div>
                </div>
            </div>            
        </div>
    </body>
    <script type="text/javascript">
        $(document).ready(function() {
            /*$.ajax({
                url: '../content/${actionBean.hgnc}/free',
                success: function(data) {
                    var response = data.split("#");
                    if (response[1] != 'null') {
                        var stream = '<p class="information">Information from Freebase</p><p>' + response[1];
                        stream += '<a href="http://www.freebase.com/view' + response[0] + '" title="View ${actionBean.hgnc} details in Freebase"><img src="../resources/images/freebasemore.png" border="none" width="16px" height="16px" alt="..." /></a></p>';
                        $('body').append('<div id="freebase"></div>');
                        $('#freebase').html(stream);
                    } else {
                        $('body').append('<div id="freebase"></div>');
                        $('#freebase').html('<p class="information"><a href="http://www.freebase.com/search?query=' + response[0] + '" title="View ${actionBean.hgnc} details in Freebase">View ${actionBean.hgnc} details in Freebase <img src="../resources/images/freebasemore.png" border="none" width="16px" height="16px" alt="..." /></a></p');
                    }}
            })*/
            $('.images').fadeSlideShow({
                width:400,
                height:200, 
                speed:'slow', 
                interval:5000
            });
        });
    </script>
</html>
