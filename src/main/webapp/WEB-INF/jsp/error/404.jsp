<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>404</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp"/>
<div class="container">
    <div class="d-flex align-items-center justify-content-center vh-100">
        <div class="text-center">
            <c:choose>
                <c:when test="${sessionScope.message == null}">
                    <h1 class="display-1 fw-bold">404</h1>
                    <p class="fs-3"><span class="text-danger"><fmt:message key="404.oops"/></span> <fmt:message key="404.page"/> </p>
                    <p class="lead">
                        <fmt:message key="404.exist"/>
                    </p>
                </c:when>
            </c:choose>
            <c:choose>
                <c:when test="${sessionScope.message != null}">
                    <h1><fmt:message key="${sessionScope.message}"/></h1>
                    ${sessionScope.message = null}
                </c:when>
            </c:choose>
            <a href="${pageContext.request.contextPath}/salon" class="btn btn-primary"><fmt:message key="404.home"/></a>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp"/>

</body>
</html>
