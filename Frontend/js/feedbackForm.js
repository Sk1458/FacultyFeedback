const facultyId = new URLSearchParams(window.location.search).get('facultyId');
const facultyDetailsUrl = `http://127.0.0.1:8080/student/viewFaculty/${facultyId}`;
const feedbackForm = document.getElementById('feedbackForm');

let facultySubjects = []; // Store subjects with semesters

// Fetch faculty details and populate the page
async function loadFacultyDetails() {
    try {
        const response = await fetch(facultyDetailsUrl);
        if (!response.ok) throw new Error('Faculty not found');

        const faculty = await response.json();

        // Populate faculty details
        document.getElementById('facultyId').innerText = faculty.id;
        document.getElementById('facultyName').innerText = faculty.name;
        document.getElementById('facultyImage').src = `data:image/jpeg;base64,${faculty.base64Image}`;

        // Store faculty subjects and semesters
        facultySubjects = faculty.subjects; 

        // Log subjects to verify they're coming correctly
        console.log('Faculty Subjects:', faculty.subjects);
    } catch (error) {
        console.error('Error loading faculty details:', error);
        alert('Error loading faculty details: ' + error.message);
    }
}

// Function to update the subject dropdown based on selected semester
function updateSubjectDropdown() {
    const selectedSemester = parseInt(document.getElementById('currentSemester').value, 10);
    const selectSubject = document.getElementById('selectSubject');
    
    selectSubject.innerHTML = ''; // Clear existing options

    // Filter subjects based on semester
    const filteredSubjects = facultySubjects.filter(sub => sub.semester === selectedSemester);
    
    if (filteredSubjects.length === 0) {
        selectSubject.innerHTML = '<option value="">No subjects available</option>';
        return;
    }

    // Add filtered subjects to the dropdown
    filteredSubjects.forEach(subject => {
        const option = document.createElement('option');
        option.value = subject.subject;
        option.textContent = subject.subject;
        selectSubject.appendChild(option);
    });
}

// Add event listener for semester selection
document.getElementById('currentSemester').addEventListener('change', updateSubjectDropdown);

// Handle feedback form submission
feedbackForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    const selectedSubject = document.getElementById('selectSubject').value;
    if (!selectedSubject) {
        alert("Please select a subject.");
        return;
    }

    const feedbackData = {
        facultyId: parseInt(facultyId, 10),
        subject: selectedSubject,
        semester: parseInt(document.getElementById('currentSemester').value, 10),
        regularity: document.getElementById('regularity').value,
        knowledgeDepth: document.getElementById('knowledgeDepth').value,
        communication: document.getElementById('communication').value,
        engagement: document.getElementById('engagement').value,
        explanationQuality: document.getElementById('explanationQuality').value,
        overallPerformance: document.getElementById('overallPerformance').value,
        additionalComments: document.getElementById('additionalComments').value
    };

    console.log("Submitting Feedback Data:", feedbackData); // Debugging log

    try {
        const response = await fetch("http://localhost:8080/student/submitFeedback?facultyId=" + facultyId, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(feedbackData)
        });

        if (response.ok) {
            alert("Feedback submitted successfully!");
            window.location.href = "feedback.html"; // Redirect to faculty list
        } else {
            alert("Failed to submit feedback.");
        }
    } catch (error) {
        console.error("Error submitting feedback:", error);
        alert("An error occurred while submitting feedback.");
    }
});

// Load faculty details when the page is loaded
document.addEventListener('DOMContentLoaded', loadFacultyDetails);
