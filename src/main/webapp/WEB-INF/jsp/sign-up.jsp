<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><html>
<head>
    <title>Sign-up</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <section class="vh-100 bg-image"
             style="background-image: url('https://mdbcdn.b-cdn.net/img/Photos/new-templates/search-box/img4.webp');">
        <div class="mask d-flex align-items-center h-100 gradient-custom-3">
            <div class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                        <div class="card" style="border-radius: 15px;">
                            <div class="card-body p-5">
                                <h2 class="text-uppercase text-center mb-5"><fmt:message key="sign.create"/></h2>

                                <form action="${pageContext.request.contextPath}/salon?command=reg" method="post">
                                    <c:choose>
                                        <c:when test="${message != null}">
                                            <p5 class="text-danger"><fmt:message key="error.${message}"/></p5>
                                        </c:when>
                                    </c:choose>
                                    <div class="form-outline mb-4">
                                        <input type="text" id="formName" name="name" value="${name}"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formName"><fmt:message key="sign.name"/></label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="text" id="formSurname" name="surname" value="${surname}"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formSurname"><fmt:message key="sign.surname"/></label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="email" id="formEmail" name="email" value="${email}"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formEmail"><fmt:message key="sign.email"/></label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="tel" id="formNumber" name="number" value="${number}"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formNumber"><fmt:message key="sign.number"/></label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="text" id="formKey" name="key" value="${key}"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formKey"><fmt:message key="sign.key"/></label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="password" id="formPassword" name="password" value="${password}"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formPassword"><fmt:message key="sign.pass"/></label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="password" id="formPasswordRepeat" name="passwordRepeat" value="${passwordRepeat}"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formPasswordRepeat"><fmt:message key="sign.repeat"/></label>
                                    </div>

                                    <div class="d-flex justify-content-center">
                                        <button type="submit"
                                                class="btn btn-success btn-block btn-lg gradient-custom-4 text-body">
                                            <fmt:message key="sign.reg"/>
                                        </button>
                                    </div>

                                    <p class="text-center text-muted mt-5 mb-0"><fmt:message key="sign.already"/> <a
                                            href="${pageContext.request.contextPath}/salon?command=login"
                                            class="fw-bold text-body"><u><fmt:message key="sign.log"/></u></a></p>
                                </form>
                            </div>
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
