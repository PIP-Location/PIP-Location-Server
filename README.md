# 📌 pinpick

A new Spring Boot project.

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Gradle 8+
- MySQL 8+

### 📦 Tech Stack
#### ✅ Language & Framework
- Java 17
- Spring Boot 3.3.8

#### 💾 Database & ORM
- MySQL
- Spring Data JPA
- QueryDSL 5.0.0

#### 🔒 Security
- Spring Security
- JWT

[//]: # (☁️ Cloud & Storage)

[//]: # (Azure Blob Storage &#40;Spring Cloud Azure 5.14.0&#41;)

#### 📄 API Documentation
- Springdoc OpenAPI 2.2.0 (Swagger UI)

#### 🧪 Testing
- JUnit 5
- Spring Boot Test
- Spring Security Test
- Rest Assured
- Testcontainers (MySQL, JUnit)

### 📁 Project Structure
```
src
└── main
    ├── java
    │   └── kr.co.pinpick
    │       ├── archive              
    │       │   ├── controller       
    │       │   ├── dto              
    │       │   ├── entity           
    │       │   ├── repository       
    │       │   └── service          
    │       ├── common               
    │       │   ├── aspect           
    │       │   ├── dto              
    │       │   ├── entity           
    │       │   ├── error            
    │       │   ├── extension        
    │       │   ├── oauth            
    │       │   ├── rule             
    │       │   ├── s3               
    │       │   ├── security         
    │       │   └── service          
    │       ├── config               
    │       ├── user                 
    │       │   ├── controller       
    │       │   ├── dto              
    │       │   ├── entity           
    │       │   ├── repository       
    │       │   └── service          
    │       └── PinpickApplication   
    └── resources
``` 

### 📚 Useful Links
- [Spring Boot Docs](https://docs.spring.io/spring-boot/)
- [Spring Security Docs](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
- [Springdoc OpenAPI](https://springdoc.org/)