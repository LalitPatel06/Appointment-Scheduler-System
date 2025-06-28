# Appointment-Scheduler-System
# 🗓️ Appointment Scheduler System

A console-based Java application to manage medical appointments efficiently with role-based access for Admin and Users. It helps automate appointment bookings, cancellations, and updates with real-time validations and doctor specialization mapping.

## 🚀 Features

- 🔐 **Role-based access** for Admin and Users
- 📅 **Book appointments** with time slot and doctor selection
- 🔄 **Update or cancel** existing appointments
- 🔍 **Search** appointments by name or ID
- 📃 **View appointment history**
- ✅ Input validation and conflict-free time slot allocation

## 🛠️ Tech Stack

- **Language:** Java (Core Java, OOPs, Exception Handling)
- **Database:** MySQL
- **UI:** Console-based (Scanner for input/output)
- **Connector:** JDBC
- **Build Tool:** Manual (VS Code / Terminal)

## 📂 Project Structure

```plaintext
AppointmentScheduler/
│
├── src/
│   ├── Main.java
│   ├── Admin.java
│   ├── User.java
│   ├── DBConnection.java
│   └── DoctorHelper.java
│
├── database/
│   └── appointment_schema.sql
│
└── README.md
```
---

### 🧑‍⚕️ Roles & Functionalities

#### 👤 User
- Book new appointment (name, problem, doctor, time)  
- View appointment list  
- Cancel appointment by ID (with confirmation)  

#### 🛡️ Admin
- View all appointments  
- Add new appointments  
- Cancel appointments (with confirmation)  
- Update appointment problem/doctor/time  
- Search appointments by ID or patient name  

---

### 📌 Validations

- **Name**: 3–20 alphabetic characters only  
- **Problem selection**: Numeric (1 to 5 only)  
- **Doctor selection**: Numeric within list range  
- **Time slot**: Cannot double-book  
- **ID validations** for update/cancel/search  

---

### 🧪 Sample Appointment Table (MySQL)

```sql
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    date DATE,
    time TIME,
    description VARCHAR(100),
    doctor_name VARCHAR(50)
);
```

---

### ⚙️ How to Run

1. Import into VS Code or any Java IDE  
2. Set up MySQL database using provided schema  
3. Add your DB credentials in `DBConnection.java`  
4. Compile and run `Main.java`  

---

### 🔗 GitHub Repository  
[Appointment-Scheduler-System](https://github.com/LalitPatel06/Appointment-Scheduler-System)

---

### 📬 Contact  
**Lalit Patel**  
📧 Email: lalitpatel062002@gmail.com  
🔗 LinkedIn: [linkedin.com/in/lalitpatel6260](https://www.linkedin.com/in/lalitpatel6260)
