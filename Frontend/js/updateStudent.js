// ✅ Wait until DOM is fully loaded
document.addEventListener("DOMContentLoaded", () => {

    const bulkForm = document.getElementById("semesterUpdateForm");
    const bulkMessageDiv = document.getElementById("bulkMessage");

    const singleForm = document.getElementById("singleStudentForm");
    const singleMessageDiv = document.getElementById("singleMessage");

    // ✅ Bulk Semester Update Submission
    bulkForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        const year = document.getElementById("year").value;
        const semester = document.getElementById("semester").value;

        // Display loading message
        bulkMessageDiv.textContent = "Updating... Please wait.";
        bulkMessageDiv.style.color = "blue";

        try {
            const response = await fetch(`http://localhost:8080/admin/updateStudentSemester?year=${year}&semester=${semester}`, {
                method: "PUT"
            });

            if (response.ok) {
                const result = await response.text();
                bulkMessageDiv.textContent = result;
                bulkMessageDiv.style.color = "green";
            } else {
                const errorText = await response.text();
                bulkMessageDiv.textContent = `Error: ${errorText}`;
                bulkMessageDiv.style.color = "red";
            }

        } catch (error) {
            bulkMessageDiv.textContent = "Failed to connect to the server.";
            bulkMessageDiv.style.color = "red";
            console.error("Error:", error);
        }
    });

    // ✅ Single Student Semester Update Submission (Fixed ID reference)
    singleForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        const rollNumber = document.getElementById("rollNumber").value;
        const newSemester = document.getElementById("singleSemester").value;   // ✅ Fixed the ID reference

        singleMessageDiv.textContent = "Updating... Please wait.";
        singleMessageDiv.style.color = "blue";

        try {
            const response = await fetch(`http://localhost:8080/admin/updateStudentSemesterByRollNumber?rollNumber=${rollNumber}&semester=${newSemester}`, {
                method: "PUT"
            });

            if (response.ok) {
                const result = await response.text();
                singleMessageDiv.textContent = result;
                singleMessageDiv.style.color = "green";
            } else {
                const errorText = await response.text();
                singleMessageDiv.textContent = `Error: ${errorText}`;
                singleMessageDiv.style.color = "red";
            }

        } catch (error) {
            singleMessageDiv.textContent = "Failed to connect to the server.";
            singleMessageDiv.style.color = "red";
            console.error("Error:", error);
        }
    });

});
