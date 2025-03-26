const facultyId = new URLSearchParams(window.location.search).get('facultyId');
const studentSemester = localStorage.getItem("studentSemester");  // Fetch from local storage
const facultyDetailsUrl = `http://127.0.0.1:8080/student/viewFaculty/${facultyId}`;
const feedbackForm = document.getElementById('feedbackForm');

let facultySubjects = [];  // Store subjects with semesters

// ✅ Check if facultyId is valid
if (!facultyId) {
    alert("Faculty ID is missing. Redirecting to the faculty list.");
    window.location.href = "feedback.html"; 
} else {
    // ✅ Fetch and display faculty details
    async function loadFacultyDetails() {
        try {
            const response = await fetch(facultyDetailsUrl);
            if (!response.ok) throw new Error('Faculty not found');

            const faculty = await response.json();

            // ✅ Display faculty details
            document.getElementById('facultyId').innerText = faculty.id;
            document.getElementById('facultyName').innerText = faculty.name;
            document.getElementById('facultyImage').src = `data:image/jpeg;base64,${faculty.base64Image}`;

            // Store faculty subjects
            facultySubjects = faculty.subjects;

            // ✅ Automatically fill semester and subject
            autoFillSubjectAndSemester();

        } catch (error) {
            console.error('Error loading faculty details:', error);
            alert('Error loading faculty details: ' + error.message);
        }
    }

    // ✅ Automatically fill the subject and semester fields
    function autoFillSubjectAndSemester() {
        const semesterField = document.getElementById('currentSemester');
        const subjectField = document.getElementById('subject');

        // Find subject based on student semester
        const matchingSubject = facultySubjects.find(sub => sub.semester == studentSemester);

        if (matchingSubject) {
            semesterField.value = studentSemester;  // Auto-fill semester
            subjectField.value = matchingSubject.subject;  // Auto-fill subject
        } else {
            semesterField.value = studentSemester;
            subjectField.value = "No subject found for this semester";
        }
    }

    // ✅ Handle form submission
    feedbackForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const subjectValue = document.getElementById('subject').value;

        if (subjectValue === "No subject found for this semester") {
            alert("No valid subject available for feedback.");
            return;
        }

        const feedbackData = {
            facultyId: parseInt(facultyId, 10),
            subject: subjectValue,
            semester: studentSemester,
            regularity: document.getElementById('regularity').value,
            knowledgeDepth: document.getElementById('knowledgeDepth').value,
            communication: document.getElementById('communication').value,
            engagement: document.getElementById('engagement').value,
            explanationQuality: document.getElementById('explanationQuality').value,
            overallPerformance: document.getElementById('overallPerformance').value,
            additionalComments: document.getElementById('additionalComments').value
        };

        console.log("Submitting Feedback Data:", feedbackData);  // Debugging log

        try {
            const response = await fetch(`http://localhost:8080/student/submitFeedback?facultyId=${facultyId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(feedbackData)
            });

            if (response.ok) {
                alert("Feedback submitted successfully!");
                window.location.href = "feedback.html"; 
            } else {
                alert("Failed to submit feedback.");
            }
        } catch (error) {
            console.error("Error submitting feedback:", error);
            alert("An error occurred while submitting feedback.");
        }
    });

    // ✅ Load faculty details when the page is loaded
    document.addEventListener('DOMContentLoaded', loadFacultyDetails);
}
