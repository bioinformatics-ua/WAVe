<%--
    Document   : browse
    Created on : 24/Fev/2010, 16:34:04
    Author     : pedrolopes
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<s:layout-render name="/layout/search.jsp">
    <s:layout-component name="title">
        Search results for <c:out value="${actionBean.id}" escapeXml="true" /> | WAVe
    </s:layout-component>
    <s:layout-component name="browse">
        <c:choose>
            <c:when test="${actionBean.numberOfResults == 0}">
                <h2>No results!</h2>
                <p class="warning">WAVe could not find any results for <b><em><c:out value="${actionBean.id}" escapeXml="true" /></em></b>.</p>
            </c:when>
            <c:otherwise>
                <h2><span id="size">${actionBean.numberOfResults}</span> results for full text search on &lt;<b><em><c:out value="${actionBean.id}" escapeXml="true" /></em></b>&gt;</h2>
                <div id="browse">
                    <table cellpadding="10" cellspacing="0" border="0" class="display" id="genes">
                        <tbody>
                            <c:forEach var="item" items="${actionBean.items}" >
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.node == 1}">
                                                Gene ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 91}">
                                                Protein ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 92}">
                                                Protein ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 83}">
                                                Disease ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 71}">
                                                Pharma ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 90}">
                                                Pathway ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 89}">
                                                Pathway ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 99}">
                                                Study ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 96}">
                                                Ontology ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 97}">
                                                Ontology ${item.value}
                                            </c:when>
                                            <c:when test="${item.node == 98}">
                                                Ontology ${item.value}
                                            </c:when>
                                            <c:otherwise>
                                                ${item.value}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><a href="../gene/${item.g.HGNC}" title="${item.g.HGNC} | WAVe">${item.g.id}</a></td><td><a href="../gene/${item.g.HGNC}" title="${item.g.HGNC} | WAVe">${item.g.HGNC}</a></td><td>${item.g.numberOfLsdbs}</td><td>${item.g.numberOfVariants}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="dataTables_processing">WAVe is loading, please wait</div>
                </div>
            </c:otherwise>
        </c:choose>
    </s:layout-component>
</s:layout-render>
