<%@ include file="/WEB-INF/jsp/header.jsp"%>


<h1>Products</h1>

<table class="table table-hover">
    <c:forEach items="${products}" var="prod">
        <c:url var="edit" value="/product/edit">
            <c:param name="id" value="${prod.number}" />
        </c:url>
        <tr>
            <td><a href="${edit}"><c:out value="${prod.name}" /></a></td>
            <td><i>$<c:out value="${prod.price}" /></i></td>
        </tr>
    </c:forEach>
</table>

<c:url var="create" value="/product/edit" />
<p>
    <a class="btn btn-info" href="${create}">Create new product</a>
</p>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>