function validateFacultyLogin(event) {
    event.preventDefault();  // Prevent default form submission

    let username = document.getElementById("faculty-id").value;
    let password = document.getElementById("password").value;

    fetch("http://localhost:8080/faculty/validate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })  // Ensure backend expects this format
    })
    .then(response => response.json())
    .then(data => {
        if (data) {
            alert("Login successful!");
            window.location.href = "faculty-dashboard.html"; // Redirect on success
        } else {
            document.getElementById("faculty-error-message").innerText = "Invalid credentials. Please try again.";
        }
    })
    .catch(error => console.error("Error:", error));
}

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