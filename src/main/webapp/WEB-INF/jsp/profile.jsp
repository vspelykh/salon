<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
        <c:choose>
            <c:when test="${isAdmin}">
                <li><a href="${pageContext.request.contextPath}/salon" class="nav-link px-2 text-primary">Admin page</a>
                </li>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${isMaster}">
                <li><a href="${pageContext.request.contextPath}/salon" class="nav-link px-2 text-primary">Hairdresser
                    page</a>
                </li>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${isClient}">
                <li><a href="${pageContext.request.contextPath}/salon" class="nav-link px-2 text-primary">Client
                    page</a>
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
                                    <p class="mb-0">Full Name</p>
                                </div>
                                <div class="col-sm-9">
                                    <p class="text-muted mb-0">${user.name} ${user.surname}</p>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <p class="mb-0">Email</p>
                                </div>
                                <div class="col-sm-9">
                                    <p class="text-muted mb-0">${user.email}</p>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <p class="mb-0">Phone</p>
                                </div>
                                <div class="col-sm-9">
                                    <p class="text-muted mb-0">${user.number}</p>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <p class="mb-0">Roles</p>
                                </div>
                                <div class="col-sm-9">
                                    <p class="text-muted mb-0">${user.roles}</p>
                                </div>
                            </div>
                            <hr>
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
