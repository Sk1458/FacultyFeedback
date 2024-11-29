# Faculty Feedback System

A full-stack web application for managing and analyzing faculty performance feedback. Built using **Spring Boot**, **HTML**, **CSS**, **JavaScript**, and **Chart.js**, this project allows administrators to manage faculty details and students to provide feedback.

---

## Features

### Admin Functionalities
- **Login Page**: Secure login for administrators.
- **Manage Faculty**:
  - Add new faculty members.
  - Update faculty details.
  - Delete faculty records.
  - View a list of all faculty members with their details.
- **Performance Analysis**:
  - Calculate overall feedback scores for faculty based on student responses.
  - Visualize faculty performance using pie charts for each faculty member.

### Student Functionalities
- **Feedback System**:
  - Secure login with a common student ID and password.
  - View a list of faculty members and give feedback for each.
  - Feedback form includes fields like:
    - Subject taught
    - Semester
    - Ratings for attributes like regularity, knowledge depth, communication, engagement, and explanation quality.
    - Additional comments section.

---

## Tech Stack

### Backend
- **Spring Boot**: Used for building RESTful APIs and handling business logic.
- **Hibernate**: For database interaction.

### Frontend
- **HTML/CSS/JavaScript**: For creating interactive user interfaces.
- **Chart.js**: To render graphical representations of faculty performance.

### Database
- **MySQL Database**: For development and testing.

---

## Screenshots

### Admin Dashboard


### Student Feedback Page


### Faculty Performance Analysis


---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/faculty-feedback-system.git
   cd faculty-feedback-system


## Project Directory Structure
```bash
FacultyFeedback(Repository)
├───Backend
│   └───FacultyFeedbackSystem
│       │   .classpath
│       │   .gitattributes
│       │   .gitignore
│       │   .project
│       │   HELP.md
│       │   mvnw
│       │   mvnw.cmd
│       │   pom.xml
│       │
│       ├───.mvn
│       │   └───wrapper
│       │           maven-wrapper.properties
│       │
│       ├───.settings
│       │       org.eclipse.core.resources.prefs
│       │       org.eclipse.jdt.apt.core.prefs
│       │       org.eclipse.jdt.core.prefs
│       │       org.eclipse.m2e.core.prefs
│       │       org.springframework.ide.eclipse.prefs
│       │
│       ├───logs
│       │       admin-activities.html
│       │
│       ├───src
│       │   ├───main
│       │   │   ├───java
│       │   │   │   └───com
│       │   │   │       └───example
│       │   │   │           └───demo
│       │   │   │                   Admin.java
│       │   │   │                   AdminRepository.java
│       │   │   │                   AdminSetupRunner.java
│       │   │   │                   CorsConfig.java
│       │   │   │                   FacultyController.java
│       │   │   │                   FacultyData.java
│       │   │   │                   FacultyFeedbackSystemApplication.java
│       │   │   │                   FacultyPerformanceDTO.java
│       │   │   │                   FacultyRepository.java
│       │   │   │                   FeedbackEntry.java
│       │   │   │                   LoginController.java
│       │   │   │                   SecurityConfig.java
│       │   │   │                   StudentController.java
│       │   │   │                   StudentCredentials.java
│       │   │   │                   StudentCredentialsRepository.java
│       │   │   │                   StudentSetupRunner.java
│       │   │   │
│       │   │   └───resources
│       │   │       │   application.properties
│       │   │       │   LogBack.xml
│       │   │       │
│       │   │       ├───static
│       │   │       └───templates
│       │   └───test
│       │       └───java
│       │           └───com
│       │               └───example
│       │                   └───demo
│       │                           FacultyFeedbackSystemApplicationTests.java
│       │
│       └───target
│           ├───classes
│           │   │   application.properties
│           │   │   LogBack.xml
│           │   │
│           │   ├───com
│           │   │   └───example
│           │   │       └───demo
│           │   │               Admin.class
│           │   │               AdminRepository.class
│           │   │               AdminSetupRunner.class
│           │   │               CorsConfig$1.class
│           │   │               CorsConfig.class
│           │   │               FacultyController.class
│           │   │               FacultyData.class
│           │   │               FacultyFeedbackSystemApplication.class
│           │   │               FacultyPerformanceDTO.class
│           │   │               FacultyRepository.class
│           │   │               FeedbackEntry.class
│           │   │               LoginController.class
│           │   │               SecurityConfig.class
│           │   │               StudentController.class
│           │   │               StudentCredentials.class
│           │   │               StudentCredentialsRepository.class
│           │   │               StudentSetupRunner.class
│           │   │
│           │   └───META-INF
│           │       │   MANIFEST.MF
│           │       │
│           │       └───maven
│           │           └───CSE21
│           │               └───FacultyFeedbackSystem
│           │                       pom.properties
│           │                       pom.xml
│           │
│           └───test-classes
│               └───com
│                   └───example
│                       └───demo
│                               FacultyFeedbackSystemApplicationTests.class
│
└───Frontend
    │   addFaculty.html
    │   admin.html
    │   deleteFaculty.html
    │   facultyPerformance.html
    │   feedback.html
    │   feedbackForm.html
    │   login.html
    │   updateFaculty.html
    │   viewFaculty.html
    │
    ├───css
    │       addFaculty.css
    │       deleteFaculty.css
    │       facultyPerformance.css
    │       feedback.css
    │       feedbackForm.css
    │       login.css
    │       updateFaculty.css
    │       viewFaculty.css
    │
    └───js
            addFaculty.js
            deleteFaculty.js
            facultyPerformance.js
            feedback.js
            feedbackForm.js
            login.js
            updateFaculty.js
            viewFaculty.js

