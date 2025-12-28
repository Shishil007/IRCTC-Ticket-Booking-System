A console-based Java train ticket booking system inspired by IRCTC.
This project demonstrates core backend concepts like authentication, data persistence, service-layer design, and seat booking logic using Java + Gradle.

📌 Features
👤 User Management

  User signup with password hashing
  
  User login & authentication
  
  Persistent user data stored in JSON

🚆 Train Management

  Search trains by source and destination
  
  Validate train routes (source before destination)
  
  Display available seat layout

🎟 Booking System

  Book train seats
  
  Prevent double booking
  
  Cancel bookings using Ticket ID
  
  Persist updated seat availability

💾 Persistence

  File-based storage using JSON
  
  users.json for user data

🧱 Project Architecture

IRCTC-Ticket-Booking-System
├── app/
│   └── src/main/java
│       └── org/ticket/booking
│           ├── App.java
│           ├── entities/
│           │   ├── User.java
│           │   ├── Train.java
│           │   └── Ticket.java
│           ├── services/
│           │   ├── UserBookingService.java
│           │   └── TrainService.java
│           └── utils/
│               └── UserServiceUtil.java
│
├── app/src/main/java/org/ticket/booking/localDB/
│   ├── users.json
│   └── trains.json
│
├── build.gradle
├── settings.gradle
├── gradlew
└── .gitignore



👨‍💻 Author
Shishil Sandilya
GitHub: Shishil007

⭐ If you like this project
Give it a ⭐ on GitHub — it helps a lot!
