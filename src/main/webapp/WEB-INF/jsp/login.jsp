<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
    <title>Login</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <style>
        .bg-image-vertical {
            position: relative;
            overflow: hidden;
            background-repeat: no-repeat;
            background-position: right center;
            background-size: auto 100%;
        }

        @media (min-width: 1025px) {
            .h-custom-2 {
                height: 100%;
            }
        }
    </style>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <section class="vh-100">
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-6 text-black">

                    <div class="px-5 ms-xl-4">
                        <em class="fas fa-scissors fa-2x me-3 pt-5 mt-xl-4" style="color: #709085;"></em>
                        <span class="h1 fw-bold mb-0"><fmt:message key="salon.name"/></span>
                    </div>

                    <div class="d-flex align-items-center h-custom-2 px-5 ms-xl-4 mt-5 pt-5 pt-xl-0 mt-xl-n5">

                        <form action="${pageContext.request.contextPath}/salon?command=checkLogin" method="post"
                              style="width: 23rem;">

                            <h3 class="fw-normal mb-3 pb-3" style="letter-spacing: 1px;"><fmt:message
                                    key="log.log"/></h3>

                            <div class="form-outline mb-4">
                                <input name="login" type="email" id="loginFormEmail"
                                       class="form-control form-control-lg active"
                                       placeholder="Email address"
                                       value="${insLogin}"/>
                                <label class="form-label" for="loginFormEmail"><fmt:message key="sign.email"/></label>
                            </div>

                            <div class="form-outline mb-4">
                                <input name="password" type="password" id="loginFormEmailPass"
                                       class="form-control active form-control-lg"
                                       placeholder="Password"
                                       value="${insPassword}"/>
                                <label class="form-label" for="loginFormEmailPass"><fmt:message
                                        key="sign.pass"/></label>
                            </div>
                            <c:choose>
                                <c:when test="${sessionScope.message != null}">
                                <p5 class="text-danger"><fmt:message key="login.fail"/></p5>
                                ${sessionScope.message = null}
                                </c:when>
                            </c:choose>

                            <div class="pt-1 mb-4">
                                <button class="btn btn-info btn-lg btn-block" type="submit"><fmt:message
                                        key="main.login"/></button>
                            </div>

                            <p class="small mb-5 pb-lg-2"><a class="text-muted" href="#!"><fmt:message
                                    key="log.forgot"/></a></p>
                            <p><fmt:message key="log.acc"/> <a
                                    href="${pageContext.request.contextPath}/salon?command=sign-up"
                                    class="link-info"><fmt:message key="log.reg"/></a></p>

                        </form>

                    </div>

                </div>
                <div class="col-sm-6 px-0 d-none d-sm-block">
                    <img src="static/images/welcome.png"
                         alt="Login image" class="w-100 vh-100" style="object-fit: cover; object-position: left;">
                </div>
            </div>
        </div>
    </section>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
