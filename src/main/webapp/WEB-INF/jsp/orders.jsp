<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pt" uri="/WEB-INF/tld/customTags.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="admin.orders"/></title>
    <script type="text/javascript" src="/static/scripts.js"></script>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <form action="${pageContext.request.contextPath}/salon">
        <label>
            <input hidden name="command" value="orders">
        </label>
        <!--    sidebar-->
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav" style="margin-top: 190px; float: left">
                <nav class="sb-sidenav accordion sb-sidenav-light" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                            <!--                        accordion-->
                            <div class="accordion" id="accordionPanelsStayOpenExample">
                                <div class="accordion-item">
                                    <h2 class="accordion-header" id="panelsStayOpen-headingTwo">
                                        <button class="accordion-button collapsed" type="button"
                                                data-bs-toggle="collapse"
                                                data-bs-target="#panelsStayOpen-collapseTwo" aria-expanded="false"
                                                aria-controls="panelsStayOpen-collapseTwo">
                                            <fmt:message key="role.master"/>
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingTwo">
                                        <div class="accordion-body">
                                            <label class="form-check">
                                                <label>
                                                    <input onchange="this.form.submit()" name="id" type="radio"
                                                           value=""> <fmt:message key="orders.all"/>
                                                </label>
                                                <br>
                                                <c:forEach items="${masters}" var="item">
                                                    <label><input onchange="this.form.submit()"
                                                                  name="id"
                                                                  type="radio" value="${item.id}"
                                                        ${idChecked == item.id ? 'checked="checked"' : ''}>
                                                        <c:out value="${item.name} ${item.surname}"/>
                                                    </label>
                                                    <br>
                                                </c:forEach>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="accordion-item">
                                    <h2 class="accordion-header" id="panelsStayOpen-headingTwos">
                                        <button class="accordion-button collapsed" type="button"
                                                data-bs-toggle="collapse"
                                                data-bs-target="#panelsStayOpen-collapseTwos" aria-expanded="false"
                                                aria-controls="panelsStayOpen-collapseTwos">
                                            <fmt:message key="orders.status"/>
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseTwos" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingTwos">
                                        <div class="accordion-body">
                                            <label class="form-check">
                                                <label>
                                                    <input onchange="this.form.submit()" name="status" type="radio"
                                                           value=""> <fmt:message key="orders.all"/>
                                                </label>
                                                <br>
                                                <label><input onchange="this.form.submit()"
                                                              name="status"
                                                              type="radio" value="RESERVED"
                                                ${statusChecked == 'RESERVED' ? 'checked="checked"' : ''}>
                                                    <fmt:message key="status.reserved"/>                                                </label>
                                                <br>
                                                <label><input onchange="this.form.submit()"
                                                              name="status"
                                                              type="radio" value="SUCCESS"
                                                ${statusChecked == 'SUCCESS' ? 'checked="checked"' : ''}>
                                                    <fmt:message key="status.success"/>
                                                </label>
                                                <br>
                                                <label><input onchange="this.form.submit()"
                                                              name="status"
                                                              type="radio" value="CANCELLED"
                                                ${statusChecked == 'CANCELLED' ? 'checked="checked"' : ''}>
                                                    <fmt:message key="status.cancelled"/>
                                                </label>
                                                <br><label><input onchange="this.form.submit()"
                                                                  name="status"
                                                                  type="radio" value="DIDNT_COME"
                                            ${statusChecked == 'DIDNT_COME' ? 'checked="checked"' : ''}>
                                                <fmt:message key="status.didnt"/>
                                            </label>
                                                <br>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="accordion-item">
                                    <h2 class="accordion-header" id="panelsStayOpen-headingTwos1">
                                        <button class="accordion-button collapsed" type="button"
                                                data-bs-toggle="collapse"
                                                data-bs-target="#panelsStayOpen-collapseTwos1" aria-expanded="false"
                                                aria-controls="panelsStayOpen-collapseTwos1">
                                            <fmt:message key="orders.payment"/>
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseTwos1" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingTwos1">
                                        <div class="accordion-body">
                                            <label class="form-check">
                                                <label>
                                                    <input onchange="this.form.submit()" name="payment_status" type="radio"
                                                           value=""> <fmt:message key="orders.all"/>
                                                </label>
                                                <br>
                                                <label><input onchange="this.form.submit()"
                                                              name="payment_status"
                                                              type="radio" value="PAID_BY_CARD"
                                                ${payment_statusChecked == 'PAID_BY_CARD' ? 'checked="checked"' : ''}>
                                                    <fmt:message key="status.card"/>                                                </label>
                                                <br>
                                                <label><input onchange="this.form.submit()"
                                                              name="payment_status"
                                                              type="radio" value="PAID_IN_SALON"
                                                ${payment_statusChecked == 'PAID_IN_SALON' ? 'checked="checked"' : ''}>
                                                    <fmt:message key="status.salon"/>                                                </label>
                                                <br>
                                                <label><input onchange="this.form.submit()"
                                                              name="payment_status"
                                                              type="radio" value="NOT_PAID"
                                                ${payment_statusChecked == 'NOT_PAID' ? 'checked="checked"' : ''}>
                                                    <fmt:message key="status.not"/>                                                </label>
                                                <br><label><input onchange="this.form.submit()"
                                                                  name="payment_status"
                                                                  type="radio" value="RETURNED"
                                            ${payment_statusChecked == 'RETURNED' ? 'checked="checked"' : ''}>
                                                <fmt:message key="status.returned"/>                                            </label>
                                                <br>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="accordion-item">
                                    <h2 class="accordion-header" id="panelsStayOpen-headingTwoq">
                                        <button class="accordion-button collapsed" type="button"
                                                data-bs-toggle="collapse"
                                                data-bs-target="#panelsStayOpen-collapseTwoq" aria-expanded="false"
                                                aria-controls="panelsStayOpen-collapseTwoq">
                                            <fmt:message key="orders.date"/>
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseTwoq" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingTwoq">
                                        <div class="accordion-body">
                                            <label class="form-check">
                                                <br>
                                                <label>
                                                    <input type="date" name="dateFrom" value="dateFromChecked">
                                                </label>
                                                <br>
                                                <label>
                                                    <input type="date" name="daTo" value="dateToChecked">
                                                </label>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
            <%--            --%>
            <!--            table-->
            <div class="container-xl table-book-body">
                <div class="table-responsive">
                    <div class="table-wrapper">
                        <div class="table-title">
                            <div class="">
                                <div class="col-sm-8"><h2><b><fmt:message key="admin.orders"/></b></h2></div>
                                <%--    params--%>
                                <label for="size"><fmt:message key="master.size"/></label>
                                <select name="size" id="size" onchange="this.form.submit()">
                                    <c:forEach items="${sizes}" var="item">
                                        <option value="${item}" ${sizeChecked == item ? 'selected="selected"' : ''}>
                                                ${item}</option>
                                    </c:forEach>
                                </select>

                                <input type="submit" value="<fmt:message key="main.filter"/>">
                                <table id="mastersTable" class="table table-striped table-hover table-bordered">
                                    <thead>
                                    <tr>
                                        <th><b><fmt:message key="role.master"/></b<i></i></th>
                                        <th><b><fmt:message key="role.client"/></b<i></i></th>
                                        <th><b><fmt:message key="orders.date"/></b<i></i></th>
                                        <th><b><fmt:message key="appointment.price"/></b<i></i></th>
                                        <th><b><fmt:message key="orders.status"/></b></th>
                                        <th><b></b><i></i></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${appointments}" var="item">
                                        <tr>
                                            <td>${item.master}</td>
                                            <td>${item.client}</td>
                                            <td>
                                                    <fmt:parseDate value="${item.date}" pattern="yyyy-MM-dd'T'HH:mm"
                                                                   var="parsedDateTime" type="both"/>
                                                    <fmt:formatDate pattern="dd.MM.yyyy HH:mm"
                                                                    value="${parsedDateTime}"/>
                                            <td>${item.price}</td>
                                            <td>
                                                <tags:printStatus status="${item.status}"/>,
                                                <br>
                                                <tags:printPaymentStatus status="${item.paymentStatus}"/>
                                            </td>
                                            <td>
                                                <button type="button" style="width: 40px; height: 40px "
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#modal${item.id}">&#x270D;
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <tags:pagination pageChecked="${pageChecked}" pathStr="${pathStr}" pagesArray="${pagesArray}"
                             lastPage="${lastPage}"/>
        </div>
    </form>
    <c:forEach var="item" items="${appointments}">
    <tags:orderModal appointment="${item}"/>
    </c:forEach>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
