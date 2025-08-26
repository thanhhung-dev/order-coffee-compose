# OrderCoffee - Application

An internal order management system where customers place drink orders , and the baristas (using a Java Swing desktop app) can view and update the order status in real-time

## Features

- Onboarding
- Dashboard
- Live Chat
- Manager Order
- Manager Product
- Manager Categories
- Customer Database

## Tech Stack

### Frontend

- Java Swing
- FlatLaf - UI/UX
- Midlayout - setsize
- Retrofit to consume RESTful APIs
- JTable for displaying orders
### Backend
- Spring Boot 3
- Spring Data JPA
### Database 
- Mysql for data storage
### Deployment Infrastructure
- **Azure**: Cloud hosting
- **Docker & Docker Compose**: Containerized deployment
- **GitHub**: Source code version control
- **Postman**: API testing
## Quick Start with Docker

The fastest way to get started is using Docker Compose:

```bash
# Clone the repository
git clone [https://github.com/HungPig/OderCoffeeClient](https://github.com/HungPig/OderCoffeeClient)
cd order-coffee

# Start all services
cd src
docker-compose up -d
```

After the services are running, access:
- Desktop: http://localhost
- API: http://localhost:8080

## Running on Apple Silicon (M1/M2)

For Mac users with Apple Silicon (M1/M2) processors, our Docker configuration supports ARM64 architecture:

```bash
# Clone the repository
git clone https://github.com/HungPig/order-coffee-compose.git
cd src

# Edit docker-compose.yml to use ARM64 architecture
# Change BUILDPLATFORM values from linux/amd64 to linux/arm64

# Start all services
cd src
docker-compose up -d

### Specifying Platform in Docker Compose

There are multiple ways to specify the platform when running Docker Compose:

1. **Using environment variables** (recommended):
```bash
# For ARM64 (Apple M1/M2)
DOCKER_DEFAULT_PLATFORM=linux/arm64 docker-compose up -d

# For AMD64 (Intel/AMD processors)
DOCKER_DEFAULT_PLATFORM=linux/amd64 docker-compose up -d
```

2. **Using Docker Compose build arguments** (already configured):
```bash
# Edit the BUILDPLATFORM args in docker-compose.yml before running
docker-compose up --build
```

3. **Using Docker BuildKit**:
```bash
# Enable BuildKit
export DOCKER_BUILDKIT=1

# Set the platform
export BUILDPLATFORM=linux/arm64

# Run Docker Compose
docker-compose up --build
```

If you experience any architecture-related issues:

- Make sure Docker Desktop is updated to the latest version
- Verify that the BUILDPLATFORM is set to `linux/arm64` in the docker-compose.yml file
- Check that both Dockerfiles properly use the platform variable with `--platform=${BUILDPLATFORM}`

## Detailed Docker Setup

### Environment Configuration

The Docker setup uses environment variables to configure the services. The key variables are already set in the `docker-compose.yml` file, but you can customize them:

```yaml
# MySQL
MYSQL_DATABASE=ordercoffee
MYSQL_USER=root
MYSQL_PASSWORD=your_password

# Spring Boot
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/ordercoffee
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
```
## Troubleshooting Docker Setup

### Architecture-specific Issues

If encountering "exec format error" messages:
- Verify your architecture: `uname -m`
- Set the platform explicitly: `DOCKER_DEFAULT_PLATFORM=linux/arm64 docker-compose up -d`
- Update BUILDPLATFORM in docker-compose.yml accordingly
- Ensure base images support your architecture
- Try using multiarch images like `--platform=linux/arm64` for Apple Silicon

## Development Setup

### Frontend Development (Local)

```bash
# Open the project in your IDE (IntelliJ, NetBeans, Eclipse)

# Set JDK 17+ and build the project

# Run the main class to start the application
src/client/Ordercoffee.java
```

### Backend Development (Local)

```bash
cd src/server

# Run using Maven
./mvnw spring-boot:run

# Package as JAR
./mvnw package

# Run with specific profile
java -jar target/*.jar --spring.profiles.active=dev
```

## Environment Variables

### Frontend Environment

Frontend environment variables are set during build:
## Contributors

<!-- readme: contributors -start -->
<table>
	<tbody>
		<tr>
            <td align="center">
                <a href="https://github.com/HungPig">
                    <img src="https://avatars.githubusercontent.com/u/118031742?v=4" width="100;" alt="Liquidwe"/>
                    <br />
                    <sub><b>Hưng Thanh</b></sub>
                </a>
            </td>
            <td align="center">
                <a href="https://github.com/VanKhoa2301">
                    <img src="https://avatars.githubusercontent.com/u/199430332?v=4" width="100;" alt="fishTsai20"/>
                    <br />
                    <sub><b>Khoa Cao Van</b></sub>
                </a>
            </td>
            <td align="center">
                <a href="https://github.com/duc-tuan0207">
                    <img src="https://avatars.githubusercontent.com/u/154722842?v=4" width="100;" alt="lxcong"/>
                    <br />
                    <sub><b>Duc Tuan</b></sub>
                </a>
            </td>
	<tbody>
</table>
<!-- readme: contributors -end -->
## Document

Not yet

## Library Resources

- [FlatLaf](https://github.com/JFormDesigner/FlatLaf) - FlatLaf library for the modern UI design theme
- [MigLayout](https://github.com/mikaelgrev/miglayout) - MigLayout library for flexible layout management
