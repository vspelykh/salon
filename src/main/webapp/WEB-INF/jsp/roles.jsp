<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <title><fmt:message key="roles.roles"/></title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <c:choose>
        <c:when test="${sessionScope.message != null}">
            <p5 class="text-danger"><fmt:message key="roles.success"/></p5>
            ${sessionScope.message = null}
        </c:when>
    </c:choose>
    <form action="${pageContext.request.contextPath}/salon">
        <label>
            <input hidden name="command" value="roles">
        </label>
        <div class="form-group">
            <label for="exampleFormControlInput1"><fmt:message key="roles.input"/></label>
            <input name="search" type="text" class="form-control" id="exampleFormControlInput1"
                   placeholder="<fmt:message key="roles.holder"/>">
        </div>
        <input type="submit" hidden class="btn btn-info" value="Find">
    </form>
    <c:choose>
        <c:when test="${users != null}">
            <table id="rolesTable" class="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th><fmt:message key="master.name"/><i></i></th>
                    <th><fmt:message key="roles.mail"/><i></i></th>
                    <th><fmt:message key="cons.num"/><i></i></th>
                    <th><fmt:message key="roles.roles"/><i></i></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="item">
                    <tr>
                        <td>${item.name} ${item.surname}</td>
                        <td>${item.email}</td>
                        <td>${item.number}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.roles.contains(requestScope.get('admin')) == true}">
                                    <fmt:message key="role.admin"/>
                                    <br>
                                </c:when>
                            </c:choose>
                            <c:choose>
                                <c:when test="${item.roles.contains(requestScope.get('master')) == true}">
                                    <fmt:message key="role.master"/>
                                    <br>
                                </c:when>
                            </c:choose>
                            <c:choose>
                                <c:when test="${item.roles.contains(requestScope.get('client')) == true}">
                                    <fmt:message key="role.client"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${item.roles.contains(requestScope.get('admin')) == true}">
                                    <form method="post" action="${pageContext.request.contextPath}/salon">
                                        <input hidden name="command" value="change-role">
                                        <label>
                                            <input hidden name="role" value="ADMINISTRATOR">
                                        </label>
                                        <label>
                                            <input hidden name="action" value="remove">
                                        </label>
                                        <label>
                                            <input hidden name="user_id" value="${item.id}">
                                        </label>
                                        <input class="btn btn-danger" type="submit" value="<fmt:message key="remove.admin"/>"/>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" action="${pageContext.request.contextPath}/salon">
                                        <input hidden name="command" value="change-role">
                                        <label>
                                            <input hidden name="role" value="ADMINISTRATOR">
                                        </label>
                                        <label>
                                            <input hidden name="action" value="add">
                                        </label>
                                        <label>
                                            <input hidden name="user_id" value="${item.id}">
                                        </label>
                                        <input class="btn btn-primary" type="submit" value="<fmt:message key="add.admin"/>"/>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${item.roles.contains(requestScope.get('master')) == true}">
                                    <form method="post" action="${pageContext.request.contextPath}/salon">
                                        <input hidden name="command" value="change-role">
                                        <label>
                                            <input hidden name="role" value="HAIRDRESSER">
                                        </label>
                                        <label>
                                            <input hidden name="action" value="remove">
                                        </label>
                                        <label>
                                            <input hidden name="user_id" value="${item.id}">
                                        </label>
                                        <input class="btn btn-danger" type="submit" value="<fmt:message key="remove.master"/>"/>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" action="${pageContext.request.contextPath}/salon">
                                        <input hidden name="command" value="change-role">
                                        <label>
                                            <input hidden name="role" value="HAIRDRESSER">
                                        </label>
                                        <label>
                                            <input hidden name="action" value="add">
                                        </label>
                                        <label>
                                            <input hidden name="user_id" value="${item.id}">
                                        </label>
                                        <label>
                                            <input hidden name="search" value="${search}">
                                        </label>
                                        <input class="btn btn-primary" type="submit" value="<fmt:message key="add.master"/>"/>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${item.roles.contains(requestScope.get('client')) == true}">
                                    <form method="post" action="${pageContext.request.contextPath}/salon">
                                        <input hidden name="command" value="change-role">
                                        <label>
                                            <input hidden name="role" value="CLIENT">
                                        </label>
                                        <label>
                                            <input hidden name="action" value="remove">
                                        </label>
                                        <label>
                                            <input hidden name="user_id" value="${item.id}">
                                        </label>
                                        <input class="btn btn-danger" type="submit" value="<fmt:message key="remove.client"/>"/>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" action="${pageContext.request.contextPath}/salon">
                                        <input hidden name="command" value="change-role">
                                        <label>
                                            <input hidden name="role" value="CLIENT">
                                        </label>
                                        <label>
                                            <input hidden name="action" value="add">
                                        </label>
                                        <label>
                                            <input hidden name="user_id" value="${item.id}">
                                        </label>
                                        <label>
                                            <input hidden name="search" value="${search}">
                                        </label>
                                        <input class="btn btn-primary" type="submit" value="<fmt:message key="add.client"/>"/>
                                    </form>
                                </c:otherwise>
                            </c:choose>
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
