const apiUrl = "http://localhost:8080/admin";

// Pagination variables
let currentPage = 1;
const pageSize = 10;
let allRollNumbers = [];

// Function to filter and display student credentials
async function filterCredentials() {
    const year = document.getElementById("year").value.trim();
    const campus = document.getElementById("campus").value;
    const entryType = document.getElementById("entryType").value;
    const branch = document.getElementById("branch").value;

    const queryParams = new URLSearchParams({
        year: year,
        campus: campus,
        entryType: entryType,
        branch: branch
    }).toString();

    try {
        const response = await fetch(`${apiUrl}/viewStudentCredentials?${queryParams}`);
        
        if (response.ok) {
            allRollNumbers = await response.json();
            currentPage = 1;  // Reset to the first page after filtering
            displayPage();
            updatePaginationControls();
        } else {
            alert("Failed to fetch student credentials.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Error fetching student credentials.");
    }
}

// Function to display the current page of credentials
function displayPage() {
    const tableBody = document.querySelector("#credentialsTable tbody");
    tableBody.innerHTML = "";

    if (allRollNumbers.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="2">No credentials found.</td></tr>`;
        return;
    }

    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const pageData = allRollNumbers.slice(start, end);

    pageData.forEach(roll => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${roll}</td>
            <td>
                <button onclick="deleteCredential('${roll}')">Delete</button>
            </td>
        `;
        tableBody.appendChild(row);
    });

    updatePaginationControls();
}

// Function to update pagination controls
function updatePaginationControls() {
    // Check if the pagination container exists; if not, create it dynamically
    let paginationControls = document.querySelector(".pagination-controls");

    if (!paginationControls) {
        paginationControls = document.createElement("div");
        paginationControls.classList.add("pagination-controls");
        document.body.appendChild(paginationControls);  // Append to the body or appropriate container
    }

    paginationControls.innerHTML = "";

    const totalPages = Math.ceil(allRollNumbers.length / pageSize);

    if (totalPages <= 1) return;  // No need for pagination if only one page

    // Previous button
    const prevButton = document.createElement("button");
    prevButton.textContent = "Previous";
    prevButton.disabled = currentPage === 1;
    prevButton.onclick = () => {
        currentPage--;
        displayPage();
    };
    paginationControls.appendChild(prevButton);

    // Page info box
    const pageInfo = document.createElement("div");
    pageInfo.classList.add("page-info");
    pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
    paginationControls.appendChild(pageInfo);

    // Next button
    const nextButton = document.createElement("button");
    nextButton.textContent = "Next";
    nextButton.disabled = currentPage === totalPages;
    nextButton.onclick = () => {
        currentPage++;
        displayPage();
    };
    paginationControls.appendChild(nextButton);
}


// Function to delete a single credential
async function deleteCredential(rollNumber) {
    if (!confirm(`Are you sure you want to delete ${rollNumber}?`)) return;

    try {
        const cleanRollNumber = rollNumber.trim().split(" ")[0];  // Extract only the roll number

        const response = await fetch(`${apiUrl}/deleteStudentCredential/${cleanRollNumber}`, {
            method: "DELETE"
        });

        if (response.ok) {
            alert(`${rollNumber} deleted successfully`);
            allRollNumbers = allRollNumbers.filter(roll => roll !== rollNumber);
            displayPage();
            updatePaginationControls();
        } else {
            alert(`Failed to delete ${rollNumber}`);
        }
    } catch (error) {
        console.error("Error:", error);
        alert(`Error deleting ${rollNumber}`);
    }
}

// Function to delete all filtered credentials
async function deleteAllFiltered() {
    if (!confirm("Are you sure you want to delete ALL filtered credentials?")) return;

    const year = document.getElementById("year").value.trim();
    const campus = document.getElementById("campus").value;
    const entryType = document.getElementById("entryType").value;
    const branch = document.getElementById("branch").value;

    const queryParams = new URLSearchParams();
    if (year) queryParams.append("year", year);
    if (campus) queryParams.append("campus", campus);
    if (entryType) queryParams.append("entryType", entryType);
    if (branch) queryParams.append("branch", branch);

    try {
        const response = await fetch(`${apiUrl}/deleteAllStudentCredentials?${queryParams.toString()}`, {
            method: "DELETE"
        });

        if (response.ok) {
            const message = await response.text();
            alert(message);

            // Clear the table after deleting all filtered credentials
            allRollNumbers = [];
            displayPage();
        } else {
            alert("Failed to delete all filtered credentials.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Error deleting all filtered credentials.");
    }
}

// Function to navigate back to the Admin Dashboard
function goToAdminDashboard() {
    window.location.href = "admin.html";
}