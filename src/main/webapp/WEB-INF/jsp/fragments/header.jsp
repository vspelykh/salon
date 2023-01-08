<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap">
                    <use xlink:href="#bootstrap"></use>
                </svg>
            </a>
            <img alt="" src="static/images/logo.jpg" width="50" height="50">
            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="${pageContext.request.contextPath}/salon" class="nav-link px-2 text-secondary">Home</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/salon?command=masters" class="nav-link px-2 text-white">Masters</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/salon?command=pricing" class="nav-link px-2 text-white">Pricing</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/salon?command=cabinet" class="nav-link px-2 text-white">Your
                    appointments</a></li>
                <li><a href="${pageContext.request.contextPath}/salon?command=about" class="nav-link px-2 text-white">About</a>
                </li>
            </ul>

            <div class="text-end">
                <c:choose>
                    <c:when test="${!sessionScope.get('isLogged')}">
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=login">
                                <button type="button" class="btn btn-outline-light me-2">Login
                                </button>
                            </a>
                        </label>
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=sign-up">
                                <button type="button" class="btn btn-info">Sign-up</button>
                            </a>
                        </label>
                    </c:when>
                    <c:otherwise>
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=logout">
                                <button type="button" class="btn btn-outline-light me-2">Logout
                                </button>
                            </a>
                        </label>
                        <label>
                            <a href="${pageContext.request.contextPath}/salon?command=profile">
                                <button type="button" class="btn btn-success me-2">Profile
                                </button>
                            </a>
                        </label>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>