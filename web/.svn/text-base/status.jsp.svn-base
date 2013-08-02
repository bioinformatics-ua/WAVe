<%-- 
    Document   : help
    Created on : Mar 25, 2010, 11:08:47 AM
    Author     : pedrolopes
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<s:layout-render name="/layout/extras.jsp">
    <s:layout-component name="title">Status | WAVe</s:layout-component>
    <s:layout-component name="content">
        <h1>Status</h1>
        <br />
        <table cellpadding="20" cellspacing="20" width="80%">
            <tr>
                <td width="40%"><h3>Information</h3></td>
                <td width="40%"><h3>Modules</h3></td>
            </tr>
            <tr>
                <td width="50%">
                    <p>Version <b>${actionBean.version}</b></p>
                    <p>Database build <b>${actionBean.dbBuild}</b></p>
                    <p></p>
                    <p><b>${actionBean.genes}</b> genes</p>
                    <p><b>${actionBean.variants}</b> variants</p>
                    <p><b>${actionBean.pointers}</b> pointers</p>
                </td>
                <td width="50%">
                    <c:choose>
                        <c:when test="${actionBean.api}">
                            <p><span class="module"><img src="<c:url value="/resources/images/status_enabled.png" />" alt="Enabled" border="0" /> API</span></p>
                                </c:when>
                                <c:otherwise>
                            <p><span class="module"><img src="<c:url value="/resources/images/status_disabled.png" />" alt="Disabled" border="0" /> API</span></p>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${actionBean.geneverse}">
                            <p><span class="module"><img src="<c:url value="/resources/images/status_enabled.png" />" alt="Enabled" border="0" /> GeneVerse</span></p>
                                </c:when>
                                <c:otherwise>
                            <p><span class="module"><img src="<c:url value="/resources/images/status_disabled.png" />" alt="Disabled" border="0" /> GeneVerse</span></p>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${actionBean.database}">
                            <p> <span class="module"><img src="<c:url value="/resources/images/status_enabled.png" />" alt="Enabled" border="0" /> Database</span></p>
                                </c:when>
                                <c:otherwise>
                            <p><span class="module"><img src="<c:url value="/resources/images/status_disabled.png" />" alt="Disabled" border="0" /> Database</span></p>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${actionBean.arabella}">
                            <p> <span class="module"><img src="<c:url value="/resources/images/status_enabled.png" />" alt="Enabled" border="0" /> Arabella</span></p>
                                </c:when>
                                <c:otherwise>
                            <p><span class="module"><img src="<c:url value="/resources/images/status_disabled.png" />" alt="Disabled" border="0" /> Arabella</span></p>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${actionBean.varcrawler}">
                            <p> <span class="module"><img src="<c:url value="/resources/images/status_enabled.png" />" alt="Enabled" border="0" /> VarCrawler</span></p>
                                </c:when>
                                <c:otherwise>
                            <p> <span class="module"><img src="<c:url value="/resources/images/status_disabled.png" />" alt="Disabled" border="0" /> VarCrawler</span></p>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${actionBean.realtime}">
                            <p><span class="module"><img src="<c:url value="/resources/images/status_enabled.png" />" alt="Enabled" border="0" /> Realtime</span></p>
                                </c:when>
                                <c:otherwise>
                            <p> <span class="module"><img src="<c:url value="/resources/images/status_disabled.png" />" alt="Disabled" border="0" /> Realtime</span> </p>
                                </c:otherwise>
                            </c:choose>
                </td>
            </tr>
        </table>
    </s:layout-component>
</s:layout-render>
