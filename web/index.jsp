<%-- 
    Document   : index
    Created on : 12/Mar/2010, 11:27:04
    Author     : pedrolopes
--%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<s:layout-render name="/layout/default.jsp">
    <s:layout-component name="header">
        <div class="high">
            <div class="menu">
                <c:choose>
                    <c:when test="${IE}">
                        <div class="menu" align="right">
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
                </c:choose></div>
        </div>
    </s:layout-component>
    <s:layout-component name="main">
        <img src="<c:url value="/resources/images/title.png" />" width="350" height="111" alt="WAVe | Web Analysis of the Variome" />
        <c:choose>
            <c:when test="${IE}">
                <div class="box">
                    <input type="text" onfocus="if (this.value == 'Search here') {this.value = '';}" onblur="if (this.value == '') {this.value = 'Search here';}" value="Search here" name="hgnc" id="hgnc" /><input id="submit" type="submit" value="GO" name="view" />
                </div>
            </c:when>
            <c:otherwise>
                <div class="box">                    
                    <div class="search">                        
                        <input type="text" onfocus="if (this.value == 'Search here') {this.value = '';}" onblur="if (this.value == '') {this.value = 'Search here';}" value="Search here" name="hgnc" id="hgnc" /><input id="submit" type="submit" value="GO" name="view" />
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </s:layout-component>
</s:layout-render>
