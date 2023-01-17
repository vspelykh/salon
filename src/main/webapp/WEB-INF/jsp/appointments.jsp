<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Appointments</title>
    <fmt:setLocale value="${cookie['lang'].value}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">

</div>
<jsp:include page="fragments/footer.jsp"/>
<c:forEach items="${appointments}" var="item">
    ${item.clientId}
</c:forEach>
</body>
</html>
