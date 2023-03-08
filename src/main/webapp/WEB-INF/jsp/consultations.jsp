<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="admin.consultations"/></title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <c:choose>
        <c:when test="${consultations.isEmpty()}">
            <fmt:message key="cons.empty"/>
        </c:when>
        <c:when test="${!consultations.isEmpty()}">

            <table id="consTable" class="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th><fmt:message key="master.name"/><i></i></th>
                    <th><fmt:message key="cons.num"/><i></i></th>
                    <th><fmt:message key="orders.date"/><i></i></th>
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
                            <form action="${pageContext.request.contextPath}/salon"
                                  method="post">
                                <input hidden name="id" value="${item.id}">
                                <input hidden name="command" value="consultation-edit">
                                <c:choose>
                                    <c:when test="${item.read == false}">
                                        <button class="btn btn-warning" type="submit" name="action" value="read">Read</button>
                                    </c:when>
                                </c:choose>

                                <button class="btn btn-danger" type="submit" name="action" value="delete">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
    </c:choose>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
