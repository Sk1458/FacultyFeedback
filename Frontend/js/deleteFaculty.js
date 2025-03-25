let facultyList = [];

// ✅ Fetch and display faculty data
window.onload = async function () {
    await loadFacultyData();
    populateFilters();
};

// ✅ Load faculty data
async function loadFacultyData() {
    try {
        const response = await fetch("http://localhost:8080/admin/viewFaculty");
        if (response.ok) {
            facultyList = await response.json();
            populateFacultyTable(facultyList);
            populateFilters();
        } else {
            alert("Failed to load faculty data.");
        }
    } catch (error) {
        console.error("Error fetching faculty data:", error);
        alert("An error occurred while loading faculty data.");
    }
}

// ✅ Populate table
function populateFacultyTable(data) {
    const tbody = document.getElementById("facultyTableBody");
    tbody.innerHTML = "";

    data.forEach(faculty => {
        const row = document.createElement("tr");

        const subjectSemesterPairs = faculty.subjects && Array.isArray(faculty.subjects)
            ? faculty.subjects.map(entry => `${entry.subject} - Sem ${entry.semester}`).join(", ")
            : "No subjects";

        row.innerHTML = `
            <td>${faculty.id}</td>
            <td>${faculty.name}</td>
            <td>${faculty.email || "N/A"}</td>
            <td>${faculty.mobileNumber || "N/A"}</td>
            <td>${faculty.campusCode || "N/A"}</td>
            <td>${subjectSemesterPairs}</td>
            <td>
                ${faculty.base64Image ? `<img src="data:image/jpeg;base64,${faculty.base64Image}" alt="${faculty.name}" width="50">` : "No Image"}
            </td>
            <td>
                <button onclick="deleteFaculty(${faculty.id})" class="delete-btn">Delete Faculty</button>
            </td>
            <td>
                <button onclick="openSubjectSemesterModal(${faculty.id})" class="delete-btn">Delete Subjects</button>
            </td>
        `;

        tbody.appendChild(row);
    });
}

// ✅ Populate filters dynamically
function populateFilters() {
    const subjectFilter = document.getElementById("subjectFilter");
    const campusFilter = document.getElementById("campusFilter");

    const subjects = new Set();
    const campuses = new Set();

    facultyList.forEach(faculty => {
        faculty.subjects.forEach(subject => {
            subjects.add(`${subject.subject} - Sem ${subject.semester}`);
        });
        campuses.add(faculty.campusCode || "N/A");
    });

    // Clear previous options
    subjectFilter.innerHTML = `<option value="">All Subjects</option>`;
    campusFilter.innerHTML = `<option value="">All Campuses</option>`;

    // Populate subjects
    subjects.forEach(subject => {
        const option = document.createElement("option");
        option.value = subject;
        option.textContent = subject;
        subjectFilter.appendChild(option);
    });

    // Populate campuses
    campuses.forEach(campus => {
        const option = document.createElement("option");
        option.value = campus;
        option.textContent = campus;
        campusFilter.appendChild(option);
    });
}

// ✅ Filter functionality
function applyFilters() {
    const selectedSubject = document.getElementById("subjectFilter").value;
    const selectedCampus = document.getElementById("campusFilter").value;

    let filteredList = facultyList;

    if (selectedSubject) {
        const [filterSubject, filterSemester] = selectedSubject.split(" - Sem ");
        filteredList = filteredList.filter(faculty =>
            faculty.subjects.some(sub =>
                sub.subject === filterSubject && sub.semester.toString() === filterSemester
            )
        );
    }

    if (selectedCampus) {
        filteredList = filteredList.filter(faculty => faculty.campusCode === selectedCampus);
    }

    populateFacultyTable(filteredList);
}

// ✅ Delete faculty by ID
async function deleteFaculty(facultyId) {
    if (!confirm("Are you sure you want to delete this faculty?")) return;

    try {
        const response = await fetch(`http://localhost:8080/admin/deleteFaculty/${facultyId}`, {
            method: "DELETE"
        });

        if (response.ok) {
            alert("Faculty deleted successfully!");
            await loadFacultyData();
        } else {
            const errorText = await response.text();
            alert("Failed to delete faculty: " + errorText);
        }
    } catch (error) {
        console.error("Error deleting faculty:", error);
        alert("An error occurred while deleting faculty.");
    }
}

// ✅ Open modal to delete subject-semester pairs
function openSubjectSemesterModal(facultyId) {
    const modal = document.getElementById("subjectSemesterModal");
    const subjectSemesterList = document.getElementById("subjectSemesterList");

    const faculty = facultyList.find(f => f.id === facultyId);
    document.getElementById("facultyName").textContent = `Faculty: ${faculty.name}`;

    subjectSemesterList.innerHTML = "";

    faculty.subjects.forEach(subject => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${subject.subject}</td>
            <td>${subject.semester}</td>
            <td>
                <button onclick="deleteSubjectSemester(${facultyId}, '${subject.subject}', ${subject.semester})">Delete</button>
            </td>
        `;
        subjectSemesterList.appendChild(row);
    });

    modal.style.display = "block";
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

// ✅ Close modal
function closeModal() {
    document.getElementById("subjectSemesterModal").style.display = "none";
}
