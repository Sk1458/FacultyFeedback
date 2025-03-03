let selectedFacultyId = null;
    
// Fetch and display faculty data
async function loadFacultyData() {
    try {
        const response = await fetch("http://localhost:8080/admin/viewFaculty");
        if (response.ok) {
            const facultyList = await response.json();
            populateFacultyTable(facultyList);
        } 
        else {
            alert("Failed to load faculty data.");
        }
    } 
    catch (error) {
        console.error("Error fetching faculty data:", error);
        alert("An error occurred while loading faculty data.");
    }
}
    
function populateFacultyTable(facultyList) {
    const tbody = document.getElementById("facultyTableBody");
    tbody.innerHTML = "";

    facultyList.forEach(faculty => {
        const row = document.createElement("tr");

        // Format subjects with semesters
        const subjectSemesterPairs = faculty.subjects.map(sub => `${sub.subject} (Sem ${sub.semester})`).join(", ");


        row.innerHTML = `
            <td>${faculty.id}</td>
            <td>${faculty.name}</td>
            <td>${subjectSemesterPairs}</td>
            <td>
                ${faculty.base64Image ? `<img src="data:image/jpeg;base64,${faculty.base64Image}" alt="${faculty.name}" width="50">` : "No Image"}
            </td>
            <td>
                <button class="update-btn" onclick="redirectToFeedbackPage(${faculty.id})">Give Feedback</button>
            </td>
        `;
            
        tbody.appendChild(row);
    });

}

function redirectToFeedbackPage(facultyId) {
    window.location.href = `feedbackForm.html?facultyId=${facultyId}`;
}

// Redirect to login page
function logout() {
    window.location.href = "loginPage.html"; // Replace "login.html" with the actual login page URL
}

// Initial load
loadFacultyData();