<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="df" uri="/WEB-INF/tld/customTag.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400&display=swap" rel="stylesheet">

    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script src="https://npmcdn.com/flatpickr/dist/l10n/uk.js"></script>
    <link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/material_blue.css">

    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="appointment.postponement"/></title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <div class="content">
        <br>
        <div class="container text-left">
            <div class="row justify-content-center">
                <h2 class="mb-5 text-center"><fmt:message key="appointment.postponement"/>.
                    <fmt:message key="old.date"/> <df:dateTimeParser locale="${sessionScope.locale}"
                                                                     date="${appointment.date}"/></h2>
                <div class="col-lg-3">
                    <c:choose>
                        <c:when test="${sessionScope.error != null}">
                            <h6 style="color: red"><fmt:message key="calendar.occupied"/></h6>
                            ${sessionScope.error = null}
                        </c:when>
                    </c:choose>
                    <form onsubmit="return validateForm()" id="postpone"
                          name="postpone-form" action="${pageContext.request.contextPath}/salon">
                        <label>
                            <input hidden name="command" value="postpone-form">
                        </label>
                        <label>
                            <input hidden name="id" value="${param.id}">
                        </label>
                        <div class="form-group">
                            <label><input name="day" type="datetime-local" class="form-control"
                                          onchange="this.form.submit()"
                                          placeholder="${placeholder}">
                            </label>
                            <%--                            <button onclick="document.getElementById('form-id').submit();"><fmt:message--%>
                            <%--                                    key="main.submit"/></button>--%>
                        </div>
                    </form>
                    <form action="${pageContext.request.contextPath}/salon" method="post" id="postponement-form">
                        <label>
                            <input hidden name="command" value="postponement">
                        </label>
                        <label>
                            <input hidden name="day" value="${placeholder}">
                        </label>
                        <input hidden name="id" value="${param.id}">
                        <br>
                        <c:forEach items="${slots}" var="item">
                            <input type="radio" class="btn-check" required name="time" id="${item}"
                                   onchange="confirmPostponement(this.value)"
                                   value="${item}" autocomplete="off"/>
                            <label class="btn btn-secondary" for="${item}">${item}</label>
                        </c:forEach>
                    </form>

                    <script>
                        function confirmPostponement(selectedValue) {
                            if (confirm('<fmt:message key="submit.postpone"/>')) {
                                document.getElementById("postponement-form").submit();
                            } else {
                                // Uncheck the selected radio button
                                document.getElementById(selectedValue).checked = false;
                            }
                        }
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    flatpickr("input[type=datetime-local]", {
        locale: "${sessionScope.lang == "en" ? "en" : "uk"}",
        dateFormat: "d-m-Y",
        enableTime: false,
        minDate: "today",
        maxDate: new Date().fp_incr(90),
        "enable": [
            function (date) {
                <c:forEach items="${days}" var="item">
                if (date.getMonth() + 1 === ${item.getDate().getMonth().getValue()}
                    && date.getDate() === ${item.getDate().getDayOfMonth()}
                    && date.getFullYear() === ${item.getDate().getYear()}) {
                    return true;
                }
                </c:forEach>
                return false;
            }
        ],
    });

    function validateForm() {
        var x = document.forms["calendar"]["day"].value;
        if (x == "") {
            alert("<fmt:message key="schedule.validate"/>");
            return false;
        }
    }
</script>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>