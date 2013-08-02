<%-- 
    Document   : header
    Created on : 17/Fev/2010, 15:47:21
    Author     : pedrolopes
--%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<div class="logo">
    <a href="<c:url value="/" />" title="Web Analysis of the Variome | WAVe"><span>WAVe</span></a>
</div>
<div class="high">
    <div class="search">
        <div class="form"> 
            <p class="searchinput"><input type="text" title="Search here" name="hgnc" id="hgnc" /></p>
            <p class="searchsubmit"><input type="submit" id="submit" value="GO" name="action" /></p>              
            <a href="<c:url value="/gene/*" />" title="View all | WAVe" class="all">View All</a>
        </div>
    </div>
    <div class="menu">
        <c:choose>
            <c:when test="${IE}">
                <div class="menu" align="right" style="text-align: center">
                    <a href="<c:url value="/about" />">About</a> |
                    <a href="<c:url value="/disclaimer" />">Disclaimer</a> |
                    <a href="<c:url value="/help" />">Help</a> |
                    <a href="<c:url value="/contact" />">Contacts</a>
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
</div>
