<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>403</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/styles.css">
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>

</head>
<body>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp"/>
<div class="text-wrapper">
    <div class="title" data-content="404">
        <fmt:message key="403.denied"/>
    </div>

    <div class="subtitle">
        <fmt:message key="403.access"/>
    </div>

    <div class="buttons">
        <a class="button" href="${pageContext.request.contextPath}/salon"><fmt:message key="404.home"/></a>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp"/>

</body>
</html>
