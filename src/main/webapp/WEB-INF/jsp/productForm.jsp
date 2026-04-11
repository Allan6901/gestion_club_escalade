<%@ include file="/WEB-INF/jsp/header.jsp"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1>Edit Product</h1>

<div class="card bg-light">
    <div class="card-body">
        <form:form method="POST" modelAttribute="product">

            <form:errors path="*" cssClass="alert alert-danger" element="div" />

            <div class="form-group my-1">
                <label for="name">Name:</label>
                <form:input class="form-control" path="name" />
                <form:errors path="name" cssClass="alert alert-warning"
                    element="div" />
            </div>
            <div class="form-group my-1">
                <label for="description">Description:</label>
                <form:textarea class="form-control" path="description" rows="4" />
                <form:errors path="description" cssClass="alert alert-warning"
                    element="div" />
            </div>
            <div class="form-group my-1">
                <label for="price">Price:</label>
                <form:input path="price" class="form-control" />
                <form:errors path="price" cssClass="alert alert-warning"
                    element="div" />
            </div>
            <div class="form-group my-1">
                <button type="submit" class="btn btn-info">Submit</button>
            </div>
            <div class="form-group my-1">
                <label for="type">Type:</label>
                <form:select path="type" multiple="false" class="form-control">
                    <form:option value="" label="--- Select ---" />
                    <form:options items="${productTypes}" />
                </form:select>
                <form:errors path="type" cssClass="alert alert-warning"
                    element="div" />
            </div>
            <div class="form-group my-1">
                <label for="code">Code:</label>
                <form:input path="code.base" class="form-control"/>
                <form:input path="code.number" class="form-control"/>
                <form:errors path="code" cssClass="alert alert-warning"
                    element="div" />
            </div>
        </form:form>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>