let selectedFacultyId = null;

// Fetch and display faculty data
async function loadFacultyData() {
    try {
        const response = await fetch("http://localhost:8080/admin/viewFaculty");
        if (response.ok) {
            const facultyList = await response.json();
            populateFacultyTable(facultyList);
        } else {
            alert("Failed to load faculty data.");
        }
    } catch (error) {
        console.error("Error fetching faculty data:", error);
        alert("An error occurred while loading faculty data.");
    }
}

function populateFacultyTable(facultyList) {
    const tbody = document.getElementById("facultyTable").querySelector("tbody");
    tbody.innerHTML = "";

    facultyList.forEach(faculty => {
        const row = document.createElement("tr");

        const subjectSemesterPairs = faculty.subjects && Array.isArray(faculty.subjects)
            ? faculty.subjects.map(entry => `${entry.subject || 'N/A'} - ${entry.semester || 'N/A'}`).join(", ")
            : "No subjects";

        row.innerHTML = `
            <td>${faculty.id}</td>
            <td>${faculty.name}</td>
            <td>${faculty.email}</td>
            <td>${faculty.mobileNumber}</td>
            <td>${faculty.campusCode || "N/A"}</td> <!-- ✅ Display Campus Code -->
            <td>${subjectSemesterPairs}</td>
            <td>
                ${faculty.base64Image ? `<img src="data:image/jpeg;base64,${faculty.base64Image}" alt="${faculty.name}" width="50">` : "No Image"}
            </td>
            <td>
                <button class="update-btn" onclick="openUpdateForm(${faculty.id}, '${faculty.campusCode || ''}')">Update</button>
            </td>
        `;

        tbody.appendChild(row);
    });
}

// Open the update form with pre-filled values
function openUpdateForm(facultyId, campusCode) {
    selectedFacultyId = facultyId;

    document.getElementById("facultyIdHeader").textContent = `Updating Faculty ID: ${facultyId}`;

    // Reset the form fields
    document.getElementById("updateFacultyForm").reset();
    document.getElementById("updateSubjectsContainer").innerHTML = ""; 
    document.getElementById("updateForm").style.display = "block";

    // Pre-fill campus code
    document.getElementById("updateCampusCode").value = campusCode || "";
}

// Close the update form
function closeUpdateForm() {
    document.getElementById("updateFacultyForm").reset();
    document.getElementById("updateSubjectsContainer").innerHTML = "";
    document.getElementById("updateForm").style.display = "none";
}

// Function to add a subject-semester pair
function addSubjectField(subject = "", semester = "") {
    const container = document.getElementById("updateSubjectsContainer");

    const subjectDiv = document.createElement("div");
    subjectDiv.classList.add("subject-pair");

    subjectDiv.innerHTML = `
        <input type="text" placeholder="Subject" class="subject-input" value="${subject}">
        <input type="number" placeholder="Semester" class="semester-input" min="1" value="${semester}">
        <button type="button" class="remove-subject-btn">Remove</button>
    `;

    container.appendChild(subjectDiv);

    subjectDiv.querySelector(".remove-subject-btn").addEventListener("click", () => {
        container.removeChild(subjectDiv);
    });
}

// Add new subject-semester pair dynamically
document.getElementById("addSubjectButton").addEventListener("click", () => {
    addSubjectField();
});

// Submit updates
document.getElementById("updateFacultyForm").onsubmit = async function (event) {
    event.preventDefault();

    const formData = new FormData();
    formData.append("id", selectedFacultyId);

    const name = document.getElementById("updateName").value.trim();
    if (name) formData.append("name", name);

    const mobileNumber = document.getElementById("updateMobileNumber").value.trim();
    if (mobileNumber) formData.append("mobileNumber", mobileNumber);

    const email = document.getElementById("updateEmail").value.trim();
    if (email) formData.append("email", email);

    const campusCode = document.getElementById("updateCampusCode").value.trim();
    if (campusCode) formData.append("campusCode", campusCode);  // ✅ Add Campus Code

    const image = document.getElementById("updateImage").files[0];
    if (image) formData.append("image", image);

    // Collect subjects and semesters
    const subjectsArray = [];
    document.querySelectorAll(".subject-pair").forEach(pair => {
        const subject = pair.querySelector(".subject-input").value.trim();
        const semester = pair.querySelector(".semester-input").value.trim();

        if (subject && semester) {
            subjectsArray.push({ subject, semester: parseInt(semester) });
        }
    });

    if (subjectsArray.length > 0) {
        formData.append("subjectsJson", JSON.stringify(subjectsArray)); // Send as JSON string
    }

    // ✅ Log to check what is being sent
    console.log("Final Data Being Sent:", Object.fromEntries(formData));

    try {
        const response = await fetch("http://localhost:8080/admin/updateFaculty", {
            method: "PUT",
            body: formData,
        });

        if (response.ok) {
            alert("Faculty updated successfully!");
            closeUpdateForm();
            loadFacultyData(); 
        } else {
            const errorText = await response.text();
            alert("Failed to update faculty: " + errorText);
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred while updating faculty.");
    }
};

// Initial load
loadFacultyData();
