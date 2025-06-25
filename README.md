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
| Language     | Java 17+             |
| Framework    | Spring Boot          |
| Build Tool   | **Gradle**           |
| Database     |  PostgreSQL          |
| Testing      | JUnit 5, Mockito     |
| REST         | Spring Web (REST APIs) |
| JSON Mapper  | Jackson              |
| Dev Tools    | Lombok, Spring DevTools |

---

## 📂 Project Structure

```

hotel-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/example/hms/
│   │   │   ├── controller/
│   │   │   │   ├── GuestController.java
│   │   │   │   ├── RoomController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   └── PaymentController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── GuestRequestDTO.java
│   │   │   │   │   ├── RoomRequestDTO.java
│   │   │   │   │   ├── BookingRequestDTO.java
│   │   │   │   │   └── PaymentRequestDTO.java
│   │   │   │   └── response/
│   │   │   │       ├── GuestResponseDTO.java
│   │   │   │       ├── RoomResponseDTO.java
│   │   │   │       ├── BookingResponseDTO.java
│   │   │   │       └── PaymentResponseDTO.java
│   │   │   ├── entity/
│   │   │   │   ├── Guest.java
│   │   │   │   ├── Room.java
│   │   │   │   ├── Booking.java
│   │   │   │   └── Payment.java
│   │   │   ├── enums/
│   │   │   │   ├── RoomType.java
│   │   │   │   ├── PaymentMethod.java
│   │   │   │   └── BookingType.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── GuestNotFoundException.java
│   │   │   │   ├── RoomNotFoundException.java
│   │   │   │   ├── BookingNotFoundException.java
│   │   │   │   └── PaymentException.java
│   │   │   ├── repository/
│   │   │   │   ├── GuestRepository.java
│   │   │   │   ├── RoomRepository.java
│   │   │   │   ├── BookingRepository.java
│   │   │   │   └── PaymentRepository.java
│   │   │   ├── service/
│   │   │   │   ├── GuestService.java
│   │   │   │   ├── GuestServiceImpl.java
│   │   │   │   ├── RoomService.java
│   │   │   │   ├── RoomServiceImpl.java
│   │   │   │   ├── BookingService.java
│   │   │   │   ├── BookingServiceImpl.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   └── PaymentServiceImpl.java
│   │   │   └── HotelManagementSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql (optional)
│   └── test/
│       └── java/com/example/hms/
│           ├── controller/
│           │   ├── GuestControllerTest.java
│           │   ├── RoomControllerTest.java
│           │   ├── BookingControllerTest.java
│           │   └── PaymentControllerTest.java
│           └── service/
│               ├── GuestServiceTest.java
│               ├── RoomServiceTest.java
│               ├── BookingServiceTest.java
│               └── PaymentServiceTest.java
├── build.gradle
├── settings.gradle

````

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17 or later
- Gradle 7+
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
| GET    | `/guests/{email}` | Get guest by email      |
| PUT    | `/guests/{email}` | Update guest details    |
| DELETE | `/guests/{email}` | Delete a guest by email |

### 🛏️ Room APIs

| Method | Endpoint             | Description          |
| ------ | -------------------- | -------------------- |
| POST   | `/rooms`             | Add a new room       |
| GET    | `/rooms`             | View all rooms       |
| GET    | `/rooms/type/{type}` | Filter rooms by type |

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

