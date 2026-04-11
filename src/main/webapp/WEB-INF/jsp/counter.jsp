<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h1>Counter is <c:out value="${counter.value}" default="None" /></h1>

<c:url var="init" value="/counter/init" />
<c:url var="inc" value="/counter/inc" />
<c:url var="show" value="/counter" />

<p>
    <a class="btn btn-primary mx-2" href="${show}">Show</a>
    <a class="btn btn-primary mx-2" href="${init}">Init</a>
    <a class="btn btn-primary mx-2" href="${inc}">Increment</a>
</p>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>