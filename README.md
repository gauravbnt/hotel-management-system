  ---

# 🏨 Hotel Management System

A full-stack **Hotel Management System** built using **Spring Boot** and **Gradle**. This RESTful backend application manages guests, rooms, bookings, and payments. It supports dynamic booking pricing, robust exception handling, validation, and modular service architecture using DTOs.

---

## ✅ Features

- 🧑‍💼 Guest registration, search, update, and delete
- 🛏️ Room management by room number and type
- 📅 Booking rooms with price auto-calculated by stay duration
- 💵 Payment tracking with methods and transaction validation
- 🔐 Clean exception handling for all API layers
- 🧪 Unit & controller tests using JUnit 5 and Mockito

---

## ⚙️ Tech Stack

| Category     | Technology           |
|--------------|----------------------|
| Language     | Java 21              |
| Framework    | Spring Boot          |
| Build Tool   | **Gradle**           |
| Database     |  PostgreSQL          |
| Testing      | JUnit 5, Mockito     |
| REST         | Spring Web (REST APIs) |
| JSON Mapper  | Jackson              |
| Dev Tools    | Lombok, Spring DevTools |

---

## 📂 Project flow

```
                    +-------------------------+
                    |  Guest Enters Booking   |
                    +-------------------------+
                                |
                                v
                    +----------------------------+
                    | Check Room Availability    |
                    +----------------------------+
                                |
                                v
                    +----------------------------+
                    | Calculate Total Price      |
                    +----------------------------+
                                |
                                v
                    +----------------------------+
                    | Create Booking Record      |
                    +----------------------------+
                                |
                                v
                    +----------------------------+
                    | Return Booking Confirmation|
                    +----------------------------+


````

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 21
- Gradle 8
- Postman or Swagger UI

### 📦 Build & Run

```bash
# Clone the repository
git clone https://github.com/gauravbnt/hotel-management-system.git
cd hotel-management-system

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
````

---

## 📬 API Endpoints Overview

### 👤 Guest APIs

| Method | Endpoint          | Description             |
| ------ | ----------------- | ----------------------- |
| POST   | `/guests`         | Register a new guest    |
| GET   | `/guests`          | Get all guests          |
| GET    | `/guests/{email}` | Get guest by email      |
| PUT    | `/guests/{email}` | Update guest details    |
| DELETE | `/guests/{email}` | Delete a guest by email |

### 🛏️ Room APIs

| Method | Endpoint             | Description          |
| ------ | -------------------- | -------------------- |
| POST   | `/rooms`             | Add a new room       |
| GET    | `/rooms/{roomNumber}`| view a room by   roomnumber             |                          
| PUT    | `/rooms/{roomNumber}`| update a room by  roomnumber   |                             
| GET    | `/rooms`             | View all rooms       |
| GET    |`/rooms/{isAvaialble}`| Filter rooms by      availability|


### 📅 Booking APIs

| Method | Endpoint            | Description                  |
| ------ | ------------------- | ---------------------------- |
| POST   | `/bookings`         | Book a room                  |
| PUT    | `/bookings/{id}`    | Update an existing booking   |
| GET    | `/bookings/{email}` | View bookings by guest email |

### 💳 Payment APIs

| Method | Endpoint                    | Description                  |
| ------ | --------------------------- | ---------------------------- |
| POST   | `/payments`                 | Make a payment for a booking |
| GET    | `/payments/{transactionId}` | View payment details         |
| PUT    | `/payments/{transactionId}` | Update payment details       |


---

## 🧪 Running Tests

```bash
./gradlew test
```

Test coverage includes:

* ✔️ Service logic with JUnit + Mockito
* ✔️ Controller layer with MockMvc
* ✔️ Edge-case validation and exception flows

---

## 🛡️ Exception Handling

* All exceptions handled centrally via `GlobalExceptionHandler`
* Custom exceptions:

  * `GuestNotFoundException`
  * `RoomNotFoundException`
  * `BookingNotFoundException`
  * `PaymentException`
* Proper HTTP status codes (404, 400, 409) with meaningful error messages

---

## 📄 Sample Booking Request

```json
{
  "guestEmail": "john@example.com",
  "roomNumber": "101",
  "checkInDate": "2025-07-01",
  "checkOutDate": "2025-07-03",
  "bookingType": "ONLINE"
}
```

Auto-calculated amount = pricePerNight × numberOfNights

---

## 📌 Enum Reference

* **RoomType**: `SINGLE`, `DOUBLE`, `SUITE`
* **PaymentMethod**: `UPI`, `CARD`, `CASH`
* **BookingStatus**: `BOOKED`, `CHECKED_IN`, `CANCALLED`

---

