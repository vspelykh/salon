<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="schedule.schedule"/></title>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/material_blue.css">
    <script src="https://npmcdn.com/flatpickr/dist/l10n/uk.js"></script>
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
                <h2 class="mb-5 text-center"><fmt:message key="schedule.for"/> ${user.name} ${user.surname}.</h2>
                <div class="col-lg-3">
                    <!-- Button trigger modal -->
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
                        <fmt:message key="master.edit"/>
                    </button>
                    <!-- Modal -->
                    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="exampleModalLabel"><fmt:message
                                            key="master.about"/></h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <form id="modal" method="post" action="${pageContext.request.contextPath}/salon">
                                        <input hidden name="command" value="edit-master">
                                        <input hidden name="id" value="${user.id}">
                                        <div class="mb-3">
                                            <label>
                                                <fmt:message key="master.level"/>
                                                <select required name="level">
                                                    <option ${userLevel.level == 'TOP' ? 'selected' : ''}
                                                            value="TOP">TOP
                                                    </option>
                                                    <option ${userLevel.level == 'PRO' ? 'selected' : ''}
                                                            value="PRO">PRO
                                                    </option>
                                                    <option ${userLevel.level == 'YOUNG' ? 'selected' : ''}
                                                            value="YOUNG">YOUNG
                                                    </option>
                                                </select>
                                            </label>
                                        </div>
                                        <fmt:message key="master.about"/>
                                        <div class="mb-3">
                                            <label>
                                            <textarea required rows="4" cols="55" name="about">${userLevel.about}
                                            </textarea>
                                            </label>
                                        </div>
                                        <div class="mb-3">
                                            <label>
                                            <textarea required rows="4" cols="55" name="about_ua">${userLevel.aboutUa}
                                            </textarea>
                                            </label>
                                        </div>
                                        <div class="mb-3">
                                            <label>
                                                <select required name="active">
                                                    <option ${userLevel.active == 'true' ? 'selected' : ''}
                                                            value="true"><fmt:message key="master.active"/>
                                                    </option>
                                                    <option ${userLevel.active == 'false' ? 'selected' : ''}
                                                            value="false"><fmt:message key="master.fired"/>
                                                    </option>
                                                </select>
                                            </label>
                                        </div>
                                    </form>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                                            key="main.cancel"/></button>
                                    <button form="modal" type="submit" class="btn btn-primary">
                                        <fmt:message key="main.submit"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <form onsubmit="return validateForm()" id="schedule" method="post"
                          name="schedule-form" action="${pageContext.request.contextPath}/salon">
                        <label>
                            <input hidden name="command" value="edit-schedule">
                        </label>
                        <label>
                            <input hidden name="id" value="${user.id}">
                        </label>
                        <h6 style="color: #ac2bac"><fmt:message key="schedule.days"/></h6>
                        <label for="action-select"><fmt:message key="schedule.action"/>
                            <input hidden name="command" value="schedule">
                            <select name="action" id="action-select" required onclick="selectHelper()">
                                <option disabled selected value><fmt:message key="schedule.select"/></option>
                                <option value="save"><fmt:message key="schedule.save"/></option>
                                <option value="delete"><fmt:message key="schedule.delete"/></option>
                            </select>
                        </label>
                        <p></p>
                        <c:choose>
                            <c:when test="${sessionScope.message != null}">
                                <fmt:message key="schedule.remove"/> ${sessionScope.removed_days}
                                ${sessionScope.message = null}
                                ${sessionScope.removed_days = null}
                            </c:when>
                        </c:choose>
                        <div class="form-group">
                            <label for="selector-date"><input name="days" type="datetime-local" id="selector-date"
                                                              class="form-control"
                                                              placeholder="<fmt:message key="select.days"/>">
                            </label>
                            <button onclick="document.getElementById('form-id').submit();"><fmt:message
                                    key="main.submit"/></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script>

        var coolDates = [${days}]

        flatpickr("input[type=datetime-local]", {
            locale: "${sessionScope.lang == "en" ? "en" : "uk"}",
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
                alert("<fmt:message key="schedule.validate"/>");
                return false;
            }
        }
    </script>

</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
