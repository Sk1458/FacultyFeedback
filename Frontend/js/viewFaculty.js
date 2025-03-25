let facultyList = [];

// 🚀 Fetch faculty data and populate the table & filter dropdowns
window.onload = async function () {
    try {
        const response = await fetch("http://localhost:8080/admin/viewFaculty");
        if (response.ok) {
            facultyList = await response.json();
            
            // Populate the table and dropdowns
            populateTable(facultyList);
            populateCampusDropdown(facultyList);
            populateSubjectDropdown(facultyList);
        } else {
            alert("Failed to load faculty data.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred while fetching faculty data.");
    }
};

// 🚀 Populate faculty table with data
function populateTable(data) {
    const tableBody = document.getElementById("facultyTableBody");
    tableBody.innerHTML = ""; // Clear existing rows

    data.forEach(faculty => {
        const row = document.createElement("tr");

        // ID, Name, Email, Mobile
        row.innerHTML = `
            <td>${faculty.id}</td>
            <td>${faculty.name}</td>
            <td>${faculty.email || "N/A"}</td>
            <td>${faculty.mobileNumber || "N/A"}</td>
            <td>${faculty.campusCode || "N/A"}</td>  <!-- ✅ Display Campus Code -->
        `;

        // Subjects
        const subjectsCell = document.createElement("td");
        if (faculty.subjects && Array.isArray(faculty.subjects)) {
            subjectsCell.textContent = faculty.subjects
                .map(entry => `${entry.subject} - Semester ${entry.semester}`)
                .join(", ");
        } else {
            subjectsCell.textContent = "No subjects assigned";
        }
        row.appendChild(subjectsCell);

        // Image
        const imageCell = document.createElement("td");
        if (faculty.base64Image) {
            const img = document.createElement("img");
            img.src = `data:image/jpeg;base64,${faculty.base64Image}`;
            img.alt = faculty.name;
            img.width = 50;
            imageCell.appendChild(img);
        }
        row.appendChild(imageCell);

        tableBody.appendChild(row);
    });
}

// 🚀 Populate Campus Dropdown with unique campus codes
function populateCampusDropdown(data) {
    const campusFilter = document.getElementById("campusFilter");
    const uniqueCampuses = new Set();

    data.forEach(faculty => {
        if (faculty.campusCode) {
            uniqueCampuses.add(faculty.campusCode);
        }
    });

    campusFilter.innerHTML = '<option value="">All Campuses</option>';
    uniqueCampuses.forEach(campus => {
        const option = document.createElement("option");
        option.value = campus;
        option.textContent = campus;
        campusFilter.appendChild(option);
    });
}

// 🚀 Populate Subject Dropdown
function populateSubjectDropdown(data) {
    const subjectFilter = document.getElementById("subjectFilter");
    const uniqueSubjectSemesters = new Set();

    data.forEach(faculty => {
        faculty.subjects.forEach(entry => {
            const subjectSemesterPair = `${entry.subject} - Semester ${entry.semester}`;
            uniqueSubjectSemesters.add(subjectSemesterPair);
        });
    });

    subjectFilter.innerHTML = '<option value="">All Subjects</option>';
    uniqueSubjectSemesters.forEach(pair => {
        const option = document.createElement("option");
        option.value = pair;
        option.textContent = pair;
        subjectFilter.appendChild(option);
    });
}

// 🚀 Apply Combined Filters (Campus + Subject)
function applyFilters() {
    const selectedCampus = document.getElementById("campusFilter").value;
    const selectedSubject = document.getElementById("subjectFilter").value;

    const [selectedSub, selectedSem] = selectedSubject.split(" - Semester ") || [];

    const filteredList = facultyList.filter(faculty => {
        const campusMatch = !selectedCampus || faculty.campusCode === selectedCampus;

        const subjectMatch = !selectedSubject || faculty.subjects.some(entry =>
            entry.subject === selectedSub && entry.semester == selectedSem
        );

        return campusMatch && subjectMatch;
    });

    populateTable(filteredList);
}
