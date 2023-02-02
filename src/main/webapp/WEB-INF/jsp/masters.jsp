<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="master.our"/> <fmt:message key="master.masters"/></title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <form action="${pageContext.request.contextPath}/salon">
        <label>
            <input hidden name="command" value="masters">
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
                                    <h2 class="accordion-header" id="panelsStayOpen-headingCat">
                                        <button class="accordion-button collapsed" type="button"
                                                data-bs-toggle="collapse"
                                                data-bs-target="#panelsStayOpen-collapseCat" aria-expanded="false"
                                                aria-controls="panelsStayOpen-collapseCat">
                                            <fmt:message key="master.categories"/>
                                        </button>
                                    </h2>
                                    <div id="panelsStayOpen-collapseCat" class="accordion-collapse collapse"
                                         aria-labelledby="panelsStayOpen-headingCat">
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
                                    <div class="accordion-item">
                                        <h2 class="accordion-header" id="panelsStayOpen-headingOne">
                                            <button class="accordion-button collapsed" type="button"
                                                    data-bs-toggle="collapse"
                                                    data-bs-target="#panelsStayOpen-collapseOne" aria-expanded="false"
                                                    aria-controls="panelsStayOpen-collapseOne">
                                                <fmt:message key="master.services"/>
                                            </button>
                                        </h2>
                                        <div id="panelsStayOpen-collapseOne" class="accordion-collapse collapse"
                                             aria-labelledby="panelsStayOpen-headingOne">
                                            <div class="accordion-body">
                                                <label class="form-check">
                                                    <c:forEach items="${services}" var="item">
                                                        <label>
                                                            <input name="services" type="checkbox" value="${item.id}"
                                                                ${servicesChecked.contains(item.id) ? 'checked="checked"' : ''}>
                                                            <c:out value="${item.service}"/>
                                                        </label>
                                                        <br>
                                                    </c:forEach>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordion-item">
                                        <h2 class="accordion-header" id="panelsStayOpen-headingTwo">
                                            <button class="accordion-button collapsed" type="button"
                                                    data-bs-toggle="collapse"
                                                    data-bs-target="#panelsStayOpen-collapseTwo" aria-expanded="false"
                                                    aria-controls="panelsStayOpen-collapseTwo">
                                                <fmt:message key="master.levels"/>
                                            </button>
                                        </h2>
                                        <div id="panelsStayOpen-collapseTwo" class="accordion-collapse collapse"
                                             aria-labelledby="panelsStayOpen-headingTwo">
                                            <div class="accordion-body">
                                                <label class="form-check">
                                                    <c:forEach items="${levels}" var="item">
                                                        <label><input name="levels" type="checkbox" value="${item.name}"
                                                            ${levelsChecked.contains(item) ? 'checked="checked"' : ''}>
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
                                <div class="col-sm-8"><h2><fmt:message key="master.our"/> <b><fmt:message
                                        key="master.masters"/></b></h2></div>
                                <%--    params--%>
                                <label for="size"><fmt:message key="master.size"/></label>
                                <select onchange="this.form.submit()" name="size" id="size">
                                    <c:forEach items="${sizes}" var="item">
                                        <option value="${item}" ${sizeChecked == item ? 'selected="selected"' : ''}>
                                                ${item}</option>
                                    </c:forEach>
                                </select>
                                <label for="sort"><fmt:message key="master.sort"/></label>
                                <select onchange="this.form.submit()" name="sort" id="sort">
                                    <option value="NAME_ASC" ${sortChecked == 'NAME_ASC' ? 'selected="selected"' : ''}>
                                        A-Z
                                    </option>
                                    <option value="NAME_DESC" ${sortChecked == 'NAME_DESC' ? 'selected="selected"' : ''}>
                                        Z-A
                                    </option>
                                    <option value="RATING_DESC" ${sortChecked == 'RATING_DESC' ? 'selected="selected"' : ''}>
                                        <fmt:message key="sort.rating"/></option>
                                    <option value="RATING_ASC" ${sortChecked == 'RATING_ASC' ? 'selected="selected"' : ''}>
                                        <fmt:message key="sort.ratingdown"/></option>
                                    <option value="FIRST_PRO" ${sortChecked == 'FIRST_PRO' ? 'selected="selected"' : ''}>
                                        <fmt:message key="sort.pro"/></option>
                                    <option value="FIRST_YOUNG" ${sortChecked == 'FIRST_YOUNG' ? 'selected="selected"' : ''}>
                                        <fmt:message key="sort.young"/></option>
                                </select>
                                <label>
                                    <input onchange="this.form.submit()" name="search" type="text" style="width: 400px"
                                           placeholder=
                                           <fmt:message key="main.search"/>
                                                   value="${searchChecked}"
                                           aria-label="Search">
                                </label>
                                <input type="submit" value=<fmt:message key="main.filter"/>>
                                <table id="mastersTable" class="table table-striped table-hover table-bordered">
                                    <thead>
                                    <tr>
                                        <th><fmt:message key="master.photo"/><i></i></th>
                                        <th><fmt:message key="master.name"/><i></i></th>
                                        <th><fmt:message key="master.about"/><i></i></th>
                                        <th><fmt:message key="master.level"/><i></i></th>
                                        <th><fmt:message key="main.rating"/></th>
                                        <c:choose>
                                            <c:when test="${isAdmin}">
                                                <th></th>
                                            </c:when>
                                        </c:choose>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${masters}" var="item">
                                        <tr>
                                            <td></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/salon?command=calendar&id=${item.id}"
                                                   class="nav-link px-2 text-decoration-underline">${item.name} ${item.surname}</a>
                                            </td>
                                            <td>${item.about}</td>
                                            <td>${item.level}</td>
                                            <c:choose>
                                                <c:when test="${isAdmin}">
                                                    <th>
                                                        <a href="${pageContext.request.contextPath}/salon?command=schedule&id=${item.id}"
                                                           class="btn btn-primary nav-link px-2 text-white"><fmt:message
                                                                key="master.manage"/></a>
                                                        <a href="${pageContext.request.contextPath}/salon?command=look-schedule&id=${item.id}"
                                                           class="btn btn-success nav-link px-2 text-white"><fmt:message
                                                                key="master.look"/></a>
                                                    </th>
                                                </c:when>
                                            </c:choose>
                                            <td>${item.rating}</td>
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
</div>
<br>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
