const form = document.getElementById("filterForm");
const tableBody = document.querySelector("#studentTable tbody");
const prevBtn = document.getElementById("prevPage");
const nextBtn = document.getElementById("nextPage");
const pageInfo = document.getElementById("pageInfo");

let allRollNumbers = [];
let currentPage = 1;
const pageSize = 20; // Number of roll numbers per page

// Fetch and display data with pagination
form.addEventListener("submit", async (event) => {
    event.preventDefault();
    currentPage = 1; // Reset to first page on new filter
    await fetchRollNumbers();
});

async function fetchRollNumbers() {
    const year = document.getElementById("year").value;
    const campus = document.getElementById("campus").value;
    const entryType = document.getElementById("entryType").value;
    const branch = document.getElementById("branch").value;

    const url = new URL("http://localhost:8080/admin/viewStudentCredentials");
    if (year) url.searchParams.append("year", year);
    if (campus) url.searchParams.append("campus", campus);
    if (entryType) url.searchParams.append("entryType", entryType);
    if (branch) url.searchParams.append("branch", branch);

    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error("Failed to fetch data");

        allRollNumbers = await response.json();
        displayPage(currentPage);
        updatePaginationButtons();

    } catch (error) {
        console.error("Error fetching data:", error);
    }
}

// Display the current page of roll numbers
function displayPage(page) {
    tableBody.innerHTML = "";

    const startIndex = (page - 1) * pageSize;
    const endIndex = Math.min(startIndex + pageSize, allRollNumbers.length);

    for (let i = startIndex; i < endIndex; i++) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.textContent = allRollNumbers[i];
        row.appendChild(cell);
        tableBody.appendChild(row);
    }

    pageInfo.textContent = `Page ${currentPage} of ${Math.ceil(allRollNumbers.length / pageSize)}`;
}

// Update the button states based on the current page
function updatePaginationButtons() {
    prevBtn.disabled = currentPage === 1;
    nextBtn.disabled = currentPage * pageSize >= allRollNumbers.length;
}

// Pagination button event handlers
prevBtn.addEventListener("click", () => {
    if (currentPage > 1) {
        currentPage--;
        displayPage(currentPage);
        updatePaginationButtons();
    }
});

nextBtn.addEventListener("click", () => {
    if (currentPage * pageSize < allRollNumbers.length) {
        currentPage++;
        displayPage(currentPage);
        updatePaginationButtons();
    }
});