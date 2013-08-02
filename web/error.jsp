<%-- 
    Document   : error
    Created on : 29/Mar/2010, 17:11:51
    Author     : pedrolopes
--%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="IE" value="${fn:contains(header['User-Agent'],'MSIE')}" />
<s:layout-render name="/layout/default.jsp">
    <s:layout-component name="title">Error</s:layout-component>
    <s:layout-component name="header">
        <div class="high">
            <c:choose>
                <c:when test="${IE}">
                    <div class="menu" align="right" style="text-align: center">
                        <a href="<c:url value="/about" />" target="_blank">About</a> |
                        <a href="<c:url value="/disclaimer" />" target="_blank">Disclaimer</a> |
                        <a href="<c:url value="/help" />" target="_blank">Help</a> |
                        <a href="<c:url value="/contact" />" target="_blank">Contacts</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="menu">
                        <ul>
                            <li><a href="<c:url value="/contact" />" class="contact">Contacts</a></li>
                            <li><a href="<c:url value="/help" />" class="help">Help</a></li>
                            <li><a href="<c:url value="/disclaimer" />" class="disclaimer">Disclaimer</a></li>
                            <li><a href="<c:url value="/about" />" class="about">About</a></li>
                        </ul>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </s:layout-component>
    <s:layout-component name="main">
        <img src="<c:url value="/resources/images/error.png" />" width="300" alt="WAVe | Error" />
        <p>
            Wow! This is embarassing... WAVe has produced and error! Do not worry, it is probably our fault!
            <br />Please <a href="javascript:history.go(-1)">go back</a> or continue your work below! Sorry!
        </p>
        <c:choose>
            <c:when test="${IE}">
                <s:form beanclass="pt.ua.bioinformatics.wave.actions.GeneActionBean" id="info" name="view">
                    <label><b>Gene</b></label><s:text name="hgnc" id="hgnc" /><s:submit value="GO" name="view" />
                </s:form>
            </c:when>
            <c:otherwise>
                <s:form beanclass="pt.ua.bioinformatics.wave.actions.GeneActionBean" id="info" name="view">
                    <div class="box">
                        <div class="search">
                            <s:text name="hgnc" id="hgnc" /><s:submit value="GO" name="view" />
                        </div>
                    </div>
                </s:form>
            </c:otherwise>
        </c:choose>
    </s:layout-component>
</s:layout-render>
