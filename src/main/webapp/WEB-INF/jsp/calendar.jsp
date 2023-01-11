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

    <title>Calendar #1</title>
    <fmt:setLocale value="${cookie['lang'].value}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <div class="content">

        <div class="container text-left">
            <div class="row justify-content-center">
                <div class="col-lg-3">
                    <h2 class="mb-5 text-center">Calendar #1 (Date Picker)</h2>
                    <form action="#">
                        <div class="form-group">
                            <label for="chooseDay"></label><input id="chooseDay" type="datetime-local"
                                                                  class="form-control" placeholder="Pick A Date">
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>

    <script>
        flatpickr("input[type=datetime-local]", {
            dateFormat: "Y-d-m",
            enableTime: false,
            minDate: "today",
            maxDate: new Date().fp_incr(90),
            "enable": [
                function (date) {
                    var d = (date.getMonth() + 1) + '-' + date.getDate() + '-' + date.getFullYear();
                    <c:forEach items="${days}" var="item">

                    if (date.getMonth() + 1 === ${item.getMonth().getValue()}
                        && date.getDate() === ${item.getDayOfMonth()}
                        && date.getFullYear() === ${item.getYear()}) {
                        return true;
                    }
                    </c:forEach>
                    return false;
                }
            ],
        });
    </script>
</div>
<jsp:include page="fragments/footer.jsp"/>

</body>
</html>