# JPA Library Management System

A sample Java project demonstrating JPA/Hibernate usage in a library management context.

## Learning Objectives

- Understanding JPA Entity relationships
- Working with JPA repositories
- Managing database transactions
- Implementing domain models
- Writing unit tests for persistence layer

## Prerequisites

- Java Development Kit (JDK) 21
- Maven 3.9+
- Your favorite IDE (VS Code, IntelliJ IDEA, or Eclipse)
- Git

## Quick Start

1. Clone the repository:

    ```bash
    git clone https://github.com/ebpro/sample-jpalibrary.git
    cd sample-jpalibrary
    ```

2. Build the project:

    ```bash
    ./mvnw clean verify
    ```

3. Run the application:

    ```bash
    mvn exec:java -Dexec.mainClass="fr.univtln.bruno.samples.jpa.App"
    ```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── fr/univtln/bruno/samples/jpa/
│   │       ├── model/
│   │       │   ├── documents/
│   │       │   ├── users/
│   │       │   └── utils/
│   │       └── App.java
│   └── resources/
│       └── META-INF/
│           └── persistence.xml
└── test/
    └── java/
        └── fr/univtln/bruno/samples/jpa/
```

## Key Concepts Demonstrated

- JPA Entity relationships (OneToMany, ManyToOne, ManyToMany)
- Inheritance mapping
- Identifiers and primary keys
- Data generation for testing
- Lombok usage for reducing boilerplate

## Development Guidelines

1. Follow the existing package structure
2. Use Lombok annotations to reduce boilerplate
3. Write unit tests for new features
4. Document your code with Javadoc
5. Follow Java naming conventions

## Database

- Using H2 database launch in application.
- Database console available at `http://localhost:8082`
- Using H2 in-memory database for testing

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.