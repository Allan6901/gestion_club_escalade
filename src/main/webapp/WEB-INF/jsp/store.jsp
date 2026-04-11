<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:url var="open"  value="/store/open" />
<c:url var="close" value="/store/close" />
<c:url var="show"  value="/store/" />

<h1>Store state : <c:out value="${store.state}" default="unknown" /></p>

<p>
    <a class="btn btn-primary mx-2" href="${show}">Show</a>
    <a class="btn btn-primary mx-2" href="${open}">Open</a>
    <a class="btn btn-primary mx-2" href="${close}">Close</a>
</p>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>