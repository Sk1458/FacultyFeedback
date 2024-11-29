const modal = document.getElementById("facultyModal");

function closeModal() {
    modal.style.display = "none";
}

document.getElementById("addFacultyForm").onsubmit = async function (event) {
    event.preventDefault();

    const formData = new FormData();
    formData.append("id", document.getElementById("id").value);
    formData.append("name", document.getElementById("name").value);
    formData.append("subjects", document.getElementById("subjects").value.split(",").map(s => s.trim()));
    formData.append("image", document.getElementById("image").files[0]);

    try {
        const response = await fetch("http://localhost:8080/admin/addFaculty", {
        method: "POST",
        body: formData,
        });

        if (response.ok) {
            const facultyDetails = {
                id: document.getElementById("id").value,
                name: document.getElementById("name").value,
                subjects: document.getElementById("subjects").value,
            };

            document.getElementById("modalId").textContent = facultyDetails.id;
            document.getElementById("modalName").textContent = facultyDetails.name;
            document.getElementById("modalSubjects").textContent = facultyDetails.subjects;

            modal.style.display = "block"; // Show the modal

            document.getElementById("addFacultyForm").reset(); // Reset form
            document.getElementById("errorMessage").innerHTML = ""; // Clear errors
        } 
        else {
            const errorMessage = await response.text();
            document.getElementById("errorMessage").innerHTML = errorMessage || "Failed to add faculty.";
        }
    }
    catch (error) {
        console.error("Error:", error);
        alert("An error occurred. Please check your backend connection.");
    }
};