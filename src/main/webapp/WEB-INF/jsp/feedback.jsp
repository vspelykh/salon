<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><html>
<head>
    <title>Feedback</title>
    <link rel="stylesheet" href="static/styles.css">
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <section class="vh-100 bg-image"
             style="background-image: url('https://static.wixstatic.com/media/11062b_9d6344682fad41cb95225ff4856adec2~mv2.jpg/v1/fill/w_640,h_480,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/11062b_9d6344682fad41cb95225ff4856adec2~mv2.jpg');">
        <div class="mask d-flex align-items-center h-100 gradient-custom-3">
            <div class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                        <div class="card" style="border-radius: 15px;">
                            <div class="card-body p-5">
                                <div id="feedback-form-wrapper">

                                    <div id="feedback-form-modal">

                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="exampleModalLabel">Feedback
                                                        Form</h5>
                                                </div>
                                                <div class="modal-body">
                                                    <form method="post" id="ff"
                                                          action="${pageContext.request.contextPath}/salon">
                                                        <input hidden name="command" value="feedback-post">
                                                        <input hidden name="appointment_id" value="${param.get('id')}">
                                                        <div class="form-group">
                                                            <label>Please rate the service of the master</label>
                                                            <div class="rating-input-wrapper d-flex justify-content-between mt-2">
                                                                <label><input value="1" type="radio" name="mark"/><span
                                                                        class="border rounded px-3 py-2">1</span></label>
                                                                <label><input value="2" type="radio" name="mark"/><span
                                                                        class="border rounded px-3 py-2">2</span></label>
                                                                <label><input value="3" type="radio" name="mark"/><span
                                                                        class="border rounded px-3 py-2">3</span></label>
                                                                <label><input value="4" type="radio" name="mark"/><span
                                                                        class="border rounded px-3 py-2">4</span></label>
                                                                <label><input value="5" type="radio" name="mark"/><span
                                                                        class="border rounded px-3 py-2">5</span></label>
                                                            </div>
                                                            <div class="rating-labels d-flex justify-content-between mt-1">
                                                                <label>Very unlikely</label>
                                                                <label>Very likely</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>Would you like to say
                                                                something?</label>
                                                            <textarea name="comment" class="form-control" id="input-two"
                                                                      rows="3"></textarea>
                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <button form="ff" type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
