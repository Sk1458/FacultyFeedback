const facultyId = new URLSearchParams(window.location.search).get('facultyId');
const facultyDetailsUrl = `http://127.0.0.1:8080/student/viewFaculty/${facultyId}`;
const feedbackForm = document.getElementById('feedbackForm');
    
// Fetch faculty details and populate the page
async function loadFacultyDetails() {
    try {
        const response = await fetch(facultyDetailsUrl);
        if (!response.ok) throw new Error('Faculty not found');

        const faculty = await response.json();

        // Populate faculty details
        document.getElementById('facultyId').innerText = faculty.id;
        document.getElementById('facultyName').innerText = faculty.name;
        document.getElementById('facultySubjects').innerText = faculty.subjects.join(', ');
        document.getElementById('facultyImage').src = `data:image/jpeg;base64,${faculty.base64Image}`;

        // Populate subject dropdown
        const selectSubject = document.getElementById('selectSubject');
        selectSubject.innerHTML = ''; // Clear any existing options
        faculty.subjects.forEach(subject => {
            const option = document.createElement('option');
            option.value = subject;
            option.textContent = subject;
            selectSubject.appendChild(option);
        });
    } 
    catch (error) {
        console.error('Error loading faculty details:', error);
        alert('Error loading faculty details: ' + error.message);
    }
}
    
// Handle feedback form submission
feedbackForm.addEventListener('submit', async (event) => {
    event.preventDefault(); // Prevent form from refreshing the page

    // Prepare feedback data
    const feedbackData = {
        subject: document.getElementById('selectSubject').value,
        semester: parseInt(document.getElementById('currentSemester').value, 10),
        regularity: document.getElementById('regularityToClass').value,
        knowledgeDepth: document.getElementById('knowledgeDepth').value,
        communication: document.getElementById('communication').value,
        engagement: document.getElementById('engagement').value,
        explanationQuality: document.getElementById('explanationQuality').value,
        overallPerformance: document.getElementById('overallPerformance').value,
        additionalComments: document.getElementById('additionalComments').value,
    };
    
    try {
        // Include facultyId as a query parameter in the POST request URL
        const response = await fetch(`http://127.0.0.1:8080/student/submitFeedback?facultyId=${facultyId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(feedbackData),
        });
    
        if (!response.ok) throw new Error('Failed to submit feedback');

        alert('Feedback submitted successfully!');
        feedbackForm.reset(); // Clear the form after successful submission
    } 
    catch (error) {
        console.error('Error submitting feedback:', error);
        alert('Error submitting feedback: ' + error.message);
    }
});
    
// Load faculty details when the page is loaded
document.addEventListener('DOMContentLoaded', loadFacultyDetails);