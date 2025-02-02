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
        facultySubjects = faculty.subjects; // Assuming subjects contain { name, semester } structure

        // Log subjects to verify they're coming correctly
        console.log('Faculty Subjects:', faculty.subjects);  // Added this log

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
        option.value = subject.subject;  // Fix: Using 'subject' instead of 'name'
        option.textContent = subject.subject;  // Fix: Correct property
        selectSubject.appendChild(option);
    });
}

// Add event listener for semester selection
document.getElementById('currentSemester').addEventListener('change', updateSubjectDropdown);

// Load faculty details when the page is loaded
document.addEventListener('DOMContentLoaded', loadFacultyDetails);
