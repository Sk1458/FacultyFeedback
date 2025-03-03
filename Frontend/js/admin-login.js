async function validateAdminLogin(event) {
    event.preventDefault(); // Prevent form submission

    // Use the correct IDs from your HTML
    const username = document.getElementById("admin-id").value;
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("student-error-message"); // or update HTML to admin-error-message

    try {
        const response = await fetch("http://localhost:8080/admin/validate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, password })
        });
        const isValid = await response.json();

        if (isValid) {
            window.location.href = "admin.html"; // Redirect to admin page on success
        } else {
            errorMessage.innerText = "Invalid credentials! Please try again.";
        }
    } catch (error) {
        console.error("Login error:", error);
        errorMessage.innerText = "Server error! Please try again later.";
    }
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