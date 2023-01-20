<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Pricing</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <ul class="list-group list-group-flush">
        <c:forEach items="${services}" var="item">
            <li class="list-group-item">${item.service} <b>${item.price}/
                <fmt:formatNumber value="${item.price * 1.15}" maxFractionDigits="0"/>/
                <fmt:formatNumber value="${item.price * 1.3}" maxFractionDigits="0"/></b>
            </li>
        </c:forEach>
    </ul>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
