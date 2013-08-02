<%-- 
    Document   : browse
    Created on : 17/Fev/2010, 16:01:04
    Author     : pedrolopes
--%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:layout-definition>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
        "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/favicon.ico" />">
        <meta name="description" content="WAVe | Web Analysis of the Variome | Browsing genes: WAVe is a variome integration application, focused on providing a centralized access to online available locus-specific databases and genomic variants.">
        <meta name="keywords" content="WAVe, web analysis of the variome, wavegene, aveiro, bioinformatics, http://bioinformatics.ua.pt, diseasecard, gen2phen, g2p, http://gen2phen.org, university of aveiro, universidade de aveiro, variome, variation, variant, hvp, human variome project, hgvs, lsdb, locus specific database, trembl, swissprot, expasy, uniprot, ncbi, ensembl, gene expression, genewave, g2pwave, biowave, genetic, genomic, lovd, integration, analysis, data integration, service composition, towards a portable personal health record">
        <title><s:layout-component name="title">WAVe</s:layout-component></title>
        <s:layout-component name="scripts">
            <jsp:include page="/layout/scripts.jsp" />
        </s:layout-component>
        <script src="<c:url value="/resources/javascript/defuscate.js" />" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {               
                // (pseudo)form handling
                $.widget("custom.catcomplete", $.ui.autocomplete, {
                    _renderMenu: function( ul, items ) {
                        var self = this,
                        currentCategory = "";
                        $.each( items, function( index, item ) {
                            if ( item.category != currentCategory ) {
                                ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
                                currentCategory = item.category;
                            }
                            self._renderItem( ul, item );
                        });
                    }
                });
                $("#hgnc").catcomplete({
                    delay: 500,
                    source: 'http://bioinformatics.ua.pt/WAVe/autocomplete'
                });
                $("#submit").click(function(){
                    window.location.href = "http://bioinformatics.ua.pt/WAVe/search/" + $("#hgnc").attr("value");
                });
                $("#hgnc").keypress(function(e){
                    if(e.keyCode == 13) {
                        window.location.href = "http://bioinformatics.ua.pt/WAVe/search/" + $("#hgnc").attr("value");
                    }
                });
                $(function() {
                    var $placeholder = $('input[title]');
                    if ($placeholder.length > 0) {
                        var attrPh = $placeholder.attr('title');
                        $placeholder.attr('value', attrPh)
                        .bind('focus', function() {
                            var $this = $(this);
                            if($this.val() === attrPh)
                                $this.val('').css('color','#171207');

                        }).bind('blur', function() {
                            var $this = $(this);
                            if($this.val() === '')
                                $this.val(attrPh).css('color','#333');
                        });
                    }
                });
                $('.mail').defuscate();
            });
        </script>
    </head>
    <body>
        <div id="header">
            <s:layout-component name="header">
                <jsp:include page="/layout/header.jsp"/>
            </s:layout-component>
        </div>
        <div id="extra">
            <s:layout-component name="content" />
        </div>
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
</html>
</s:layout-definition>
