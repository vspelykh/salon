<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<%@ attribute name="appointment" required="true" type="ua.vspelykh.salon.dto.AppointmentDto" %>

<head>
    <title>Change status of appointment</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <script type="text/javascript" src="/static/scripts.js"></script>
</head>
<body>

<div class="modal fade" id="modal${appointment.id}" tabindex="-1"
     aria-labelledby="modalLabel${appointment.id}"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="modalLabel${appointment.id}"><fmt:message key="appointment.name"/></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <tags:changeStatus id="${param.id}"
                                   appointment="${appointment}"
                                   isAdmin="true" redirect="redirect"
                                   status="${appointment.status}"/>

                        <tags:acceptPayment id="${param.id}" redirect="redirect"
                                            appointment_id="${appointment.id}"
                                            status="${appointment.paymentStatus}"/>

                <c:choose>
                    <c:when test="${appointment.status == 'RESERVED'}">
                        <form action="${pageContext.request.contextPath}/salon"
                              method="post">
                            <input hidden name="command" value="edit-appointment">
                            <input hidden name="id" value="${param.id}">
                            <input hidden name="appointment_id"
                                   value="${appointment.id}">
                            <input hidden name="redirect" value="redirect">
<%--                            <select name="new_slot"--%>
<%--                                    onchange="if(!confirm('<fmt:message--%>
<%--                                            key="submit.slot"/>')){return false;}"--%>
<%--                                    class="form-select">--%>
<%--                                <option selected disabled><fmt:message--%>
<%--                                        key="schedule.possible"/></option>--%>
<%--                                <c:forEach--%>
<%--                                        items="${free_slots_map.get(scheduleItem.appointment.id)}"--%>
<%--                                        var="slot">--%>
<%--                                    <option value="${slot}">${slot}</option>--%>
<%--                                </c:forEach>--%>
<%--                            </select>--%>
                        </form>
                    </c:when>
                </c:choose>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary"
                        data-bs-dismiss="modal"><fmt:message
                        key="main.cancel"/></button>
                <button type="submit" class="btn btn-primary">
                    <fmt:message key="main.submit"/>
                </button>
            </div>
        </div>
    </div>
</div>