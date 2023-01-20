<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Consultation</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="container">
    <section class="text-center">
        <!-- Background image -->
        <div class="p-5 bg-image" style="
        background-image: url('https://cdn.media.amplience.net/i/Cosnova/essence-beauty-benzz-023-product-mobile-4122');
        height: 300px;
        "></div>
        <!-- Background image -->
        <div class="card mx-4 mx-md-5 shadow-5-strong" style="
        margin-top: -100px;
        background: hsla(0, 0%, 100%, 0.8);
        backdrop-filter: blur(30px);
        ">
            <div class="card-body py-5 px-md-5">
                <div class="row d-flex justify-content-center">
                    <div class="col-lg-8">
                        <h2 class="fw-bold mb-5">Send us your name and number, and we will call you at the nearest
                            time</h2>
                        <form method="post" action="${pageContext.request.contextPath}/salon">
                            <label>
                                <input hidden name="command" value="consultation-post">
                            </label>
                            <div class="form-outline mb-4">
                                <input type="text" name="name" id="name" class="form-control active"/>
                                <label class="form-label" for="name">Name</label>
                            </div>

                            <div class="form-outline mb-4">
                                <input type="tel" name="number" id="number" class="form-control active"/>
                                <label class="form-label" for="number">Number</label>
                            </div>
                            <button type="submit" class="btn btn-primary btn-block mb-4">
                                Submit
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
