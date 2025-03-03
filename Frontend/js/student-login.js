async function validateStudentLogin(event) {
    event.preventDefault(); // Prevent form submission

    const studentId = document.getElementById("student-id").value;
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("student-error-message");

    try {
        const response = await fetch("http://localhost:8080/student/validate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ studentId, password })
        });

        if (!response.ok) throw new Error("Network response was not ok");

        const isValid = await response.json(); // isValid is a boolean now

        if (isValid) {
            window.location.href = "feedback.html"; // Redirect on success
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