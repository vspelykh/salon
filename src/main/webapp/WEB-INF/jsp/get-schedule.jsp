<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="df" uri="/WEB-INF/tld/customTag.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
    <title>Appointments</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <link rel="stylesheet" href="/static/styles.css">
    <script type="text/javascript" src="/static/scripts.js"></script>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container py-7">
    <h2 class="text-uppercase text-letter-spacing-xs my-0 text-primary font-weight-bold">
        Schedule
    </h2>
    <p class="text-sm text-dark mt-0 mb-5">There's time and place for everything.</p>
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
                                    : ${scheduleItem.info}</p>
                            </li>
                            <c:choose>
                                <c:when test="${scheduleItem.appointment != null}">
                                    <form action="${pageContext.request.contextPath}/salon" method="post">
                                        <input hidden name="command" value="edit-appointment">
                                        <input hidden name="id" value="${param.id}">
                                        <input hidden name="days" value="${param.days}">
                                        <input hidden name="appointment_id" value="${scheduleItem.appointment.id}">
                                        <select name="status" onchange="confirmBeforeSubmit(this.form)"
                                                class="form-select">
                                            <c:forEach items="${status}" var="s">
                                                <option ${scheduleItem.appointment.status == s ? 'selected disabled' : ''}
                                                        value="${s}">${s}</option>
                                            </c:forEach>
                                        </select>
                                        <select name="new_slot" onchange="confirmBeforeSubmit(this.form)"
                                                class="form-select">
                                            <option selected disabled>Possible slots to change time today</option>
                                            <c:forEach items="${free_slots_map.get(scheduleItem.appointment.id)}"
                                                       var="slot">
                                                <option value="${slot}">${slot}</option>
                                            </c:forEach>
                                        </select>
                                    </form>
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
