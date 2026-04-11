<%@ include file="/WEB-INF/jsp/header.jsp"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:url var="courseList" value="/course" />
<c:url var="courseNew" value="/course/new" />
<c:url var="courseFind" value="/course/find" />

<h1>Courses</h1>
<form action="${courseFind}" method="post">
<sec:csrfInput />
	<div class="card bg-primary">
		<div class="card-body">
			<a class="btn btn-info mx-3" href="${courseNew}">New course</a>
			<input name="name" size="10" class="mx-3" />
			<input class="btn btn-info mx-3" type="submit" value="Find" />
		</div>
	</div>
</form>
<table class="table table-hover">
	<c:forEach items="${courses}" var="course">
		<tr>
			<td>${course.id}</td>
			<td><c:out value="${course.name}" /></td>
		</tr>
	</c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
