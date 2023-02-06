<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="df" uri="/WEB-INF/tld/customTag.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="schedule.schedule"/></title>
    <link rel="stylesheet" href="/static/styles.css">
    <script type="text/javascript" src="/static/scripts.js"></script>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container py-7">
    <h2 class="text-uppercase text-letter-spacing-xs my-0 text-primary font-weight-bold">
        <fmt:message key="schedule.schedule"/>
    </h2>
    <p class="text-sm text-dark mt-0 mb-5">${user.name} ${user.surname}.</p>
    <!-- Days -->
    <div class="row">
        <c:forEach var="list" items="${schedule}">
            <div class="col-lg-4 mb-3">
                <div class="card border-primary mb-3" style="max-width: 18rem;">
                    <h4 class="mt-0 mb-3 text-dark op-8 font-weight-bold">
                        <df:dateParser locale="${sessionScope.lang}" date="${list.key}"/>
                    </h4>
                    <c:forEach var="scheduleItem" items="${list.value}">
                        <ul ${scheduleItem.info == 'Free slot' ? 'class="list-timeline list-timeline-blue"' :
                                'class="list-timeline list-timeline-orange"'}>
                            <li class="list-timeline-item p-0 pb-3 pb-lg-4 d-flex flex-wrap flex-column">
                                <p class="my-0 text-dark flex-fw text-sm ">
                                    <span class="text-inverse op-8">${scheduleItem.start} - ${scheduleItem.end}</span>
                                    : <c:choose>
                                    <c:when test="${scheduleItem.info == 'Free slot'}">
                                        <fmt:message key="schedule.free"/>
                                    </c:when>
                                    <c:when test="${scheduleItem.info == 'weekend'}">
                                        <fmt:message key="schedule.weekend"/>
                                    </c:when>
                                    <c:when test="${scheduleItem.info != 'Free slot'}">
                                        ${scheduleItem.info}
                                        <button type="button" style="width: 40px; height: 40px " data-bs-toggle="modal"
                                                data-bs-target="#exampleModal">&#x270D;
                                        </button>
                                    </c:when>
                                </c:choose></p>
                            </li>
                            <c:choose>
                                <c:when test="${scheduleItem.appointment != null}">
                                    <div class="modal fade" id="exampleModal" tabindex="-1"
                                         aria-labelledby="exampleModalLabel"
                                         aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" id="exampleModalLabel"><fmt:message
                                                            key="appointment.name"/></h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <tags:changeStatus id="${param.id}" days="${param.days}"
                                                                       appointment_id="${scheduleItem.appointment.id}"
                                                                       isAdmin="${isAdmin}"
                                                                       status="${scheduleItem.appointment.status}"/>
                                                    <c:choose>
                                                        <c:when test="${isAdmin}">
                                                            <tags:acceptPayment id="${param.id}" days="${param.days}"
                                                                                appointment_id="${scheduleItem.appointment.id}"
                                                                                status="${scheduleItem.appointment.paymentStatus}"/>
                                                        </c:when>
                                                    </c:choose>

                                                    <c:choose>
                                                        <c:when test="${isAdmin && scheduleItem.appointment.status == 'RESERVED'}">
                                                            <form action="${pageContext.request.contextPath}/salon"
                                                                  method="post">
                                                                <input hidden name="command" value="edit-appointment">
                                                                <input hidden name="id" value="${param.id}">
                                                                <input hidden name="days" value="${param.days}">
                                                                <input hidden name="appointment_id"
                                                                       value="${scheduleItem.appointment.id}">
                                                                <select name="new_slot"
                                                                        onchange="if(!confirm('<fmt:message
                                                                                key="submit.slot"/>')){return false;}"
                                                                        class="form-select">
                                                                    <option selected disabled><fmt:message
                                                                            key="schedule.possible"/></option>
                                                                    <c:forEach
                                                                            items="${free_slots_map.get(scheduleItem.appointment.id)}"
                                                                            var="slot">
                                                                        <option value="${slot}">${slot}</option>
                                                                    </c:forEach>
                                                                </select>
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
                                </c:when>
                            </c:choose>
                        </ul>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
