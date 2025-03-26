document.addEventListener("DOMContentLoaded", async () => {
    await fetchStudentDetails();  // ✅ Fetch student details first
    fetchFacultyList();           // ✅ Then load the faculty list
});

// ✅ Function to fetch student details and store them in local storage
async function fetchStudentDetails() {
    const rollNumber = localStorage.getItem("rollNumber");
    
    if (!rollNumber) {
        console.error("No roll number found in local storage.");
        window.location.href = "loginPage.html";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/student/getStudentDetails?rollNumber=${rollNumber}`);
        
        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }

        const studentData = await response.json();
        
        // ✅ Store student's semester and campus code in localStorage
        localStorage.setItem("studentSemester", studentData.semester);
        localStorage.setItem("campusCode", studentData.rollNumber.substring(2, 4));  // Extract campus code from roll number

        console.log("Student details saved:", studentData);
        
    } catch (error) {
        console.error("Error fetching student details:", error);
        alert("Failed to fetch student details. Please try again.");
    }
}

// ✅ Function to fetch faculty list
function fetchFacultyList() {
    const rollNumber = localStorage.getItem("rollNumber"); 

    if (!rollNumber) {
        console.error("No roll number found in local storage.");
        window.location.href = "loginPage.html";
        return;
    }

    fetch(`http://localhost:8080/student/faculty-list?rollNumber=${rollNumber}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            populateFacultyTable(data);
        })
        .catch(error => {
            console.error("Error fetching faculty list:", error);
            alert("Failed to fetch faculty list. Please try again.");
        });
}

// ✅ Function to populate faculty table
function populateFacultyTable(facultyList) {
    const tableBody = document.getElementById("facultyTableBody");
    tableBody.innerHTML = "";  

    if (facultyList.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="5">No faculties available for your semester and campus.</td></tr>`;
        return;
    }

    facultyList.forEach(faculty => {
        const row = document.createElement("tr");

        // Faculty ID Cell
        const idCell = document.createElement("td");
        idCell.textContent = faculty.id;  
        row.appendChild(idCell);

        // Name Cell
        const nameCell = document.createElement("td");
        nameCell.textContent = faculty.name;
        row.appendChild(nameCell);

        // Subjects Cell
        const subjectsCell = document.createElement("td");
        subjectsCell.innerHTML = faculty.subjects && faculty.subjects.length > 0
            ? faculty.subjects.map(sub => `<div>${sub.subject} (Sem ${sub.semester})</div>`).join("")
            : "No subjects assigned";
        row.appendChild(subjectsCell);

        // Image Cell
        const imgCell = document.createElement("td");
        if (faculty.image) {
            const img = document.createElement("img");
            img.src = `data:image/jpeg;base64,${faculty.image}`;
            img.alt = "Faculty Image";
            img.style.width = "100px";
            img.style.height = "100px";
            imgCell.appendChild(img);
        } else {
            imgCell.textContent = "No Image";
        }
        row.appendChild(imgCell);

        // Action Cell
        const actionCell = document.createElement("td");
        const btn = document.createElement("button");
        btn.textContent = "Give Feedback";
        btn.onclick = () => redirectToFeedbackPage(faculty.id);
        actionCell.appendChild(btn);
        row.appendChild(actionCell);

        tableBody.appendChild(row);
    });
}

// ✅ Function to redirect to feedback form
function redirectToFeedbackPage(facultyId) {
    if (!facultyId) {
        console.error("Faculty ID is missing.");
        alert("Failed to get faculty ID.");
        return;
    }
    
    // ✅ Redirect with faculty ID
    window.location.href = `feedbackForm.html?facultyId=${facultyId}`;
}

// ✅ Logout function
function logout() {
    localStorage.removeItem("rollNumber");
    localStorage.removeItem("studentSemester");
    localStorage.removeItem("campusCode");  // ✅ Remove campus code on logout
    window.location.href = "loginPage.html";
}
