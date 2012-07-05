<%-- 
    Document   : browse
    Created on : 24/Fev/2010, 16:34:04
    Author     : pedrolopes
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<s:layout-render name="/layout/browse.jsp">

    <s:layout-component name="title">
        <c:choose>
            <c:when test="${actionBean.hgnc == '*'}">
                Browse all genes | WAVe
            </c:when>
            <c:otherwise>
                Browse for <c:out value="${actionBean.hgnc}" escapeXml="true" /> | WAVe
            </c:otherwise>
        </c:choose>
    </s:layout-component>
    <s:layout-component name="browse">        
        <c:choose>
            <c:when test="${actionBean.numberOfGenes == 0}">
                <h2>No results!</h2>
                <p class="warning">WAVe could not find any genes matching <b><em><c:out value="${actionBean.hgnc}" escapeXml="true" /></em></b>.</p>
            </c:when>
            <c:otherwise>
                <h2><span id="size">${actionBean.numberOfGenes}</span> results for &lt;<b><em><c:out value="${actionBean.hgnc}" escapeXml="true" /></em></b>&gt;</h2>
                <div class="variome">
                    Select <b>only</b> genes with at least one Locus-specific Database
                    <p class="field switch">
                        <label class="variome-enable"><span>On</span></label>
                        <label class="variome-disable selected"><span>Off</span></label>
                        <input type="checkbox" id="checkbox" class="checkbox" name="field2" />
                    </p>
                </div>
                <div id="browse">
                    <table cellpadding="10" cellspacing="0" border="0" class="display" id="genes">                        
                    </table>
                    <div class="dataTables_processing">WAVe is loading, please wait</div>
                </div>
            </c:otherwise>
        </c:choose>
    </s:layout-component>
</s:layout-render>
