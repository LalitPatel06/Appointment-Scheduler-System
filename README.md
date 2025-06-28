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

## 📟 Console Output (Sample Flow)
Welcome to Appointment Scheduler
Are you Admin? (yes/no): no

=== User Dashboard ===
1. Book New Appointment
2. View All Appointments
3. Cancel Appointment
4. Exit
Enter your choice: 1

Enter your name: Rahul
Select your problem:
1. Chest
2. Eye
3. Teeth
4. Skin
5. General
Enter your choice: 3

Available doctors for Teeth:
1. Dr. Raj
2. Dr. Meena
Enter doctor choice (or press Enter for default): 1

Available Time Slots for Dr. Raj on 2025-06-27:
1. 10:00 AM to 10:30 AM
2. 10:30 AM to 11:00 AM
...
Choose your time slot: 2

✅ Appointment Confirmed!
Name     : Rahul
Date     : 2025-06-27
Time     : 10:30 AM to 11:00 AM
Problem  : Teeth
Doctor   : Dr. Raj

Welcome to Appointment Scheduler
Are you Admin? (yes/no): yes

=== Admin Dashboard ===
1. View All Appointments
2. Add Appointment
3. Cancel Appointment
4. Update Appointment
5. Search Appointment
6. Exit
Enter your choice: 1

--- All Appointments ---
ID   Name         Date        Time     Problem      Doctor
1    Rahul        2025-06-27  10:30AM  Teeth        Dr. Raj
------------------------------------------------------------

Enter your choice: 3
Enter Appointment ID to cancel: 1

Appointment Details:
Name    : Rahul
Date    : 2025-06-27
Time    : 10:30:00
Problem : Teeth
Doctor  : Dr. Raj
Are you sure you want to cancel this appointment? (yes/no): yes
✅ Appointment cancelled successfully.

Enter your choice: 5
Enter name or ID to search: Rahul
ID   Name    Date        Time     Problem    Doctor
1    Rahul   2025-06-27  10:30AM  Teeth      Dr. Raj


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
