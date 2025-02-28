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
    const tbody = document.getElementById("facultyTable").querySelector("tbody");
    tbody.innerHTML = "";

    facultyList.forEach(faculty => {
        const row = document.createElement("tr");
        
        // Make sure 'faculty.subjects' is an array of objects with 'subject' and 'semester'
        const subjectSemesterPairs = faculty.subjects && Array.isArray(faculty.subjects)
            ? faculty.subjects.map(subject => `${subject.subject} - ${subject.semester}`).join(", ")
            : "No subjects";  // Handle case where there are no subjects
        
        row.innerHTML = `
            <td>${faculty.id}</td>
            <td>${faculty.name}</td>  
            <td>${faculty.email}</td>
            <td>${faculty.mobileNumber}</td> 
            <td>${subjectSemesterPairs}</td>
            <td>
                ${faculty.base64Image ? `<img src="data:image/jpeg;base64,${faculty.base64Image}" alt="${faculty.name}" width="50">` : "No Image"}
            </td>
            <td>
                <button class="update-btn" onclick="openUpdateForm(${faculty.id})">Update</button>
            </td>
        `;

        tbody.appendChild(row);
    });
}

// Open the update form
function openUpdateForm(facultyId) {
    selectedFacultyId = facultyId;

    // Set the faculty ID header
    const facultyIdHeader = document.getElementById("facultyIdHeader");
    facultyIdHeader.textContent = `Updating Faculty ID: ${facultyId}`;

    document.getElementById("updateForm").style.display = "block";
}

// Close the update form
function closeUpdateForm() {
    document.getElementById("updateFacultyForm").reset();
    document.getElementById("updateForm").style.display = "none";
}

// Submit updates
document.getElementById("updateFacultyForm").onsubmit = async function (event) {
    event.preventDefault();

    const formData = new FormData();
    formData.append("id", selectedFacultyId);
    const name = document.getElementById("updateName").value.trim();
    if (name) formData.append("name", name);
    const addSubjects = document.getElementById("updateAddSubjects").value.trim();
    if (addSubjects) formData.append("addSubjects", addSubjects.split(",").map(s => s.trim()));
    const removeSubjects = document.getElementById("updateRemoveSubjects").value.trim();
    if (removeSubjects) formData.append("removeSubjects", removeSubjects.split(",").map(s => s.trim()));
    const image = document.getElementById("updateImage").files[0];
    if (image) formData.append("image", image);

    try {
        const response = await fetch("http://localhost:8080/admin/updateFaculty", {
            method: "PUT",
            body: formData,
        });

        if (response.ok) {
            alert("Faculty updated successfully!");
            closeUpdateForm();
            loadFacultyData(); // Refresh table
        } 
        else {
            const errorText = await response.text();
            alert("Failed to update faculty: " + errorText);
        }
    } 
    catch (error) {
        console.error("Error updating faculty:", error);
        alert("An error occurred while updating faculty.");
    }
};

// Initial load
loadFacultyData();