<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Orders</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
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
                                            Master
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingTwo">
                                        <div class="accordion-body">
                                            <label class="form-check">
                                                <label>
                                                <input onchange="this.form.submit()" name="id" type="radio"
                                                value=""> all
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
                                            Status
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseTwos" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingTwos">
                                        <div class="accordion-body">
                                            <label class="form-check">
                                                <label>
                                                    <input onchange="this.form.submit()" name="status" type="radio"
                                                           value=""> all
                                                </label>
                                                <br>
                                                <c:forEach items="${statuses}" var="item">
                                                    <label><input onchange="this.form.submit()"
                                                                  name="status"
                                                                  type="radio" value="${item}"
                                                        ${statusChecked == item ? 'checked="checked"' : ''}>
                                                        <c:out value="${item}"/>
                                                    </label>
                                                    <br>
                                                </c:forEach>
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
                                            Date
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
                                <div class="col-sm-8"><h2><b><fmt:message key="main.pricing"/></b></h2></div>
                                <%--    params--%>
                                <label for="size"><fmt:message key="master.size"/></label>
                                <select name="size" id="size" onchange="this.form.submit()">
                                    <c:forEach items="${sizes}" var="item">
                                        <option value="${item}" ${sizeChecked == item ? 'selected="selected"' : ''}>
                                                ${item}</option>
                                    </c:forEach>
                                </select>

                                <input type="submit" value="Filter">
                                <table id="mastersTable" class="table table-striped table-hover table-bordered">
                                    <thead>
                                    <tr>
                                        <th><b><fmt:message key="pricing.name"/></b<i></i></th>
                                        <th><b><fmt:message key="pricing.category"/></b<i></i></th>
                                        <th><b><fmt:message key="pricing.price"/></b<i></i></th>
                                        <th><b><fmt:message key="pricing.name"/></b<i></i></th>
                                        <th><b><fmt:message key="pricing.name"/></b<i></i></th>
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
                                            <td>${item.status}
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
            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                    <li ${pageChecked == 1 ? "class='page-item disabled'" : "class='page-item'"}>
                        <a href="${pageContext.request.contextPath}/salon${pathStr}&page=${pageChecked-1}"
                           class="page-link"><fmt:message key="carousel.previous"/></a>
                    </li>
                    <c:forEach items="${pagesArray}" var="item">
                        <li ${pageChecked == item ? "class='page-item active''" : "'class='page-item'"}>
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/salon${pathStr}&page=${item}">${item}</a>
                        </li>
                    </c:forEach>
                    <li ${pageChecked == lastPage ? "class='page-item disabled'" : "class='page-item'"}>
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/salon${pathStr}&page=${pageChecked+1}"><fmt:message
                                key="carousel.next"/></a>
                    </li>
                </ul>
            </nav>
        </div>
    </form>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
