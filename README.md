# 🐾 PetMate Backend

Welcome to the backend repository for **PetMate** — an all-in-one pet care and management platform designed to empower pet owners and service providers with a digital ecosystem that makes pet parenting smarter and easier.

## 📌 Project Overview

**PetMate** is a third-year individual project by **Sethmi Danoshi**, aiming to offer digital pet care solutions including pet profile tracking, health management, appointment booking, and more — all backed by a powerful Spring Boot backend and served through a mobile/web app.

## 🚀 Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Web & REST**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Lombok**
- **Spring Security (optional)**
- **Maven**
- **Postman** for testing APIs

## 🧠 Key Features

- 🐕 Pet profile creation & management
- 📅 Vet appointment scheduling
- 🧑‍⚕️ Service provider management dashboard
- 🔔 Automated email/SMS notifications & reminders
- 📈 Admin dashboard (optional for analytics/monitoring)

## 🗂️ Project Structure

```
setpetcare-backend/
├── src/
│   ├── main/java/com/setpetcare/
│   │   ├── controller/      # REST Controllers
│   │   ├── model/           # Data models / entities
│   │   ├── repository/      # JPA Repositories
│   │   ├── service/         # Business logic
│   │   └── SetPetcareApplication.java
│   └── main/resources/
│       ├── application.properties
│       └── static/
├── pom.xml
└── README.md
```

## ⚙️ Getting Started

### 🖥️ Prerequisites
- Java 17+
- Maven
- MySQL Server

### 🔧 Setup

1. **Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/SetPetcare-Backend.git
cd SetPetcare-Backend
```

2. **Configure MySQL Database**
   Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/setpetcare_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. **Run the app**
```bash
./mvnw spring-boot:run
```

Or from your IDE (IntelliJ, VS Code), run `SetPetcareApplication.java`.

---

## 🧪 API Testing

You can test endpoints using [Postman](https://www.postman.com/) or tools like Insomnia. API docs (e.g., Swagger/OpenAPI) can be added later for full REST documentation.

---

## 📬 Contact

Maintained by **Sethmi Danoshi**  
📧 Email: `sethmidanoshi06@gmail.com`
