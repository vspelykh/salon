<%--
  Created by IntelliJ IDEA.
  User: Влад
  Date: 07.01.2023
  Time: 16:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign-up</title>
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
                                <h2 class="text-uppercase text-center mb-5">Create an account</h2>

                                <form action="${pageContext.request.contextPath}/salon?command=reg" method="post">
                                    <c:if test="${message != null}">
                                        <p5 class="text-danger">${message}</p5>
                                    </c:if>
                                    <div class="form-outline mb-4">
                                        <input type="text" id="formName" name="name"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formName">Your Name</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="text" id="formSurname" name="surname"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formSurname">Your Surname</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="email" id="formEmail" name="email"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formEmail">Your Email</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="tel" id="formNumber" name="number"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formNumber">Your Number</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="password" id="formPassword" name="password"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formPassword">Password</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="password" id="formPasswordRepeat" name="passwordRepeat"
                                               class="form-control active form-control-lg"/>
                                        <label class="form-label" for="formPasswordRepeat">Repeat your password</label>
                                    </div>

                                    <div class="d-flex justify-content-center">
                                        <button type="submit"
                                                class="btn btn-success btn-block btn-lg gradient-custom-4 text-body">
                                            Register
                                        </button>
                                    </div>

                                    <p class="text-center text-muted mt-5 mb-0">Have already an account? <a
                                            href="${pageContext.request.contextPath}/salon?command=login"
                                            class="fw-bold text-body"><u>Login here</u></a></p>
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
