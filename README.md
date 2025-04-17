# Enhancing faculty evaluation through NLP-based sentiment analysis of student feedback

The Faculty Feedback System, powered by StanfordNLP-based sentiment analysis, provides a data-driven, automated, and dynamic approach to faculty evaluation. By leveraging real-time sentiment classification, dynamic visual representations, and automated handling of inappropriate feedback, the system offers a more insightful and efficient alternative to traditional evaluation methods. Faculty members gain actionable insights into their strengths and areas for improvement, while administrators benefit from structured performance tracking for institutional decision-making. The system not only enhances the quality of feedback analysis but also fosters a continuous improvement culture in academia. Future work can explore advanced NLP techniques and deep learning models to further refine sentiment accuracy and expand functionality.

---

## Features

### 👨‍💼Admin Functionalities
- **Admin Login Page**: Secure login with form validation.
- **Faculty Management**:
  - Add faculty along with:
    - Subject-Semester mapping
    - Campus Code
    - Profile Image
    - Mobile number & Email
  - Update faculty details (subject-semester pairs can be replaced or extended).
  - Delete full faculty records or specific subject-semester entries.
  - View all faculty and filter based on:
    - Subject
    - Semester
    - Campus Code
- **Student Credentials Management**:
  - Auto-generate student credentials from roll number.
    - **Username**: Roll number  
    - **Password**: `AdityaStudent`
  - Filter students by:
    - Year of joining
    - Campus code
    - Entry type
    - Branch code
  - Delete individual or bulk student credentials.
- **Semester Update**:
  - Update a student’s semester individually by roll number.
  - Bulk semester update option also available.
- **Forgot Password**: OTP sent to admin's email, handled via backend-only email logic.

### 🎓 Student Functionalities
- **Student Login**: Secure login using roll number and password.
- **Feedback Submission**:
  - Students can give feedback only to faculties that match their semester and campus.
  - Feedback includes ratings for:
    - Regularity  
    - Knowledge depth  
    - Communication  
    - Engagement  
    - Explanation quality  
  - Textbox for additional comments
- **Forgot Password**: OTP sent to institutional email (`{roll}@acoe.edu.in`), handled via backend-only email logic.

### 📊 Faculty Performance Analysis using NLP
- **NLP Sentiment Analysis**:
  - Sentiment-based scoring from student comments using **Stanford NLP**.
  - Sentiment categories like *Positive*, *Negative*, etc., used for detailed analysis.

### 👨‍🏫 Faculty Dashboard
- **Faculty Login** using ID, email, or mobile.
- Personalized dashboard:
  - View overall performance scores.
  - Dynamic chart visualizations (bar, pie, and spread).
  - Quick suggestions based on areas needing improvement.

---

## Tech Stack

### Backend
- **Spring Boot** – REST API + Business Logic
- **Hibernate / JPA** – ORM for MySQL DB
- **NLP (Stanford CoreNLP)** – Sentiment Analysis for Comments

### Frontend
- **HTML/CSS/JavaScript** – Dynamic UI
- **Chart.js** – Data Visualization
- **Bootstrap** – Styling & Layout

### Database
- **MySQL** – Core Data Store

---

## Screenshots
- **Home Page**

  ![Home Page](https://github.com/Sk1458/FacultyFeedback/blob/main/home.png)

- **Login Page**

  ![Login Page](https://github.com/Sk1458/FacultyFeedback/blob/main/LoginPage.png)

- **Admin Login**

  ![Admin Login](https://github.com/Sk1458/FacultyFeedback/blob/main/AdminLogin.png)

- **Admin Dashboard**

  ![Admin Dashboard](https://github.com/Sk1458/FacultyFeedback/blob/main/AdminDashboard.png)

- **Student Login**

  ![Student Login](https://github.com/Sk1458/FacultyFeedback/blob/main/StudentLogin.png)

- **Student Dashboard**

  ![Student Dashboard](https://github.com/Sk1458/FacultyFeedback/blob/main/StudentDashboard.png)

- **Feedback Form**
 
  ![Feedback Form](https://github.com/Sk1458/FacultyFeedback/blob/main/FeedbackForm.png)

- **Faculty Login**

   ![Faculty Login](https://github.com/Sk1458/FacultyFeedback/blob/main/FacultyLogin.png)

- **Faculty Dashboard**

  ![Faculty Dashboard](https://github.com/Sk1458/FacultyFeedback/blob/main/FacultyDashboard.png)

- **Bar Graph**

  ![bar Graph](https://github.com/Sk1458/FacultyFeedback/blob/main/BarGraph.png)

- **Pie Chart**

  ![Pie Chart](https://github.com/Sk1458/FacultyFeedback/blob/main/PieChart.png)

- **Radar Chart**

  ![Radar Chart](https://github.com/Sk1458/FacultyFeedback/blob/main/radarChart.png)


---

## How to Run

- 1. **Clone the repository:**
     ```bash
     git clone https://github.com/yourusername/faculty-feedback-system.git
     cd faculty-feedback-system
     
- 2. **Set Up the Frontend:**
     Navigate to the Frontend folder.
     Open the login.html file using:
       - VS Code Live Server (recommended).
       - Any other HTML rendering platform of your choice.
      
- 3. **Configure CORS Origins:**
     - Locate the @CorsOrigins annotation in the controller classes inside the Backend folder:
       ```bash
       src/main/java/com/example/demo
     - Update the value to match the link where the frontend HTML files will run (e.g., http://localhost:5500 for VS Code Live Server).

- 4. **Configure the database:**
    - Open the application.properties or application.yml file located in:
      ```bash
       src/main/resources/.
      
    - Update the database credentials:
      ```bash
      spring.datasource.url=jdbc:mysql://localhost:3306/your-database-name
      spring.datasource.username=your-username
      spring.datasource.password=your-password
      
- 5. **Run the application:**
    - Launch the backend application from your Java-supported IDE.
    - Access the frontend HTML pages using the live rendering method chosen earlier.
