<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Pricing</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <form action="${pageContext.request.contextPath}/salon">
        <label>
            <input hidden name="command" value="pricing">
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
                                            Categories
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingTwo">
                                        <div class="accordion-body">
                                            <label class="form-check">
                                                <c:forEach items="${categories}" var="item">
                                                    <label><input name="categories" type="checkbox" value="${item.id}"
                                                        ${categoriesChecked.contains(item.id) ? 'checked="checked"' : ''}>
                                                        <c:out value="${item.name}"/>
                                                    </label>
                                                    <br>
                                                </c:forEach>
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
                                <select name="size" id="size">
                                    <c:forEach items="${sizes}" var="item">
                                        <option value="${item}" ${sizeChecked == item ? 'selected="selected"' : ''}>
                                                ${item}</option>
                                    </c:forEach>
                                </select>
                                <%--                            <label for="sort"><fmt:message key="master.sort"/></label>--%>
                                <%--                            <select name="sort" id="sort">--%>
                                <%--                                <c:forEach items="${sorts}" var="item">--%>
                                <%--                                    <option value="${item.name()}" ${sortChecked == item ? 'selected="selected"' : ''}>--%>
                                <%--                                            ${item.text}</option>--%>
                                <%--                                </c:forEach>--%>
                                <%--                            </select>--%>
                                <%--                            <label>--%>
                                <%--                                <input name="search" type="text" style="width: 400px" placeholder="Search..."--%>
                                <%--                                       value="${searchChecked}"--%>
                                <%--                                       aria-label="Search">--%>
                                <%--                            </label>--%>
                                <input type="submit" value="Filter">
                                <table id="mastersTable" class="table table-striped table-hover table-bordered">
                                    <thead>
                                    <tr>
                                        <th><b><fmt:message key="pricing.name"/></b<i></i></th>
                                        <th><b><fmt:message key="pricing.category"/></b<i></i></th>
                                        <th><b><fmt:message key="pricing.price"/></b<i></i></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${services}" var="item">
                                        <tr>
                                            <td>${item.service}</td>
                                            <td>${item.category}</td>
                                            <td>${item.price}/
                                                <fmt:formatNumber value="${item.price * 1.15}" maxFractionDigits="0"/>/
                                                <fmt:formatNumber value="${item.price * 1.3}"
                                                                  maxFractionDigits="0"/></td>
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
