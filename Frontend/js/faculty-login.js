// ✅ Faculty Login Validation
async function validateFacultyLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById("faculty-id").value;
    const password = document.getElementById("password").value;
    
    const response = await fetch("http://localhost:8080/faculty/validate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    const data = await response.json();
    
    if (data.success) {
        sessionStorage.setItem("facultyId", data.facultyId);
        alert("Login successful!");
        window.location.href = "faculty-dashboard.html";
    } else {
        document.getElementById("faculty-error-message").innerText = "Invalid credentials. Please try again.";
    }
}

// ✅ Toggle Password Visibility
document.getElementById('togglePassword').addEventListener('click', () => {
    const passwordField = document.getElementById("password");
    passwordField.type = passwordField.type === "password" ? "text" : "password";
});

// 🔥 Forgot Password Functions
async function sendFacultyOTP() {
    const facultyId = document.getElementById("forgotFacultyId").value;
    
    const response = await fetch("http://localhost:8080/email/send-faculty-otp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ facultyId })
    });

    if (response.ok) {
        document.getElementById("step1").style.display = "none";
        document.getElementById("step2").style.display = "block";
    }
}

async function verifyFacultyOTP() {
    const facultyId = document.getElementById("forgotFacultyId").value;
    const otp = document.getElementById("otp").value;

    const response = await fetch("http://localhost:8080/email/verify-faculty-otp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ facultyId, otp })
    });

    if (response.ok) {
        document.getElementById("step2").style.display = "none";
        document.getElementById("step3").style.display = "block";
    }
}

async function resetFacultyPassword() {
    const facultyId = document.getElementById("forgotFacultyId").value;
    const newPassword = document.getElementById("newPassword").value;

    const response = await fetch("http://localhost:8080/email/reset-faculty-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ facultyId, newPassword })
    });

    if (response.ok) {
        alert("Password reset successfully!");
        location.reload();
    }
}
