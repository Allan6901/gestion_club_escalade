<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:url var="function" value="/functions.js" />

<script src="${function}"></script>
<h1>Simple stack calculator (rest application)</h1>
<p>
    <button class="mx-2 btn btn-primary" onclick="show();">Les valeurs</button>
    <input class="mx-2" id="input" size="10" />
    <button class="mx-2 btn btn-primary" onclick="put();">Ajouter</button>
    <button class="mx-2 btn btn-primary" onclick="add();">+</button>
    <button class="mx-2 btn btn-primary" onclick="sub();">-</button>
    <span class="mx-2" style="color: blue;" id="message"></span>
</p>
<p>La pile : <span id="numbers"></span></p>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>