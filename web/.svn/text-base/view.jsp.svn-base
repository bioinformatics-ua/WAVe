<%-- 
    Document   : view
    Created on : 25/Fev/2010, 17:04:07
    Author     : pedrolopes
--%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<s:layout-render name="/layout/navigate.jsp">
    <s:layout-component name="title">${actionBean.gene.HGNC} | WAVe</s:layout-component>    
    <s:layout-component name="sidebar">
        <!-- breadcrumbs, gene name and tools -->
        <div class="infozone">

            <div class="breadcrumb">
                <div class="home"><a href="http://bioinformatics.ua.pt/WAVe" title="WAVe | Web Analysis of the Variome" target="_top">Home</a></div>
                <c:choose>
                    <c:when test="${actionBean.previous == true}">
                        <c:choose>
                            <c:when test="${actionBean.type == 'gene'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="<c:url value="/content/${actionBean.gene.HGNC}" />" target="frame">Gene ${actionBean.gene.HGNC}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Search ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"> - <em><a href="http://bioinformatics.ua.pt/WAVe/search/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></em> - </span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${actionBean.type == 'search'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="<c:url value="/search/${actionBean.id}" />">Search ${actionBean.id}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Search ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"> - <em><a href="http://bioinformatics.ua.pt/WAVe/search/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></em> - </span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${actionBean.type == 'protein'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="<c:url value="/protein/${actionBean.id}" />">Protein ${actionBean.id}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Protein ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"><a href="http://bioinformatics.ua.pt/WAVe/protein/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${actionBean.type == 'pharma'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="http://bioinformatics.ua.pt/WAVe/pharma/${actionBean.id}" title="${actionBean.id} WAVe" target="_top">Pharma ${actionBean.id}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Pharma ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"><a href="http://bioinformatics.ua.pt/WAVe/pharma/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${actionBean.type == 'disease'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="http://bioinformatics.ua.pt/WAVe/disease/${actionBean.id}" title="${actionBean.id} WAVe" target="_top">Disease ${actionBean.id}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Disease ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"><a href="http://bioinformatics.ua.pt/WAVe/disease/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${actionBean.type == 'pathway'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="http://bioinformatics.ua.pt/WAVe/pathway/${actionBean.id}" title="${actionBean.id} WAVe" target="_top">Pathway ${actionBean.id}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Pathway ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"><a href="http://bioinformatics.ua.pt/WAVe/pathway/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${actionBean.type == 'study'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="http://bioinformatics.ua.pt/WAVe/study/${actionBean.id}" title="${actionBean.id} WAVe" target="_top">Study ${actionBean.id}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Study ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"><a href="http://bioinformatics.ua.pt/WAVe/study/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${actionBean.type == 'ontology'}">
                                <c:choose>
                                    <c:when test="${actionBean.single}">
                                        <div class="sub"><a href="http://bioinformatics.ua.pt/WAVe/ontology/${actionBean.id}" title="${actionBean.id} WAVe" target="_top">Ontology ${actionBean.id}</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="sub"><a href="#" id="mesher" title="${actionBean.id} | WAVe">Ontology ${actionBean.id} (${fn:length(actionBean.mesh)})</a></div>
                                        <div class="hiddenMesh" id="mesh">
                                            <span class="title"><a href="http://bioinformatics.ua.pt/WAVe/ontology/${actionBean.id}" title="${actionBean.id} | WAVe" target="_top">${actionBean.id}</a></span>
                                            <div class="links">
                                                <ul>
                                                    <c:forEach var="g" items="${actionBean.mesh}">
                                                        <li><a href="http://bioinformatics.ua.pt/WAVe/gene/${g.HGNC}" title="${g.HGNC} | WAVe" target="_top">${g.HGNC}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:when test="${actionBean.previous == false}">
                        <div class="sub"><a href="<c:url value="/content/${actionBean.gene.HGNC}" />" target="frame">Gene ${actionBean.gene.HGNC}</a></div>
                    </c:when>
                </c:choose>
            </div>
            <span class="symbol"><a href="<c:url value="/content/${actionBean.gene.HGNC}" />" target="frame">${actionBean.gene.HGNC}</a></span>
            <div id="sidetreecontrol">
                <a class="treelink" href="">Collapse tree</a> or <a class="treelink" href="">Expand tree</a>
            </div>
        </div>
        <div class="treezone">
            <!-- atart the tree -->
            <ul id="tree" class="filetree">
                <c:forEach var="type" items="${actionBean.datatypes}">
                    <c:choose>
                        <c:when test="${type.name=='LSDB'}">
                            <c:choose>
                                <c:when test="${actionBean.gene.numberOfLsdbs == 0}">
                                    <li class="closed"><span class="empty">LSDB</span></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="open"><span class="folder open">${type.name}</span>
                                        <ul>
                                            <c:forEach var="node" items="${type.nodes}">
                                                <c:forEach var="leaf" items="${node.leafs}">
                                                    <li><span class="file"><a title="${node.shortname}: ${fn:substring(leaf.value,0,25)}..." class="frame" value="${leaf.value}">${node.shortname} ${actionBean.gene.HGNC}</a></span></li>
                                                </c:forEach>
                                            </c:forEach>
                                        </ul>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:when test="${type.name=='Variation'}">
                            <c:choose>
                                <c:when test="${actionBean.gene.numberOfVariants == 0}">
                                    <li><span class="empty">${type.name}</span></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="open"><span class="folder">${type.name}</span>
                                        <ul>                                         
                                            <li><span class="file"><a title="Variant: ${actionBean.gene.numberOfVariants} variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/all" />" >All</a></span></li>
                                            <c:choose>
                                                <c:when test="${actionBean.gene.variantCon < 1 && actionBean.gene.variantInv < 1 && actionBean.gene.variantIns < 1 && actionBean.gene.variantDup < 1 && actionBean.gene.variantSub < 1 && actionBean.gene.variantDel < 1}">
                                                    <li><span class="empty">Change Type</span></li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="closed"><span class="folder">Change Type</span>
                                                        <ul>
                                                            <c:if test="${actionBean.gene.variantSub > 0}">
                                                                <li><span class="file"><a title="Variant: ${actionBean.gene.variantSub} substitution variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/sub" />">Substitution</a></span></li>
                                                            </c:if>
                                                            <c:if test="${actionBean.gene.variantDel > 0}">
                                                                <li><span class="file"><a title="Variant: ${actionBean.gene.variantDel} deletions variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/del" />">Deletions</a></span></li>
                                                            </c:if>
                                                            <c:if test="${actionBean.gene.variantIns > 0}">
                                                                <li><span class="file"><a title="Variant: ${actionBean.gene.variantIns} insertions variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/ins" />">Insertions</a></span></li>
                                                            </c:if>
                                                            <c:if test="${actionBean.gene.variantInv > 0}">
                                                                <li><span class="file"><a title="Variant: ${actionBean.gene.variantInv} inversions variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/inv" />">Inversion</a></span></li>
                                                            </c:if>
                                                            <c:if test="${actionBean.gene.variantDup > 0}">
                                                                <li><span class="file"><a title="Variant: ${actionBean.gene.variantDup} duplications variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/dup" />">Duplications</a></span></li>
                                                            </c:if>
                                                            <c:if test="${actionBean.gene.variantCon > 0}">
                                                                <li><span class="file"><a title="Variant: ${actionBean.gene.variantCon} conversions variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/con" />">Conversion</a></span></li>
                                                            </c:if>
                                                            <c:if test="${actionBean.gene.variantDelins > 0}">
                                                                <li><span class="file"><a title="Variant: ${actionBean.gene.variantDelins} deletion/insertion variants" class="frame" value="<c:url value="/variant/${actionBean.gene.HGNC}/delins" />">Deletion/Insertion</a></span></li>
                                                            </c:if>
                                                        </ul>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                        </ul></li>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${type.name == 'Publication'}">
                            <li class="closed"><span class="folder">${type.name}</span>
                                <ul>
                                    <c:forEach var="node" items="${type.nodes}">
                                        <c:choose>
                                            <c:when test="${node.shortname == 'PubMed'}">
                                                <li><span class="folder">PubMed</span><ul>
                                                        <li><span class="file"><a title="${node.shortname}: ${actionBean.gene.HGNC}" class="frame" value="${fn:replace(node.value,"#replaceme#",actionBean.gene.HGNC)}">${node.name}</a></span></li>
                                                    </ul></li>
                                                </c:when>
                                                <c:otherwise>
                                                <li><span class="folder">QuExT</span><ul>
                                                        <li><span class="file"><a title="${node.shortname}: ${actionBean.gene.HGNC}" class="frame" value="${fn:replace(node.value,"#replaceme#",actionBean.gene.HGNC)}">${node.name}</a></span></li>
                                                    </ul></li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <c:choose>                                
                                <c:when test="${type.size == 0}">
                                    <li><span class="empty">${type.name}</span></li>
                                </c:when>
                                <c:otherwise>
                                    <li><span class="folder">${type.name}</span>
                                        <ul>
                                            <c:forEach var="node" items="${type.nodes}">
                                                <c:if test="${node.size == 0 && node.method != 'direct'}">
                                                    <li><span class="empty">${node.name}</span></li>
                                                </c:if>
                                                <c:if test="${node.size > 0 || node.method == 'direct'}">
                                                    <c:choose>
                                                        <c:when test="${node.method != 'direct'}">
                                                            <li><span class="folder">${node.name}</span>
                                                                <ul>
                                                                    <c:forEach var="leaf" items="${node.leafs}">
                                                                        <li><span class="file"><a title="${node.shortname}: ${leaf.name}" class="frame" value="${fn:replace(node.value, '#replaceme#', leaf.value)}" title="${leaf.name}">${node.shortname} ${leaf.name}</a></span></li>
                                                                    </c:forEach>
                                                                </ul>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li><span class="folder">${node.name}</span>
                                                                <ul>
                                                                    <li><span class="file"><a class="frame" title="${node.shortname}: ${actionBean.gene.HGNC}" value="${fn:replace(node.value,"#replaceme#",actionBean.gene.HGNC)}" >${node.shortname} ${actionBean.gene.HGNC}</a></span></li>
                                                                </ul>
                                                            </li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>                                                
                                            </c:forEach>
                                        </ul></li>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
            </ul>
        </div>
    </s:layout-component>
    <c:choose>
        <c:when test="${actionBean.addressUA != 'null'}">
            <s:layout-component name="frame">
                <iframe width="100%" height="100%" frameborder="none" id="frame" src="${actionBean.addressUA}"></iframe>
            </s:layout-component>
        </c:when>
        <c:otherwise>
            <s:layout-component name="frame">
                <iframe width="100%" height="100%" frameborder="none" id="frame" src="<c:url value="/content/${actionBean.gene.HGNC}" />"></iframe>
            </s:layout-component>
        </c:otherwise>
    </c:choose>
</s:layout-render>