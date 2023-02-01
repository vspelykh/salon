<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>404</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/styles.css">

    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp"/>
<div class="container">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <div class="page-404">
        <div class="outer">
            <div class="middle">
                <div class="inner">
                    <!--BEGIN CONTENT-->
                    <div class="inner-circle"><i class="fa fa-cogs"></i><span>500</span></div>
                    <span class="inner-status">Opps! Internal Server Error!</span>
                    <span class="inner-detail">Unfortunately we're having trouble loading the page you are looking for. Please come back in a while.</span>
                    <!--END CONTENT-->
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp"/>

</body>
</html>
