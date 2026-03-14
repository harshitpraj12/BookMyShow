# 🎬 BookMyShow Backend (Spring Boot)

A backend REST API system for an online movie ticket booking platform inspired by BookMyShow.  
This project allows users to browse movies, filter shows, book seats, and make payments through a scalable backend architecture built using Spring Boot.

**:** Harshit Raj B-tech CSE Backend Developer

---

# 🚀 Features

- User registration
- Add movies and theatres
- Schedule movie shows
- Book seats for a show
- Make payments
- Generate payment ID
- Retrieve movie details by:
  - Movie ID
  - Show ID
  - City
- Filter movies and shows by:
  - City
  - Date
  - Duration
  - Available seats

### Future Improvements

- Implement Spring Security authentication
- Add JWT based authorization
- Payment gateway integration

---

# 🛠 Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.0.2
- **Build Tool:** Maven
- **Database:** SQL
- **ORM:** Spring Data JPA
- **API Style:** REST APIs
- **Security:** Spring Security (Planned)
- **Future Security:** JWT Authentication

---

# 🏗 Architecture

The project follows a layered architecture to ensure separation of concerns.
```
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database
```


---

# 📂 Project Structure

```
src/main/java/com/bookmyshow

├── controller # REST API controllers
├── service # Business logic
├── repository # Database access layer
├── entity # Database entity classes
├── dto # Data Transfer Objects
├── exception # Global exception handling
└── configuration # Application configuration
```


---

# 📦 Core Functional Modules

### 👤 User Module
- Register new users
- Manage user bookings

### 🎥 Movie Module
- Add movies
- Get movie by ID
- Filter movies by city

### 🏢 Theatre Module
- Add theatres
- Manage screens and shows

### 🎟 Booking Module
- Book seats for a show
- Check seat availability

### 💳 Payment Module
- Make payment
- Generate payment ID
- Track payment status

---

# ⚙️ API Testing

All APIs are tested using **Postman**.

---

# 📸 Postman API Screenshots

Create a folder in your repository like this:

Add screenshots inside it and display them like this:

markdown
## Postman API Screenshots

### User Registration
![Register User]("E:\user.png")

### Add Movie
![Add Movie](docs/postman/add-movie.png)

### Book Show
![Book Show](docs/postman/book-show.png)

### Payment API
![Payment](docs/postman/payment.png)


▶️ Running the Project
1. Clone the repository
```
git clone https://github.com/yourusername/BookMyShow.git
```
2. Navigate to project folder
```
cd BookMyShow
```
3. Run the project
```
mvn spring-boot:run
```
