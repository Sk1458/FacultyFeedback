let facultyList = [];

// Fetch faculty data and populate the table and filter dropdown
window.onload = async function() {
    try {
        const response = await fetch("http://localhost:8080/admin/viewFaculty");
        if (response.ok) {
            facultyList = await response.json();
            populateTable(facultyList);
            populateSubjectDropdown(facultyList);
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

// Populate table with faculty data
function populateTable(data) {
    const tableBody = document.getElementById("facultyTableBody");
    tableBody.innerHTML = ""; // Clear existing rows

    data.forEach(faculty => {
        const row = document.createElement("tr");

        // Add faculty data
        const idCell = document.createElement("td");
        idCell.textContent = faculty.id;
        row.appendChild(idCell);

        const nameCell = document.createElement("td");
        nameCell.textContent = faculty.name;
        row.appendChild(nameCell);

        const emailCell = document.createElement("td");
        emailCell.textContent = faculty.email ? faculty.email : "N/A";
        row.appendChild(emailCell);

        const mobileCell = document.createElement("td");
        mobileCell.textContent = faculty.mobileNumber ? faculty.mobileNumber : "N/A";
        row.appendChild(mobileCell);

        // Subjects (Show subject-semester pairs)
        const subjectsCell = document.createElement("td");
        if (faculty.subjects && Array.isArray(faculty.subjects)) {
            subjectsCell.textContent = faculty.subjects
                .map(entry => `${entry.subject} - Semester ${entry.semester}`)
                .join(", ");
        } else {
            subjectsCell.textContent = "No subjects assigned";
        }
        row.appendChild(subjectsCell);

        const imageCell = document.createElement("td");
        if (faculty.base64Image) {
            const img = document.createElement("img");
            img.src = `data:image/jpeg;base64,${faculty.base64Image}`;
            img.alt = faculty.name;
            img.width = 50; // Adjust image size
            imageCell.appendChild(img);
        }
        row.appendChild(imageCell);

        tableBody.appendChild(row);
    });
}

// Populate dropdown with unique subjects
function populateSubjectDropdown(data) {
    const subjectFilter = document.getElementById("subjectFilter");
    const uniqueSubjectSemesters = new Set();

    data.forEach(faculty => {
        faculty.subjects.forEach(entry => {
            const subjectSemesterPair = `${entry.subject} - Semester ${entry.semester}`;
            uniqueSubjectSemesters.add(subjectSemesterPair);
        });
    });

    // Clear previous options
    subjectFilter.innerHTML = '<option value="">All Subjects</option>';

    uniqueSubjectSemesters.forEach(pair => {
        const option = document.createElement("option");
        option.value = pair;  // Store "Math - Semester 3"
        option.textContent = pair;
        subjectFilter.appendChild(option);
    });
}

// Filter faculty by subject and semester
function filterFacultyBySubject() {
    const selectedValue = document.getElementById("subjectFilter").value;

    if (selectedValue === "") {
        populateTable(facultyList); // Show all faculty if no filter is applied
    } else {
        const [selectedSubject, selectedSemester] = selectedValue.split(" - Semester ");
        const filteredList = facultyList.filter(faculty =>
            faculty.subjects.some(entry => 
                entry.subject === selectedSubject && entry.semester == selectedSemester
            )
        );
        populateTable(filteredList);
    }
}