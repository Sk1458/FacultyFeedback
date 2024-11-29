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

        const subjectsCell = document.createElement("td");
        subjectsCell.textContent = faculty.subjects.join(", ");
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
    const uniqueSubjects = new Set();

    data.forEach(faculty => {
        faculty.subjects.forEach(subject => uniqueSubjects.add(subject));
    });

    uniqueSubjects.forEach(subject => {
        const option = document.createElement("option");
        option.value = subject;
        option.textContent = subject;
        subjectFilter.appendChild(option);
    });
}

// Filter faculty by subject
function filterFacultyBySubject() {
    const selectedSubject = document.getElementById("subjectFilter").value;

    if (selectedSubject === "") {
        populateTable(facultyList); // Show all if no subject is selected
    } else {
        const filteredList = facultyList.filter(faculty =>
            faculty.subjects.includes(selectedSubject)
        );
        populateTable(filteredList);
    }
}