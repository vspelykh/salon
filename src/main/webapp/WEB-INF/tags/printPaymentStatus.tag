<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>


<%@ attribute name="status" required="true" type="ua.vspelykh.salon.model.entity.PaymentStatus" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="localization.messages"/>

<head>
    <title>Change status of appointment</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <script type="text/javascript" src="/static/scripts.js"></script>
</head>
<body>
<c:choose>
    <c:when test="${status == 'PAID_BY_CARD'}">
        <fmt:message key="status.card"/>
    </c:when>
    <c:when test="${status == 'PAID_IN_SALON'}">
        <fmt:message key="status.salon"/>
    </c:when>
    <c:when test="${status == 'NOT_PAID'}">
        <fmt:message key="status.not"/>
    </c:when>
    <c:when test="${status == 'RETURNED'}">
        <fmt:message key="status.returned"/>
    </c:when>
</c:choose>
</body>

