<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Manage schedule of masters</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/material_blue.css">
    <script src="static/scripts.js"></script>
    <style>
        .cool-date {
            background-color: #ac2bac;
            color: #eeeeee;
        }
    </style>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <div class="content">
        <br>
        <div class="container text-left">
            <div class="row justify-content-center">
                <h2 class="mb-5 text-center">Schedule for ${user.name} ${user.surname}.</h2>
                <div class="col-lg-3">
                    <form onsubmit="return validateForm()" id="schedule" method="post"
                          name="schedule-form" action="${pageContext.request.contextPath}/salon">
                        <label>
                            <input hidden name="command" value="edit-schedule">
                        </label>
                        <label>
                            <input hidden name="id" value="${user.id}">
                        </label>
                        <h6 style="color: #ac2bac">Working days</h6>
                        <label for="action-select">Action
                            <select name="action" id="action-select" required onclick="selectHelper()">
                                <option disabled selected value> -- select an action --</option>
                                <option value="schedule">schedule</option>
                                <option value="save">save</option>
                                <option value="delete">delete</option>
                            </select>
                        </label>
                        <p></p>
                        <div class="form-group">
                            <label for="selector-date"><input name="days" type="datetime-local" id="selector-date"
                                                              class="form-control" placeholder="Select days">
                            </label>
                            <button onclick="document.getElementById('form-id').submit();">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script>

        var coolDates = [${days}]

        flatpickr("input[type=datetime-local]", {
            inline: true,
            mode: "multiple",
            dateFormat: "d-m-Y",
            enableTime: false,
            minDate: "today",
            maxDate: new Date().fp_incr(90),
            onDayCreate: function (dObj, dStr, fp, dayElem) {
                let s = "";
                if ((+dayElem.dateObj.getDate()) < 10) {
                    s += '0' + (+dayElem.dateObj.getDate());
                } else {
                    s += (+dayElem.dateObj.getDate());
                }
                if ((+dayElem.dateObj.getMonth() + 1) < 10) {
                    s += "-0" + (+dayElem.dateObj.getMonth() + 1) + "-" + (+dayElem.dateObj.getFullYear());
                } else {
                    s += "-" + (+dayElem.dateObj.getMonth() + 1) + "-" + (+dayElem.dateObj.getFullYear());
                }
                if (coolDates.indexOf(s) !== -1) {
                    dayElem.className += " cool-date";
                }
            }
        });

        function validateForm() {
            var days = document.forms["schedule"]["days"].value;
            if (days === "") {
                alert("Day must be filled out");
                return false;
            }
        }
    </script>

</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
