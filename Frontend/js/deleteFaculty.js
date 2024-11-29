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

        const subjectsCell = document.createElement("td");
        subjectsCell.textContent = faculty.subjects.join(", ");
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

        const actionCell = document.createElement("td");
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.className = "delete-btn";
        deleteButton.onclick = () => confirmDelete(faculty.id, faculty.name);
        actionCell.appendChild(deleteButton);
        row.appendChild(actionCell);

        tableBody.appendChild(row);
    });
}

function populateSubjectFilter() {
    const subjects = new Set(allFaculty.flatMap(faculty => faculty.subjects));
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
        ? allFaculty.filter(faculty => faculty.subjects.includes(selectedSubject))
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