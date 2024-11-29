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
