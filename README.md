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

### Login Page
![LoginPage](https://github.com/user-attachments/assets/cad9f796-5671-4f35-9a25-66b473b6c0fb)

### Admin Dashboard
![dashboard](https://github.com/user-attachments/assets/ff89831a-f3db-4518-beb0-9a7929eb34c0)

### Student Feedback Page
![FeedbackList](https://github.com/user-attachments/assets/60f13ad0-cf55-4d46-a4c4-fe2d8613ae3a)

### Faculty Performance Analysis
![FacultyPerf](https://github.com/user-attachments/assets/10275290-90d7-4c66-bd6a-183fe802446c)

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
│       ├───.mvn
│       │   └───wrapper
│       ├───.settings
│       ├───logs
│       │       /HTML file showcasing all the log activities
│       ├───src
│       │   ├───main
│       │   │   ├───java
│       │   │   │   └───com
│       │   │   │       └───example
│       │   │   │           └───demo
│       │   │   │                   /All the .java files here
│       │   │   └───resources
│       │   │       ├───static
│       │   │       └───templates
│       │   └───test
│       │       └───java
│       │           └───com
│       │               └───example
│       │                   └───demo
│       └───target
│           ├───classes
│           │   │   application.properties
│           │   │   LogBack.xml
│           │   ├───com
│           │   │   └───example
│           │   │       └───demo
│           │   │               /All the .class files here
│           │   └───META-INF
│           │       └───maven
│           │           └───CSE21
│           │               └───FacultyFeedbackSystem
│           │                       pom.properties
│           │                       pom.xml
│           └───test-classes
│               └───com
│                   └───example
│                       └───demo
└───Frontend
    |  /All the HTML files here
    │
    ├───css      
    └───js

