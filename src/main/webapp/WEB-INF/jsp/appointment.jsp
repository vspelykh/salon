<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="appointment.name"/></title>
    <script src="static/scripts.js"></script>

</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <section class="order-form my-4 mx-4">
        <div class="container-book order-container basic-style pt-4">
            <div class="left-column">
                <div class="row">
                    <div class="col-12">
                        <h1><fmt:message key="appointment.create"/></h1>
                        <span><b><fmt:message key="appointment.date"/></b> ${day}, <b><fmt:message
                                key="appointment.time"/></b> ${time}, <fmt:message
                                key="appointment.master"/> ${master.name} ${master.surname}.</span>
                        <br>
                        <c:choose>
                            <c:when test="${allowedTime != -1}">
                                <span><fmt:message key="appointment.allowed"/> <b>${allowedTime} <fmt:message
                                        key="appointment.min"/></b></span>
                            </c:when>
                        </c:choose>
                        <span><fmt:message key="appointment.please"/></span>
                        <hr class="mt-1">
                    </div>

                    <div class="col-12">
                        <form name="form-order" action="${pageContext.request.contextPath}/salon" method="post"
                              onchange="appointmentAction()">
                            <label>
                                <input hidden name="command" value="create-appointment">
                            </label>
                            <label>
                                <input hidden name="master_id" value="${master.id}">
                            </label>
                            <label>
                                <input hidden name="day" value="${day}">
                            </label>
                            <label>
                                <input hidden name="time" value="${time}">
                            </label>
                            <div class="row">
                                <div class="col-sm-6 text-black">
                                    <div class="card" style="border-radius: 15px;">
                                        <div class="card-body p-5">
                                            <c:forEach items="${services}" var="item">
                                                <label class="btn btn-secondary">
                                                    <input name="services" id="${item.id}" type="checkbox"
                                                           value="${item.service.service}|${item.continuance}|${item.service.price}|${item.id}"
                                                        ${servicesChecked.contains(item.id) ? 'checked="checked"' : ''}>
                                                    <c:out value="${item.service.service} (${item.continuance} min)"/>
                                                </label>
                                                <c:choose>
                                                    <c:when test="${item.id % 2 == 0}">
                                                        <br>
                                                    </c:when>
                                                </c:choose>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6 px-0 d-none d-sm-block">
                                    <p>
                                    </p>
                                </div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-12" id="submit-button">

                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<script>
    function countTime(time) {
        console.log(time);
    }

    function appointmentAction() {
        const para = document.querySelector('p');
        const para2 = document.querySelector('#submit-button');
        let time = 0;
        let price = 0;
        var res = " ";
        let j = ${first};
        let until = ${first} + ${size};
        for (let i = j; i < until; i++) {
            if (document.getElementById(i.toString()).checked === true) {
                var pl1 = document.getElementById(i.toString()).value;
                res += pl1.split("|")[0] + "<br>";
                time += Number(pl1.split("|")[1]);
                price += Number(pl1.split("|")[2]);
            }
        }
        price *= ${userLevel.level.index};
        res += '<b><fmt:message key="appointment.total"/> ' + time + ' <fmt:message key="appointment.min"/>';
        res += ' <fmt:message key="appointment.price"/> ' + Math.floor(price) + " <fmt:message key="appointment.grn"/></b>";
        if (time <= ${allowedTime} && time !== 0) {
            para2.innerHTML = '<button type="submit" name="payment" value="PAID_BY_CARD" class="btn btn-info d-block mx-auto btn-submit">' +
                '<fmt:message key="appointment.pay"/>' + '</button>' +
                '<button type="submit" name="payment" value="NOT_PAID" class="btn btn-dark d-block mx-auto btn-submit">' +
                '<fmt:message key="appointment.order"/>' + '</button>'
        } else {
            para2.innerHTML = '<button disabled type="submit" class="btn btn-dark d-block mx-auto btn-submit">' +
                '<fmt:message key="master.services"/>' + '</button>'
        }
        return para.innerHTML = res;
    }
</script>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
