<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="profile.admin"/></title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
        <li><a href="${pageContext.request.contextPath}/salon?command=roles" class="nav-link px-2 text-primary">
            <fmt:message key="admin.roles"/>
        </a>
        </li>
        <li><a href="${pageContext.request.contextPath}/salon?command=orders" class="nav-link px-2 text-primary">
            <fmt:message key="admin.orders"/>
        </a>
        </li>
        <li><a href="${pageContext.request.contextPath}/salon?command=masters" class="nav-link px-2 text-primary">
            <fmt:message key="admin.manage"/>
            </a>
        </li>
        <li><a href="${pageContext.request.contextPath}/salon?command=consultations" class="nav-link px-2 text-primary">
            <fmt:message key="admin.consultations"/>
            </a>
        </li>
        <li><a href="${pageContext.request.contextPath}/salon?command=invitation"
               class="nav-link px-2 text-primary"><fmt:message key="admin.invite"/></a>
        </li>
    </ul>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
