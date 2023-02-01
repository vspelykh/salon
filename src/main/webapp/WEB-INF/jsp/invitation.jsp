<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>About</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <div class="card" style="border-radius: 15px;">
        <div class="card-body p-5">
            <h2 class="text-uppercase text-center mb-5">Create an invitation</h2>

            <form action="${pageContext.request.contextPath}/salon" method="post">
                <input hidden name="command" value="create-invitation">
                <div class="form-outline mb-4">
                    <input type="text" id="email" name="email"
                           class="form-control active form-control-lg"/>
                    <label class="form-label" for="email">email</label>
                </div>

                <div class="form-outline mb-4">
                    <label for="role"></label><select name="role" id="role">
                        <option value="ADMINISTRATOR">Administrator</option>
                        <option value="HAIRDRESSER">Master</option>
                    </select>
                </div>
                <div class="d-flex justify-content-center">
                    <button type="submit"
                            class="btn btn-success btn-block btn-lg gradient-custom-4 text-body">
                        Submit
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
