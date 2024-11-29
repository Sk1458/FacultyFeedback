function openAdminLoginForm() {
    document.getElementById("admin-login-form").style.display = "block";
    document.getElementById("student-login-form").style.display = "none"; // Hide student form
}

function openStudentLoginForm() {
    document.getElementById("student-login-form").style.display = "block";
    document.getElementById("admin-login-form").style.display = "none"; // Hide admin form
}

async function validateAdminLogin(event) {
    event.preventDefault(); // Prevent form submission

    const username = document.getElementById("admin-username").value;
    const password = document.getElementById("admin-password").value;

    const response = await fetch("http://localhost:8080/admin/validate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    });

    const isValid = await response.json();

    if (isValid) {
        window.location.href = "admin.html"; // Redirect to admin page
    } else {
        document.getElementById("admin-error-message").innerText = "Invalid credentials! Please try again.";
    }
}

async function validateStudentLogin(event) {
    event.preventDefault(); // Prevent form submission

    const studentId = document.getElementById("student-id").value;
    const password = document.getElementById("student-password").value;

    const response = await fetch("http://localhost:8080/student/validate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ studentId, password })
    });

    const isValid = await response.json();

    if (isValid) {
        window.location.href = "feedback.html"; // Redirect to student dashboard
    } else {
        document.getElementById("student-error-message").innerText = "Invalid credentials! Please try again.";
    }
}