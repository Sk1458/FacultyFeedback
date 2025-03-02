// Fetch faculty data and populate the table
let allFaculty = [];
window.onload = async function () {
    try {
        const response = await fetch("http://localhost:8080/admin/viewFaculty");
        if (response.ok) {
            allFaculty = await response.json();
            populateTable(allFaculty);
            populateSubjectFilter();
        } 
        else {
            alert("Failed to load faculty data.");
        }
    } 
    catch (error) {
        console.error("Error:", error);
        alert("An error occurred while fetching faculty data.");
    }
};

function populateTable(facultyList) {
    const tableBody = document.getElementById("facultyTableBody");
    tableBody.innerHTML = ""; // Clear the table before populating

    facultyList.forEach(faculty => {
        const row = document.createElement("tr");

        const idCell = document.createElement("td");
        idCell.textContent = faculty.id;
        row.appendChild(idCell);

        const nameCell = document.createElement("td");
        nameCell.textContent = faculty.name;
        row.appendChild(nameCell);

        // Add email
        const emailCell = document.createElement("td");
        emailCell.textContent = faculty.email || "N/A";
        row.appendChild(emailCell);

        // Add mobile number
        const mobileCell = document.createElement("td");
        mobileCell.textContent = faculty.mobileNumber || "N/A";
        row.appendChild(mobileCell);

        // Format subjects & semesters correctly
        const subjectsCell = document.createElement("td");
        if (faculty.subjects && faculty.subjects.length > 0) {
            subjectsCell.textContent = faculty.subjects.map(s => `${s.subject} (Sem ${s.semester})`).join(", ");
        } else {
            subjectsCell.textContent = "N/A";
        }
        row.appendChild(subjectsCell);

        const imageCell = document.createElement("td");
        if (faculty.base64Image) {
            const img = document.createElement("img");
            img.src = `data:image/jpeg;base64,${faculty.base64Image}`;
            img.alt = faculty.name;
            img.width = 50; // Adjust size
            imageCell.appendChild(img);
        }
        row.appendChild(imageCell);

        // Delete Faculty Button
        const deleteFacultyCell = document.createElement("td");
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.className = "delete-btn";
        deleteButton.onclick = () => confirmDelete(faculty.id, faculty.name);
        deleteFacultyCell.appendChild(deleteButton);
        row.appendChild(deleteFacultyCell);

        // Delete Subject-Semester Button
        const deleteSubjectSemesterCell = document.createElement("td");
        const deleteSubSemButton = document.createElement("button");
        deleteSubSemButton.textContent = "Manage Subjects";
        deleteSubSemButton.className = "manage-subject-btn";
        deleteSubSemButton.onclick = () => openSubjectSemesterModal(faculty);
        deleteSubjectSemesterCell.appendChild(deleteSubSemButton);
        row.appendChild(deleteSubjectSemesterCell);

        tableBody.appendChild(row);
    });
}

function populateSubjectFilter() {
    const subjects = new Set();
    allFaculty.forEach(faculty => {
        faculty.subjects.forEach(subjectObj => {
            subjects.add(subjectObj.subject); // Extract only subject name
        });
    });

    const subjectFilter = document.getElementById("subjectFilter");
    subjects.forEach(subject => {
        const option = document.createElement("option");
        option.value = subject;
        option.textContent = subject;
        subjectFilter.appendChild(option);
    });
}

function filterBySubject() {
    const selectedSubject = document.getElementById("subjectFilter").value;
    const filteredFaculty = selectedSubject
        ? allFaculty.filter(faculty =>
            faculty.subjects.some(subjectObj => subjectObj.subject === selectedSubject)
        )
        : allFaculty;
    populateTable(filteredFaculty);
}

function confirmDelete(id, name) {
    const isConfirmed = confirm(`Are you sure you want to delete faculty: ${name}?`);
    if (isConfirmed) {
        deleteFaculty(id);
    }
}

async function deleteFaculty(id) {
    try {
        const response = await fetch(`http://localhost:8080/admin/deleteFaculty/${id}`, {
            method: "DELETE",
        });
        if (response.ok) {
            alert("Faculty deleted successfully!");
            allFaculty = allFaculty.filter(faculty => faculty.id !== id);
            populateTable(allFaculty);
        } else if (response.status === 404) {
            alert("Faculty not found.");
        } else {
            alert("Failed to delete faculty.");
        }
    } 
    catch (error) {
        console.error("Error:", error);
        alert("An error occurred while deleting the faculty.");
    }
}

function openSubjectSemesterModal(faculty) {
    const modal = document.getElementById("subjectSemesterModal");
    const subjectSemesterList = document.getElementById("subjectSemesterList");
    const facultyNameDisplay = document.getElementById("facultyName");

    // Set Faculty Name
    facultyNameDisplay.textContent = `Subjects for ${faculty.name}`;

    // Clear previous entries
    subjectSemesterList.innerHTML = "";

    // Populate subjects & semesters
    faculty.subjects.forEach(({ subject, semester }) => {
        const row = document.createElement("tr");

        const subjectCell = document.createElement("td");
        subjectCell.textContent = subject;
        row.appendChild(subjectCell);

        const semesterCell = document.createElement("td");
        semesterCell.textContent = `Sem ${semester}`;
        row.appendChild(semesterCell);

        // Delete Button
        const actionCell = document.createElement("td");
        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";
        deleteBtn.className = "delete-subject-btn";
        deleteBtn.onclick = () => deleteSubjectSemester(faculty.id, subject, semester);
        actionCell.appendChild(deleteBtn);
        row.appendChild(actionCell);

        subjectSemesterList.appendChild(row);
    });

    // Show the modal
    modal.style.display = "block";
}

function closeModal() {
    document.getElementById("subjectSemesterModal").style.display = "none";
}

// Delete Subject-Semester Function
async function deleteSubjectSemester(facultyId, subject, semester) {
    const confirmDelete = confirm(`Are you sure you want to delete ${subject} (Sem ${semester})?`);
    if (!confirmDelete) return;

    try {
        const response = await fetch(`http://localhost:8080/admin/deleteSubjectSemester/${facultyId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ subject, semester }),
        });

        if (response.ok) {
            alert(`Deleted ${subject} (Sem ${semester}) successfully!`);
            // Refresh faculty list after deletion
            window.location.reload();  
        } else {
            alert("Failed to delete subject-semester.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred while deleting the subject-semester.");
    }
}

// Close modal when clicking outside of it
window.onclick = function(event) {
    const modal = document.getElementById("subjectSemesterModal");
    if (event.target === modal) {
        closeModal();
    }
};