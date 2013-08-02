<%--
    Document   : variants
    Created on : 26/Fev/2010, 11:23:32
    Author     : pedrolopes
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/variant.css" />" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/dataTables.css" />" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/typo.css" />" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/topsy.css" />" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/tipsy.css" />" />
        <script src="<c:url value="/resources/javascript/jquery.min.js" />" type="text/javascript"></script>
        <script src="<c:url value="/resources/javascript/datatables.variant.js" />" type="text/javascript"></script>
        <script src="<c:url value="/resources/javascript/tipsy.js" />" type="text/javascript"></script>
        <script src="<c:url value="/resources/javascript/topsy.js" />" type="text/javascript"></script>
    </head>
    <body>
        <c:choose>
            <c:when test="${actionBean.gene.numberOfVariants == 0}">
                <h1>No variants!</h1>
                <div class="dataTables_processing">WAVe has no variants for <c:out value="${actionBean.hgnc}" escapeXml="true" />.</div>
            </c:when>
            <c:otherwise>
                <div id="data">
                    <h3><c:out value="${actionBean.hgnc}" escapeXml="true" /> variants</h3>
                    <table cellpadding="10" cellspacing="0" border="0" class="display" id="variants">
                    </table>
                    <div class="dataTables_processing">WAVe is loading, please wait</div>
                </div>
            </c:otherwise>
        </c:choose>
    </body>
    <script type="text/javascript">
        $(document).ready(function() {
            $('#variants').dataTable( {
                "sAjaxSource": '../../variants/${actionBean.hgnc}/${actionBean.type}',
                "aoColumns": [ {"sTitle": "Variant"}, {"sTitle": "Change"}, {"sTitle": "LSDBs"}, {"sTitle": "Copies"} ],
                "bPaginate" : true,
                "bFilter": true,
                "bSort": true,
                "bStateSave": true,
                "sPaginationType": "full_numbers",
                "oLanguage": {
                    "sZeroRecords": "No variants to display",
                    "sLengthMenu": 'Display <select>'+
                        '<option value="25">25</option>'+
                        '<option value="50">50</option>'+
                        '<option value="100">100</option>'+
                        '<option value="200">200</option>'+
                        '<option value="500">500</option>'+
                        '<option value="-1">All</option>'+
                        '</select> variants'
                },
                "iDisplayLength": 50,
                "aaSorting": [[ 0, "desc" ]],
                "bLengthChange" : true,
                "bAutoWidth" : false,
                "sDom": '<"top"lfp<"clear">>rt<"bottom"ip<"clear">>',
                "fnInitComplete": function() {
                    $('[rel=source]').topsy({fade: true, gravity: 'w', html: true});
                    $('.var').tipsy({delayIn: 5000, fade: true, gravity: 'w'});
                    $(".dataTables_processing").hide(); },
                "fnDrawCallback": function() {
                    $('[rel=source]').topsy({fade: true, gravity: 'w', html: true});
                    $('.var').tipsy({fade: true, gravity: 'w', html: true});}
            });
        } );
    </script>
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
