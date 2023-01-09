<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Administration</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
        <li><a href="${pageContext.request.contextPath}/salon?command=roles" class="nav-link px-2 text-primary">Change
            roles</a>
        </li>
        <li><a href="${pageContext.request.contextPath}/salon?command=manage" class="nav-link px-2 text-primary">Manage
            masters</a>
        </li>
    </ul>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
