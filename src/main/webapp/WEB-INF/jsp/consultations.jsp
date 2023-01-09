<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Consultations</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <table id="consTable" class="table table-striped table-hover table-bordered">
        <thead>
        <tr>
            <th>Name<i></i></th>
            <th>Email<i></i></th>
            <th>Number<i></i></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${consultations}" var="item">
            <tr>
                <td>${item.name} </td>
                <td>${item.number}</td>
                <td>${item.date}</td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/salon">
                        <label>
                            <input hidden name="command" value="consultation-delete">
                        </label>
                        <label>
                            <input hidden name="id" value="${item.id}">
                        </label>
                        <input class="btn btn-danger" type="submit" value="Remove"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
