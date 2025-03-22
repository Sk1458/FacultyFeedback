// ✅ Admin Login Validation
async function validateAdminLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById("admin-id").value;
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("admin-error-message");
  
    try {
      const response = await fetch("http://localhost:8080/admin/validate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });
  
      const isValid = await response.json();
  
      if (isValid) {
        window.location.href = "admin.html"; // Redirect to admin page
      } else {
        errorMessage.innerText = "Invalid credentials! Please try again.";
      }
    } catch (error) {
      console.error("Login error:", error);
      errorMessage.innerText = "Server error! Please try again later.";
    }
  }
  
// ✅ Toggle Password Visibility
document.getElementById('togglePassword').addEventListener('click', () => {
const passwordField = document.getElementById("password");
passwordField.type = passwordField.type === "password" ? "text" : "password";
});
  
  // 🔥 Forgot Password Flow for Admin
async function sendAdminOTP() {
    const adminId = document.getElementById("forgotAdminId").value;

    if (!adminId) {
        alert("Please enter Admin ID.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/email/send-admin-otp", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username: adminId })
        });

        const message = await response.text();

        if (response.ok) {
            alert("✅ " + "Otp is sent to your mail");
            document.getElementById("step1").style.display = "none";
            document.getElementById("step2").style.display = "block";
        } else {
            // Display the backend error message properly
            alert("❌ Failed: " + message);
        }

    } catch (error) {
        console.error("Error:", error);
        alert("❌ Error sending OTP.");
    }
}


  
// 🔥 Verify OTP with Proper Error Handling
async function verifyAdminOTP() {
    const adminId = document.getElementById("forgotAdminId").value;
    const otp = document.getElementById("otp").value;

    if (!adminId || !otp) {
        alert("Please enter Admin ID and OTP.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/email/verify-admin-otp", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ 
                username: adminId,   // ✅ Ensure you are sending 'username' correctly
                otp: otp 
            })
        });

        const message = await response.text();

        if (response.ok) {
            alert("✅ " + message);
            document.getElementById("step2").style.display = "none";
            document.getElementById("step3").style.display = "block";
        } else {
            // Display backend error message properly
            alert("❌ Verification Failed: " + message);
        }

    } catch (error) {
        console.error("Error:", error);
        alert("❌ Error verifying OTP.");
    }
}

  
// 🔥 Reset Admin Password with Proper Error Handling
async function resetAdminPassword() {
    const adminId = document.getElementById("forgotAdminId").value;  
    const newPassword = document.getElementById("newPassword").value;

    if (!adminId || !newPassword) {
        alert("Please enter Admin ID and New Password.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/email/reset-admin-password", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: adminId,     // ✅ Use 'username' to match the backend parameter
                newPassword: newPassword
            })
        });

        const message = await response.text();

        if (response.ok) {
            alert("✅ " + message);
            location.reload();  // Refresh the page after password reset
        } else {
            alert("❌ Reset Failed: " + message);
        }

    } catch (error) {
        console.error("Error:", error);
        alert("❌ Error resetting password.");
    }
}

  