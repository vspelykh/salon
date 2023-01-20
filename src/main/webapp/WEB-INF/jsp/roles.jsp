<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Roles</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <%
    if(request.getParameter("message") != null){
        out.print("<p5 class=\"text-danger\">Set of roles successfully changed</p5>");
        }
    %>
    <form action="${pageContext.request.contextPath}/salon">
        <label>
            <input hidden name="command" value="roles">
        </label>
        <div class="form-group">
            <label for="exampleFormControlInput1">Input email or mobile number</label>
            <input name="search" type="text" class="form-control" id="exampleFormControlInput1"
                   placeholder="+380661234567 or name@example.com and press Enter">
        </div>
        <input type="submit" hidden class="btn btn-info" value="Find">
    </form>
    <c:choose>
        <c:when test="${users != null}">
            <table id="rolesTable" class="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th>Name<i></i></th>
                    <th>Email<i></i></th>
                    <th>Number<i></i></th>
                    <th>Roles<i></i></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="item">
                    <tr>
                        <td>${item.name} ${item.surname}</td>
                        <td>${item.email}</td>
                        <td>${item.number}</td>
                        <td>${item.roles}</td>
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
                                        <input class="btn btn-danger" type="submit" value="Remove Administration role"/>
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
                                        <input class="btn btn-primary" type="submit" value="Add Administration role"/>
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
                                        <input class="btn btn-danger" type="submit" value="Remove Hairdresser role"/>
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
                                        <input class="btn btn-primary" type="submit" value="Add Hairdresser role"/>
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
                                        <input class="btn btn-danger" type="submit" value="Remove Client role"/>
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
                                        <input class="btn btn-primary" type="submit" value="Add Client role"/>
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
