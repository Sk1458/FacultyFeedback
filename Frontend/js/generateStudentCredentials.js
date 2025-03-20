document.getElementById('generateForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const yearInput = document.getElementById('year').value;  
    const campus = document.getElementById('campus').value;
    const entryType = document.getElementById('entryType').value;
    const branch = document.getElementById('branch').value;
    const count = parseInt(document.getElementById('count').value, 10);

    const year = yearInput.slice(-2);  // Extract last 2 digits (2021 → 21)

    const apiUrl = `http://localhost:8080/admin/generateStudentCredentials?year=${year}&campus=${campus}&entryType=${entryType}&branch=${branch}&count=${count}`;

    try {
        const response = await fetch(apiUrl, { method: 'POST' });

        if (response.ok) {
            const message = await response.text();
            alert(`Credentials generated successfully!\n${message}`);

            // Display the credentials
            const tableBody = document.querySelector("#credentialsTable tbody");
            tableBody.innerHTML = '';

            for (let i = 0; i < count; i++) {
                const rollNumber = `${year}${campus}${entryType}${branch}`;

                let suffix;
                if (i < 99) {
                    suffix = (i + 1).toString().padStart(2, '0');
                } else {
                    const charIndex = Math.floor((i - 99) / 10);
                    const char = String.fromCharCode(65 + charIndex);
                    suffix = `${char}${(i - 99) % 10}`;
                }

                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${rollNumber + suffix}</td>
                    <td>AdityaStudent</td>
                `;
                tableBody.appendChild(row);
            }

        } else {
            alert("Failed to generate credentials.");
        }
    } catch (error) {
        console.error('Error:', error);
        alert("Error generating credentials.");
    }
});
