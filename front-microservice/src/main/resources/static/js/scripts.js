function checkToken() {
    var email = document.getElementById('loginFormEmail').value;
    var password = document.getElementById('loginFormEmailPass').value;

    $.ajax({
        url: '/user/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email: email, password: password }),
        success: function(response) {
            if (response.accessToken) {
                localStorage.setItem('accessToken', response.accessToken);

                window.location.href = '/home';
            } else {
                document.getElementById('errorText').innerText = 'Invalid credentials';
            }
        },
        error: function(error) {
            console.error('errro:', error);
        }
    });
}