<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Appointment</title>
    <fmt:setLocale value="${cookie['lang'].value}"/>
    <fmt:setBundle basename="localization.messages"/>
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
                        <h1>Appointment creating</h1>
                        <span><b>Date:</b> ${day}, <b>time</b> : ${time}, master: ${master.name} ${master.surname}.</span>
                        <br>
                        <c:choose>
                            <c:when test="${allowedTime != -1}">
                                <span>For this time slot is allowed <b>${allowedTime} minutes.</b></span>
                            </c:when>
                        </c:choose>
                        <span>Please, choose another slot or date, if you need more time.</span>
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
        res += '<b>Total time: ' + time + ' min.';
        res += ' Price: ' + Math.floor(price) + " grn</b>";
        if (time <= ${allowedTime} && time !== 0) {
            para2.innerHTML = '<button type="submit" class="btn btn-dark d-block mx-auto btn-submit">' +
                'Submit' + '</button>'
        } else {
            para2.innerHTML = '<button disabled type="submit" class="btn btn-dark d-block mx-auto btn-submit">' +
                'Submit' + '</button>'
        }
        return para.innerHTML = res;
    }
</script>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
