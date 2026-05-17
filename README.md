## Hospital Management System

A backend-focused Hospital Management System built using Java and Spring Boot following a clean layered architecture approach.
The project is designed to manage core hospital workflows such as patient records, doctor management, appointments, and billing operations.

### Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Spring Boot Actuator

### Features
### Patient Management
- Add new patients
- Update patient details
- View patient information
- Delete patient records
### Doctor Management
- Add doctors
- Manage doctor details
- Retrieve doctor information
### Appointment Management
- Schedule appointments
- Manage appointment records
- Associate patients with doctors
### Billing Management
- Generate bills
- Manage billing records
- Track patient billing information

### Project Architecture

The project follows a layered architecture pattern for better maintainability and scalability.
```text
Client Request 

       ↓
Controller Layer 

       ↓
Service Layer

       ↓
Repository Layer
       ↓
MySQL Database
```

### Package Structure
    src/main/java 
    │ 
    ├── controllers 
    │ ├── AppointmentController
    │ ├── BillController 
    │ ├── DoctorController 
    │ └── PatientController 
    │ 
    ├── models 
    │ ├── Appointment 
    │ ├── Bill 
    │ ├── Doctor 
    │ └── Patient 
    │ 
    ├── repository 
    │ ├── AppointmentRepository 
    │ ├── BillRepository 
    │ ├── DoctorRepository 
    │ └── PatientRepository 
    │ 
    ├── service 
    │ ├── AppointmentService 
    │ ├── BillService 
    │ ├── DoctorService 
    │ └── PatientService 
    │ 
    └── HospitalManagementSystemApplication
    
### Configuration
### Database Configuration
Update the following properties in application.properties:
  ```bash
     spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.jpa.show-sql=true
     spring.jpa.hibernate.ddl-auto=update
```
### Running the Application
### Clone the Repository
```bash
git clone https://github.com/kumarchy/HospitalManagementSystem.git
```
### Navigate to the Project Directory
```bash
cd HospitalManagementSystem
```
### Build the Project
```bash
mvn clean install
```
### Run the Application
```bash
mvn spring-boot:run

Application will start on:

http://localhost:8080
```

### API Endpoints
### Patient APIs
| Method	| Endpoint | Description |
|---|---|---|
| GET	| /patients	| Get all patients | 
| GET	| /patients/{id}	| Get patient by ID | 
| POST | /patients	| Add new patient | 
| PUT	| /patients/{id}	| Update patient |  
| DELETE	| /patients/{id}	| Delete patient |

### Doctor APIs
| Method	| Endpoint	| Description |
|---|---|---|
| GET	| /doctors	| Get all doctors |
| POST	| /doctors	| Add doctor |
| PUT	| /doctors/{id}	| Update doctor |
| DELETE	| /doctors/{id}	| Delete doctor |

### Appointment APIs
| Method	| Endpoint	| Description
|---|---|---|
| GET	| /appointments	| Get appointments
| POST	| /appointments	| Create appointment
| DELETE	| /appointments/{id}	| Delete appointment

### Billing APIs
| Method	| Endpoint	| Description
|---|---|---|
| GET	| /bills	| Get all bills
| POST	| /bills	| Generate bill
| DELETE	| /bills/{id}	| Delete bill

### Monitoring
Spring Boot Actuator is enabled for monitoring and health checks.

### Health Endpoint
```bash
/actuator/health
```
### Exposed Endpoints
```bash
management.endpoints.web.exposure.include=*
```

### Key Learnings
- RESTful API development using Spring Boot
- Layered backend architecture implementation
- Database integration using JPA/Hibernate
- Entity and repository management
- Service-layer business logic implementation
- Backend configuration and monitoring
