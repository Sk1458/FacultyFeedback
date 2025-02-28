const modal = document.getElementById("facultyModal");

function closeModal() {
    modal.style.display = "none";
}

// Function to dynamically add subject input fields
function addSubjectField() {
    const subjectsContainer = document.getElementById("subjectsContainer");

    const div = document.createElement("div");
    div.classList.add("subject-entry");

    const subjectInput = document.createElement("input");
    subjectInput.type = "text";
    subjectInput.name = "subjects";
    subjectInput.placeholder = "Enter subject name";
    subjectInput.required = true;

    const semesterInput = document.createElement("input");
    semesterInput.type = "number";
    semesterInput.name = "semester";
    semesterInput.placeholder = "Enter semester (1-8)";
    semesterInput.min = 1;
    semesterInput.max = 8;
    semesterInput.required = true;

    const removeBtn = document.createElement("button");
    removeBtn.type = "button";
    removeBtn.textContent = "Remove";
    removeBtn.onclick = function () {
        subjectsContainer.removeChild(div);
    };

    div.appendChild(subjectInput);
    div.appendChild(semesterInput);
    div.appendChild(removeBtn);
    subjectsContainer.appendChild(div);
}

document.getElementById("addFacultyForm").onsubmit = async function (event) {
    event.preventDefault();

    const facultyId = document.getElementById("id").value;
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const mobileNumber = document.getElementById("mobileNumber").value;
    const imageInput = document.getElementById("image");
    const imageFile = imageInput.files[0];

    const subjects = [];
    document.querySelectorAll(".subject-entry").forEach(entry => {
        const subject = entry.children[0].value.trim();
        const semester = entry.children[1].value.trim();
        if (subject && semester) {
            subjects.push({ subject, semester });
        }
    });

    let response;
    try {
        if (imageFile) {
            // If image is uploaded, use FormData
            const formData = new FormData();
            formData.append("id", facultyId);
            formData.append("name", name);
            formData.append("email", email);
            formData.append("mobileNumber", mobileNumber);
            formData.append("image", imageFile);
            formData.append("subjects", JSON.stringify(subjects)); // Ensure backend correctly parses this

            response = await fetch("http://localhost:8080/admin/addFaculty", {
                method: "POST",
                body: formData,
            });
        } else {
            // If no image, send JSON payload
            // response = await fetch("http://localhost:8080/admin/addFaculty", {
            //     method: "POST",
            //     headers: { "Content-Type": "application/json" },
            //     body: JSON.stringify({ id: facultyId, name, subjects }),
            // });

            const formData = new FormData();
            formData.append("id", facultyId);
            formData.append("name", name);
            formData.append("email", email);
            formData.append("mobileNumber", mobileNumber);
            //formData.append("image", imageFile);
            formData.append("subjects", JSON.stringify(subjects)); // Ensure backend correctly parses this

            response = await fetch("http://localhost:8080/admin/addFaculty", {
                method: "POST",
                body: formData,
            });
        }

        if (response.ok) {
            const facultyDetails = {
                id: facultyId,
                name: name,
                email: email,
                mobileNumber: mobileNumber,
                subjects: subjects.map(s => `${s.subject} (Sem ${s.semester})`).join(", "),
            };

            document.getElementById("modalId").textContent = facultyDetails.id;
            document.getElementById("modalName").textContent = facultyDetails.name;
            document.getElementById("modalEmail").textContent = facultyDetails.email;
            document.getElementById("modalMobile").textContent = facultyDetails.mobileNumber;
            document.getElementById("modalSubjects").textContent = facultyDetails.subjects;

            modal.style.display = "block"; // Show the modal
            document.getElementById("addFacultyForm").reset(); // Reset form
            document.getElementById("subjectsContainer").innerHTML = "";
            document.getElementById("errorMessage").innerHTML = ""; // Clear errors
        } else {
            const errorMessage = await response.text();
            document.getElementById("errorMessage").innerHTML = errorMessage || "Failed to add faculty.";
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred. Please check your backend connection.");
    }
};
