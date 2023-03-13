<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>


<%@ attribute name="id" required="true" %>
<%@ attribute name="days" required="false" %>
<%@ attribute name="redirect" required="false" %>
<%@ attribute name="appointment" required="true" type="ua.vspelykh.salon.model.dto.AppointmentDto" %>
<%@ attribute name="status" required="true" %>
<%@ attribute name="date" required="true" type="java.time.LocalDateTime" %>
<%@ attribute name="isAdmin" required="true" type="java.lang.Boolean" %>
<c:set var="now" value="<%=java.time.LocalDate.now()%>"/>

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
        <form onsubmit="if(!confirm('<fmt:message key="submit.sure"/>')){return false;}"
              action="${pageContext.request.contextPath}/salon"
              method="post">
            <input hidden name="command" value="edit-appointment">
            <input hidden name="id" value="${id}">
            <input hidden name="appointment_id" value="${appointment.id}">
            <input hidden name="days" value="${days}">
            <input hidden name="redirect" value="${redirect}">
            <c:choose>
                <c:when test="${!now.isBefore(date.toLocalDate())}">
                    <button type="submit" name="status" value="SUCCESS" class="btn btn-success">
                        <fmt:message key="change.success"/>
                    </button>
                    <button type="submit" name="status" value="DIDNT_COME" class="btn btn-warning">
                        <fmt:message key="change.absent"/>
                    </button>
                </c:when>
            </c:choose>

            <c:choose>
                <c:when test="${isAdmin}">
                    <button type="submit" name="status" value="CANCELLED" class="btn btn-danger">
                        <fmt:message key="change.cancel"/>
                    </button>
                </c:when>
            </c:choose>
        </form>
        <c:choose>
            <c:when test="${isAdmin}">
                <form action="${pageContext.request.contextPath}/salon"
                      method="get">
                    <input hidden name="command" value="postpone-form">
                    <input hidden name="id" value="${appointment.id}">
<%--                    <input hidden name="days" value="${days}">--%>
<%--                    <input hidden name="appointment_id" value="${appointment_id}">--%>
                    <button type="submit" class="btn btn-outline-black">
                        <fmt:message key="change.postponement"/>
                    </button>
                </form>
            </c:when>
        </c:choose>
    </c:when>
</c:choose>
</body>