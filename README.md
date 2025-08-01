# 📚 Book Heaven

**Book Heaven** is a second-hand book marketplace Android application that enables users to buy and sell used books affordably and efficiently. The platform is designed to enhance book accessibility and affordability, particularly for students and avid readers.

---

## 📱 Screenshots

https://github.com/saumya-sewmini/BookHeaven/blob/6f49e530bd3b93d5d2dc8d2401e5a2e8da6f4086/Screenshot%202025-08-02%20001832.png

---

## 📖 Abstract

Book Heaven is a user-friendly Android application that facilitates secure transactions for second-hand books. It includes features like user authentication, advanced search, secure checkout, and an admin control panel. The system is built with Java, Android Studio, and Hibernate, and uses Firebase and Payhere for data and payment handling.

---

## 🧩 Features

- 🔐 Secure user authentication and profile management
- 📚 Book browsing, advanced search, and detailed listings
- 🛒 Shopping cart and secure checkout
- 📦 Order history and tracking
- 🛠 Admin panel for managing users, books, and orders
- 📱 Responsive and intuitive UI
- 🔒 Encrypted user data and secure payment integration
- 📶 Offline order history using SQLite

---

## 🧱 Architecture & Modules

### 📐 Design Approach

The app follows a **modular, layered architecture**:
- **Frontend:** Android App (Java, Jetpack)
- **Backend:** Java EE with Hibernate
- **Database:** MySQL
- **Cloud Services:** Firebase (Storage + Firestore)
- **Payment Gateway:** Payhere

### 📦 Modules

1. **User Management**
   - Register/Login
   - Profile update
2. **Book Management**
   - List, search, and view books
   - Upload with images
3. **Order Management**
   - Cart management
   - Checkout and payment
   - Order tracking
4. **Admin Panel**
   - Approve/delete users
   - Manage listings and disputes

---

## 📊 System Specifications

### ✅ Functional Requirements

- Register/Login and manage profile
- List and browse books
- Add to cart and checkout
- View and track orders
- Admin controls for platform monitoring

### ⚙️ Non-Functional Requirements

- Fast loading (≤ 3s)
- Secure encryption of user data
- Mobile responsiveness
- Offline support for order history

---

## 💡 ER Diagram

> 📌 _Insert an ER diagram image showing Users, Books, Orders, etc._  
![ER Diagram](screenshots/er-diagram.png)

---

## 🚀 Tech Stack

| Layer            | Technology                  |
|------------------|-----------------------------|
| Frontend         | Java, Android Studio        |
| UI Framework     | Jetpack                     |
| Backend          | Java EE, Hibernate          |
| Database         | MySQL, SQLite (offline)     |
| Cloud Storage    | Firebase Storage, Firestore |
| Payment Gateway  | Payhere                     |

---

