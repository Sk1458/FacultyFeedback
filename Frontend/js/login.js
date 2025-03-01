document.getElementById('togglePassword').addEventListener('click', function () {
    var passwordField = document.getElementById("password");
    var toggleButton = document.getElementById("togglePassword");

    if (passwordField.type === "password") {
        passwordField.type = "text";  // Show password
        toggleButton.innerText = "Hide";  // Change button text
    } else {
        passwordField.type = "password";  // Hide password
        toggleButton.innerText = "Show";  // Change button text
    }
});