<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Lilith</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <!--    carousel-->
    <div style="background-image: linear-gradient(#6c757d, white)" id="myCarousel" class="carousel slide"
         data-bs-ride="carousel">
        <div class="carousel-indicators">
            <button type="button" data-bs-target="#myCarousel" data-bs-slide-to="0" class=""
                    aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#myCarousel" data-bs-slide-to="1" aria-label="Slide 2"
                    class=""></button>
            <button type="button" data-bs-target="#myCarousel" data-bs-slide-to="2" aria-label="Slide 3"
                    class="active"
                    aria-current="true"></button>
        </div>
        <div class="carousel-inner">
            <div class="carousel-item">
                <img style="display: block; margin-left: auto; margin-right: auto" src="static/images/salon3.jpg"
                     alt="top-30"
                     width="700" height="400" class="fa"/>
                <div class="container">
                    <div class="carousel-caption">
                        <h1 class="carousel-font-style"></h1>
                        <p style="text-shadow: 2px 0 2px #0026bf"
                           class="carousel-font-style-small"><fmt:message key="carousel.info"/></p>
                        <p><a class="btn btn-lg btn-primary"
                              href="${pageContext.request.contextPath}/salon?command=about"><fmt:message
                                key="carousel.info2"/></a></p>
                    </div>
                </div>
            </div>
            <div class="carousel-item">
                <img style="display: block; margin-left: auto; margin-right: auto" src="static/images/salon2.jpg"
                     alt="top-30" width="700" height="400" class="fa"/>
                <div class="container">
                    <div class="carousel-caption">
                        <h1 class="carousel-font-style">.</h1>
                        <p style="text-shadow: 2px 0 2px #0026bf"
                           class="carousel-font-style-small"><fmt:message key="carousel.masters"/></p>
                        <p><a class="btn btn-lg btn-primary"
                              href="${pageContext.request.contextPath}/salon?command=masters"><fmt:message
                                key="carousel.masters2"/></a></p>
                    </div>
                </div>
            </div>
            <div class="carousel-item active">
                <img style="display: block; margin-left: auto; margin-right: auto" src="static/images/salon1.jpg"
                     alt="top-30" width="700" height="400" class="fa"/>
                <div class="container">
                    <div class="carousel-caption">
                        <h1 style="text-shadow: 2px 0 2px #0026bf" class="carousel-font-style"><fmt:message key="carousel.salon"/>
                            <fmt:message key="salon.name"/></h1>
                        <p class="carousel-font-style-small"><fmt:message key="carousel.great"/></p>
                        <p><a class="btn btn-lg btn-primary" href="#"><fmt:message key="carousel.appointment"/></a></p>
                    </div>
                </div>
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#myCarousel" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden"><fmt:message key="carousel.previous"/></span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#myCarousel" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden"><fmt:message key="carousel.next"/></span>
        </button>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
