<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
            crossorigin="anonymous"></script>
</head>

<header class="p-3 bg-dark text-white">
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <%
        request.setAttribute("path", request.getQueryString());
    %>
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap">
                    <use xlink:href="#bootstrap"></use>
                </svg>
            </a>
            <img alt="" src="static/images/logo.jpg" width="50" height="50">
            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="${pageContext.request.contextPath}/salon"
                       class="nav-link px-2 text-secondary"><fmt:message key="main.home"/> </a>
                </li>
                <li><a href="${pageContext.request.contextPath}/salon?command=masters" class="nav-link px-2 text-white"><fmt:message
                        key="main.masters"/></a>
                </li>
                <li><a href="${pageContext.request.contextPath}/salon?command=pricing" class="nav-link px-2 text-white"><fmt:message
                        key="main.pricing"/></a>
                </li>
                <li><a href="${pageContext.request.contextPath}/salon?command=consultation"
                       class="nav-link px-2 text-white"><fmt:message key="main.consultation"/></a>
                </li>
                <li><a href="${pageContext.request.contextPath}/salon?command=about"
                       class="nav-link px-2 text-white"><fmt:message key="main.about"/></a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button"
                       data-bs-toggle="dropdown" aria-expanded="true"><fmt:message key="main.lang"/></a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item"
                               href="${pageContext.request.contextPath}/salon?${requestScope.get("path")}&lang=ua"><i
                                class="flag flag-ukraine"></i></i>
                            <fmt:message key="main.ua"/></a></li>
                        <li><a class="dropdown-item"
                               href="${pageContext.request.contextPath}/salon?${requestScope.get("path")}&lang=en"><i
                                class="flag flag-united-kingdom"></i></i>
                            <fmt:message key="main.en"/></a></li>
                    </ul>
                </li>
            </ul>

            <div class="text-end">
                <c:choose>
                    <c:when test="${!sessionScope.get('isLogged')}">
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=login">
                                <button type="button" class="btn btn-outline-light me-2"><fmt:message key="main.login"/>
                                </button>
                            </a>
                        </label>
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=sign-up">
                                <button type="button" class="btn btn-info"><fmt:message key="main.sign"/></button>
                            </a>
                        </label>
                    </c:when>
                    <c:otherwise>
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=logout">
                                <button type="button" class="btn btn-outline-light me-2"><fmt:message
                                        key="main.logout"/>
                                </button>
                            </a>
                        </label>
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=profile">
                                <button type="button" class="btn btn-success me-2"><fmt:message key="main.profile"/>
                                </button>
                            </a>
                        </label>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>