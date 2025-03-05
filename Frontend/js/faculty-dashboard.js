document.addEventListener("DOMContentLoaded", fetchOverallScore);

function fetchOverallScore() {
    const facultyId = sessionStorage.getItem("facultyId"); 
    if (!facultyId) {
        console.error("Faculty ID not found in session storage.");
        return;
    }

    fetch(`http://localhost:8080/faculty/performance?facultyId=${facultyId}`)
        .then(response => response.json())
        .then(data => {
            console.log("Fetched Data:", data);

            if (!data || typeof data !== "object") {
                console.error("Invalid response format:", data);
                return;
            }

            if (data.message) { 
                document.getElementById("suggestionsBox").innerHTML = `<p>No suggestions available.</p>`;
                return;
            }

            generateSuggestions(data);
        })
        .catch(error => console.error("Error fetching score:", error));
}

function generateSuggestions(data) {
    const suggestionBox = document.getElementById("suggestionsBox");
    if (!suggestionBox) {
        console.error("Suggestions box not found.");
        return;
    }
    suggestionBox.innerHTML = ""; 

    const improvementAreas = [];

    if (data.regularity < 3.3) improvementAreas.push({ name: "Regularity", color: "danger" });
    if (data.knowledgeDepth < 3.3) improvementAreas.push({ name: "Knowledge Depth", color: "warning" });
    if (data.communication < 3.3) improvementAreas.push({ name: "Communication", color: "info" });
    if (data.engagement < 3.3) improvementAreas.push({ name: "Engagement", color: "primary" });
    if (data.explanationQuality < 3.3) improvementAreas.push({ name: "Explanation Quality", color: "secondary" });

    if (improvementAreas.length === 0) {
        suggestionBox.innerHTML = `<p class="text-success">Great job! No improvements needed.</p>`;
        return;
    }

    let badgesHTML = `<p>Areas to improve:</p>`;
    improvementAreas.forEach(area => {
        badgesHTML += `<span class="badge bg-${area.color} m-1 p-2">${area.name}</span>`;
    });

    suggestionBox.innerHTML = badgesHTML;
}

// Sidebar toggle function
function toggleSidebar() {
    const sidebar = document.getElementById("suggestionsSidebar");
    const sidebarHandle = document.querySelector(".sidebar-handle");

    sidebar.classList.toggle("active");

    // Move the handle with the sidebar
    if (sidebar.classList.contains("active")) {
        sidebarHandle.style.right = "300px"; // Moves with sidebar
    } else {
        sidebarHandle.style.right = "0"; // Reset to default
    }
}
