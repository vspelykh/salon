<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Our masters</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Company</th>
            <th>Contact</th>
            <th>Country</th>
        </tr>
        <c:forEach items="${masters}" var="item">
            <tr>
                <td>${item.name}</td>
                <td>${item.surname}</td>
                <td>${item.number}</td>
            </tr>
        </c:forEach>
        </thead>
    </table>
    <nav aria-label="Page navigation example">
        <ul class="pagination">
            <li class="page-item"><a class="page-link" href="#">Previous</a></li>
            <li class="page-item"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item"><a class="page-link" href="#">Next</a></li>
        </ul>
    </nav>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
