<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="profile.name"/></title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
        <c:choose>
            <c:when test="${isAdmin}">
                <li><a href="${pageContext.request.contextPath}/salon?command=admin" class="nav-link px-2 text-primary">
                    <fmt:message key="profile.admin"/></a>
                </li>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${isMaster}">
                <li>
                    <a href="${pageContext.request.contextPath}/salon?command=look-schedule&id=${sessionScope.currentUser.id}"
                       class="nav-link px-2 text-primary"><fmt:message key="profile.schedule"/></a>
                </li>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${isClient}">
                <li><a href="${pageContext.request.contextPath}/salon?command=my-appointments"
                       class="nav-link px-2 text-primary"><fmt:message key="profile.client"/></a>
                </li>
            </c:when>
        </c:choose>
    </ul>
    <section style="background-color: #eee;">
        <div class="container py-5">
            <div class="row">
                <div class="col-lg-8">
                    <div class="card mb-4">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-sm-3">
                                    <p class="mb-0"><fmt:message key="profile.full"/></p>
                                </div>
                                <div class="col-sm-9">
                                    <p class="text-muted mb-0">${user.name} ${user.surname}</p>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <p class="mb-0"><fmt:message key="sign.email"/></p>
                                </div>
                                <div class="col-sm-9">
                                    <p class="text-muted mb-0">${user.email}</p>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <p class="mb-0"><fmt:message key="sign.number"/></p>
                                </div>
                                <div class="col-sm-9">
                                    <p class="text-muted mb-0">${user.number}</p>
                                </div>
                            </div>
                            <hr>
                            <c:choose>
                                <c:when test="${isAdmin || isMaster}">
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <p class="mb-0"><fmt:message key="roles.roles"/></p>
                                        </div>
                                        <div class="col-sm-9">
                                            <p class="text-muted mb-0">
                                                <c:choose>
                                                    <c:when test="${isAdmin && isMaster}">
                                                        <fmt:message key="role.admin"/>, <fmt:message
                                                            key="role.master"/>
                                                    </c:when>
                                                    <c:when test="${isAdmin}">
                                                        <fmt:message key="role.admin"/>
                                                    </c:when>
                                                    <c:when test="${isMaster}">
                                                        <fmt:message key="role.master"/>
                                                    </c:when>
                                                </c:choose>

                                            </p>
                                        </div>
                                    </div>
                                    <hr>
                                </c:when>
                            </c:choose>
                            <c:choose>
                                <c:when test="${isMaster}">
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <p class="mb-0">Level</p>
                                        </div>
                                        <div class="col-sm-9">
                                            <p class="text-muted mb-0">${userLevel.level}</p>
                                        </div>
                                    </div>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
