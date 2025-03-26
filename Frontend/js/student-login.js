async function validateStudentLogin(event) {
    event.preventDefault();

    const studentId = document.getElementById("student-id").value;
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("student-error-message");

    try {
        const response = await fetch("http://localhost:8080/student/validate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ studentId, password })
        });

        const isValid = await response.json();
        if (isValid) {
            // Store roll number in local storage
            localStorage.setItem("rollNumber", studentId);
            
            window.location.href = "feedback.html";
        } else {
            errorMessage.innerText = "Invalid credentials! Please try again.";
        }
    } catch (error) {
        console.error("Login error:", error);
        errorMessage.innerText = "Server error! Please try again later.";
    }
}

document.getElementById('togglePassword').addEventListener('click', () => {
    const passwordField = document.getElementById("password");
    passwordField.type = passwordField.type === "password" ? "text" : "password";
});

// 🔥 Forgot Password Functions
async function sendOTP() {
    const rollNumber = document.getElementById("forgotRollNumber").value;
    const response = await fetch("http://localhost:8080/email/send-otp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ rollNumber })
    });

    if (response.ok) {
        document.getElementById("step1").style.display = "none";
        document.getElementById("step2").style.display = "block";
    }
}

async function verifyOTP() {
    const rollNumber = document.getElementById("forgotRollNumber").value;
    const otp = document.getElementById("otp").value;

    const response = await fetch("http://localhost:8080/email/verify-otp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ rollNumber, otp })
    });

    if (response.ok) {
        document.getElementById("step2").style.display = "none";
        document.getElementById("step3").style.display = "block";
    }
}

async function resetPassword() {
    const rollNumber = document.getElementById("forgotRollNumber").value;
    const newPassword = document.getElementById("newPassword").value;

    const response = await fetch("http://localhost:8080/email/reset-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ rollNumber, newPassword })
    });

    if (response.ok) {
        alert("Password reset successfully!");
        location.reload();
    }
}
