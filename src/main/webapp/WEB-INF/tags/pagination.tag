<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<%@ attribute name="pageChecked" required="true"%>
<%@ attribute name="pathStr" required="true" %>
<%@ attribute name="pagesArray" required="true" type="java.lang.Integer[]" %>
<%@ attribute name="lastPage" required="true"%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="localization.messages"/>

<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
        <li ${pageChecked == 1 ? "class='page-item disabled'" : "class='page-item'"}>
            <a href="${pageContext.request.contextPath}/salon${pathStr}&page=${pageChecked-1}"
               class="page-link"><fmt:message key="carousel.previous"/></a>
        </li>
        <c:forEach items="${pagesArray}" var="item">
            <li ${pageChecked == item ? "class='page-item active''" : "'class='page-item'"}>
                <a class="page-link"
                   href="${pageContext.request.contextPath}/salon${pathStr}&page=${item}">${item}</a>
            </li>
        </c:forEach>
        <li ${pageChecked == lastPage ? "class='page-item disabled'" : "class='page-item'"}>
            <a class="page-link"
               href="${pageContext.request.contextPath}/salon${pathStr}&page=${pageChecked+1}"><fmt:message
                    key="carousel.next"/></a>
        </li>
    </ul>
</nav>