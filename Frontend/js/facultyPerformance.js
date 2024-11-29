// Fetch faculty performance data from the backend
fetch('http://localhost:8080/admin/viewFacultyPerformance')
.then(response => response.json())
.then(data => {
    // Check if data is valid
    if (!data || data.length === 0) {
        console.error('Invalid or empty performance data');
        return;
    }

    // Loop through each faculty data
    data.forEach(faculty => {
        const facultyName = faculty.name;
        const facultyImage = faculty.base64Image; // Use the base64 image
        const subjectPerformance = faculty.performance;

        const subjectNames = Object.keys(subjectPerformance).filter(subject => subject !== 'overall');
        const subjectScores = subjectNames.map(subject => subjectPerformance[subject]);
        const overallScore = faculty.overallScore;

        // Create faculty card container
        const facultyCard = document.createElement('div');
        facultyCard.classList.add('faculty-card');

        // Add faculty image
        const imgElement = document.createElement('img');
        imgElement.src = `data:image/jpeg;base64,${facultyImage}`; // Use base64 image
        imgElement.classList.add('faculty-image');
        facultyCard.appendChild(imgElement);

        // Add faculty name
        const nameElement = document.createElement('div');
        nameElement.classList.add('faculty-name');
        nameElement.textContent = facultyName;
        facultyCard.appendChild(nameElement);

        // Add the pie chart canvas element
        const canvas = document.createElement('canvas');
        facultyCard.appendChild(canvas);

        // Set canvas size explicitly
        canvas.width = 200;
        canvas.height = 200;

        // Render pie chart for the faculty
        const ctx = canvas.getContext('2d');
        new Chart(ctx, {
            type: 'pie', // Pie chart for each faculty
            data: {
                labels: subjectNames, // Don't include overall score in the pie chart
                datasets: [{
                    label: 'Faculty Performance',
                    data: subjectScores, // Use only subject scores for the pie chart
                    backgroundColor: [
                        'rgba(75, 192, 192, 0.6)',   /* Soft teal */
                        'rgba(153, 102, 255, 0.6)',  /* Vibrant purple */
                        'rgba(255, 159, 64, 0.6)',    /* Bright orange */
                        'rgba(255, 99, 132, 0.6)',    /* Bold red */
                        'rgba(54, 162, 235, 0.6)'     /* Calm blue */
                    ],
                    borderColor: [
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)',
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(tooltipItem) {
                                return tooltipItem.label + ': ' + tooltipItem.raw.toFixed(2); // Display performance score
                            }
                        }
                    }
                }
            }
        });

        // Display the overall score below the pie chart, with a check for undefined
        const overallScoreElement = document.createElement('div');
        overallScoreElement.classList.add('overall-score');
        overallScoreElement.textContent = `Overall Score: ${overallScore ? overallScore.toFixed(2) : 'N/A'}`;
        facultyCard.appendChild(overallScoreElement);

        // Append the faculty card to the container
        document.getElementById('faculty-container').appendChild(facultyCard);
    });
})
.catch(error => console.error('Error fetching faculty performance data:', error));