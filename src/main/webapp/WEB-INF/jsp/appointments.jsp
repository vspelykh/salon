<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Appointments</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">

    <c:forEach var="item" items="${schedule}">
        ${item.start} - ${item.end}, ${item.info}
        <br>
    </c:forEach>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
