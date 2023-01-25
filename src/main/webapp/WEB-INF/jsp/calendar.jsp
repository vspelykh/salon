<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/material_blue.css">

    <title>Calendar ${name.name} ${name.surname}</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <div class="content">
        <br>
        <div class="container text-left">
            <div class="row justify-content-center">
                <h2 class="mb-5 text-center">Calendar for ${user.name} ${user.surname}. Choose the day</h2>
                <div class="col-lg-3">
                    <c:choose>
                        <c:when test="${param.exc == 'y'}">
                            <h6 style="color: red">Time slot have already occupied or duration not allowed anymore</h6>
                        </c:when>
                    </c:choose>
                    <form onsubmit="return validateForm()" id="calendar"
                          name="calendar-form" action="${pageContext.request.contextPath}/salon">
                        <label>
                            <input hidden name="command" value="calendar">
                        </label>
                        <label>
                            <input hidden name="id" value="${user.id}">
                        </label>
                        <div class="form-group">
                            <label><input name="day" type="datetime-local" class="form-control"
                                          placeholder="${placeholder}">
                            </label>
                            <button onclick="document.getElementById('form-id').submit();">Submit</button>
                        </div>
                    </form>
                    <form action="${pageContext.request.contextPath}/salon">
                        <label>
                            <input hidden name="command" value="appointment">
                        </label>
                        <label>
                            <input hidden name="day" value="${placeholder}">
                        </label>
                        <input hidden name="id" value="${user.id}">
                        <br>
                        <c:forEach items="${slots}" var="item">
                            <input type="radio" class="btn-check" required name="time" id="${item}"
                                   value="${item}" autocomplete="off"/>
                            <label class="btn btn-secondary" for="${item}">${item}</label>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${slots != null}">
                                <button type="submit">Create an appointment</button>
                            </c:when>
                        </c:choose>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    flatpickr("#calendar, input[type=datetime-local]", {
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
            alert("Day must be filled out");
            return false;
        }
    }
</script>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>