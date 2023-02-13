<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>


<%@ attribute name="status" required="true" type="ua.vspelykh.salon.model.entity.AppointmentStatus" %>

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
    <c:when test="${status == 'RESERVED'}">
        <fmt:message key="status.reserved"/>
    </c:when>
    <c:when test="${status == 'SUCCESS'}">
        <fmt:message key="status.success"/>
    </c:when>
    <c:when test="${status == 'CANCELLED'}">
        <fmt:message key="status.cancelled"/>
    </c:when>
    <c:when test="${status == 'DIDNT_COME'}">
        <fmt:message key="status.didnt"/>
    </c:when>
</c:choose>
</body>
