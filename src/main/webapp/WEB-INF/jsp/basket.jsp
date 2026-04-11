<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:url var="show"    value="/basket/" />
<c:url var="addDVD"  value="/basket/add/DVD" />
<c:url var="addPolo" value="/basket/add/Polo" />
<c:url var="reset"   value="/basket/reset" />

<h1>Votre panier (${basket.products.size()} produits)</h1>

<c:if test="${message != null}">
    <div class="alert alert-success" role="alert">
        <c:out value="${message}" />
    </div>
</c:if>

<ul><c:forEach items="${basket.products}" var="p">
    <li><c:out value="${p}"/></li>
</c:forEach></ul>

<p>
    <a class="btn btn-primary mx-2" href="${show}">Show</a>
    <a class="btn btn-primary mx-2" href="${addDVD}">Buy Dvd</a>
    <a class="btn btn-primary mx-2" href="${addPolo}">Buy Polo</a>
    <a class="btn btn-primary mx-2" href="${reset}">Reset</a>
</p>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>