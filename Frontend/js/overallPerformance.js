document.addEventListener("DOMContentLoaded", fetchOverallScore);
let performanceChart = null; // Store chart instance

// Fetch overall performance score from backend
function fetchOverallScore() {
    const facultyId = sessionStorage.getItem("facultyId"); // Assuming faculty ID is stored after login
    if (!facultyId) {
        console.error("Faculty ID not found in session storage.");
        return;
    }

    console.log("Faculty ID from session:", facultyId); // Debugging Log

    fetch(`http://localhost:8080/faculty/performance?facultyId=${facultyId}`)
        .then(response => response.json())
        .then(data => {
            console.log("Fetched Data:", data); // Debugging Log

            if (!data || typeof data !== "object") {
                console.error("Invalid response format:", data);
                return;
            }

            if (data.message) { // Handle no feedback case
                document.getElementById("overallScore").innerText = "No Feedback Available";
                return;
            }

            if (typeof data.overallScore === "number") {
                document.getElementById("overallScore").innerText = data.overallScore.toFixed(2);
            } else {
                console.error("Invalid overall score received:", data.overallScore);
            }

            generateChart("bar", data); // Default to bar chart
        })
        .catch(error => console.error("Error fetching score:", error));
}

// Generate chart
function generateChart(type, data) {
    const ctx = document.getElementById("performanceChart").getContext("2d");

    if (performanceChart !== null) {
        performanceChart.destroy();
    }

    performanceChart = new Chart(ctx, {
        type: type,
        data: {
            labels: ["Regularity", "Knowledge Depth", "Communication", "Engagement", "Explanation Quality"],
            datasets: [{
                label: "Performance Score",
                data: [
                    data.regularity || 0, 
                    data.knowledgeDepth || 0, 
                    data.communication || 0, 
                    data.engagement || 0, 
                    data.explanationQuality || 0
                ],
                backgroundColor: type === "bar" ? ["#007bff", "#28a745", "#ffc107", "#dc3545", "#17a2b8"] : 
                                 type === "pie" ? ["#ff6384", "#36a2eb", "#ffce56", "#4bc0c0", "#9966ff"] :
                                 type === "radar" ? ["rgba(0,123,255,0.5)"] : "#007bff", 
                borderColor: ["black"],  // Keeps a black border for clarity
                borderWidth: 1.5 
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, 
            scales: type === "pie" || type === "radar" ? {} : { y: { beginAtZero: true, max: 5 } }
        }
    });
}



// Show chart based on selection
function showChart(type) {
    const facultyId = sessionStorage.getItem("facultyId");
    if (!facultyId) {
        console.error("Faculty ID not found.");
        return;
    }

    fetch(`http://localhost:8080/faculty/performance?facultyId=${facultyId}`)
        .then(response => response.json())
        .then(data => generateChart(type, data))
        .catch(error => console.error("Error fetching score:", error));
}

// Logout function
function logout() {
    alert("Logging out...");
    sessionStorage.clear();
    window.location.href = "loginPage.html";
}
