# OrderCoffee - Application

An internal order management system where customers place drink orders, and the baristas (using a Java Swing desktop app) can view and update the order status in real-time.

## Features

- **Onboarding**: User-friendly system introduction
- **Enhanced Dashboard**: Real-time sales analytics and order statistics
- **Live Order Queue**: Real-time order management for baristas
- **Order Management**: Complete order lifecycle management
- **Product Management**: Comprehensive product catalog management
- **Category Management**: Product categorization system
- **Customer Database**: Customer information and order history
- **Table Management**: Restaurant table status tracking
- **Real-time Updates**: Auto-refreshing interfaces for up-to-date information

## Tech Stack

### Frontend
- **Java Swing**: Desktop application framework
- **FlatLaf**: Modern UI/UX theme library
- **MigLayout**: Flexible layout management
- **Retrofit**: HTTP client for consuming RESTful APIs
- **JTable**: Advanced data display components

### Backend
- **Spring Boot 3**: Enterprise Java framework
- **Spring Data JPA**: Data persistence layer
- **Swagger/OpenAPI**: Comprehensive API documentation
- **MySQL**: Relational database management
- **Hibernate**: Object-relational mapping

### Database 
- **MySQL 8.0**: Primary data storage with proper indexing
- **Connection Pooling**: Optimized database connections

### Deployment Infrastructure
- **Docker & Docker Compose**: Containerized deployment with multi-environment support
- **Azure**: Cloud hosting (production ready)
- **GitHub**: Source code version control with automated workflows
- **Environment Configuration**: Flexible deployment profiles
## Quick Start with Docker

The fastest way to get started is using Docker Compose:

```bash
# Clone the repository
git clone https://github.com/HungPig/order-coffee-compose.git
cd order-coffee-compose

# Navigate to the source directory
cd src

# Copy the environment template and customize if needed
cp .env.example .env

# Start all services (MySQL, Backend API, Desktop Client)
docker-compose up -d
```

After the services are running, access:
- **Desktop Application**: VNC viewer at `http://localhost:6080` (web-based) or VNC client at `localhost:5900`
- **API Documentation**: `http://localhost:8080/swagger-ui.html`
- **Backend API**: `http://localhost:8080/api`

## System Architecture

```
┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│   Desktop Client    │───▶│    Backend API      │───▶│      MySQL DB       │
│   (Java Swing)      │    │   (Spring Boot)     │    │   (Data Storage)    │
│                     │    │                     │    │                     │
│ • Order Queue       │    │ • Order Management  │    │ • Orders            │
│ • Sales Dashboard   │    │ • Customer CRUD     │    │ • Products          │
│ • Order Management  │    │ • Product CRUD      │    │ • Categories        │
│ • Real-time Updates │    │ • Inventory Mgmt    │    │ • Customers         │
└─────────────────────┘    │ • Swagger Docs      │    │ • Tables            │
                           └─────────────────────┘    └─────────────────────┘
```

## New Features & Enhancements

### 🚀 Enhanced Barista Interface
- **Real-time Order Queue**: Live display of pending and in-progress orders
- **One-click Status Updates**: Easy order workflow management (Pending → In-Progress → Completed)
- **Auto-refresh**: Updates every 30 seconds to keep information current
- **Order Details**: Complete order information including table, customer, items, and total

### 📊 Sales Dashboard
- **Real-time Analytics**: Daily sales statistics and revenue tracking
- **Order Breakdown**: Status-based order analysis with percentages
- **Performance Metrics**: Average order value, completion rates, and more
- **Visual Summary**: Color-coded statistics cards and detailed reports

### 🔧 Backend Improvements
- **Enhanced APIs**: New endpoints for order queue management
- **Order Validation**: Comprehensive validation with automatic price calculation
- **Inventory Management**: Product availability checking
- **Status Workflow**: Validated order status transitions
- **Customer Management**: Full CRUD operations for customer data
- **Swagger Documentation**: Complete API documentation at `/swagger-ui.html`

### 🏗️ Infrastructure Enhancements
- **Environment Profiles**: Separate development and production configurations
- **Docker Improvements**: Enhanced container setup with health checks
- **Database Optimization**: Proper indexing and sample data
- **Configuration Management**: Flexible environment variable setup

## API Endpoints

### Order Management
- `GET /api/order` - Get all orders
- `GET /api/order/{id}` - Get specific order
- `POST /api/order` - Create new order (with automatic pricing)
- `PATCH /api/order/{id}` - Update order
- `DELETE /api/order/{id}` - Delete order
- `PATCH /api/order/{id}/status` - Update order status
- `GET /api/order/pending` - Get pending orders
- `GET /api/order/in-progress` - Get in-progress orders
- `GET /api/order/status/{status}` - Get orders by status

### Customer Management
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get specific customer
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer
- `GET /api/customers/search?name={name}` - Search customers
- `GET /api/customers/email/{email}` - Find customer by email

### Product & Category Management
- Full CRUD operations for products and categories
- Image upload support for products
- Category-based product filtering

## Running on Apple Silicon (M1/M2)

For Mac users with Apple Silicon (M1/M2) processors, our Docker configuration supports ARM64 architecture:

```bash
# Clone the repository
git clone https://github.com/HungPig/order-coffee-compose.git
cd order-coffee-compose/src

# Edit .env file to specify ARM64 architecture
echo "BUILDPLATFORM=linux/arm64" >> .env

# Start all services
DOCKER_DEFAULT_PLATFORM=linux/arm64 docker-compose up -d
```

### Architecture Configuration Options

1. **Using environment variables** (recommended):
```bash
# For ARM64 (Apple M1/M2)
DOCKER_DEFAULT_PLATFORM=linux/arm64 docker-compose up -d

# For AMD64 (Intel/AMD processors)
DOCKER_DEFAULT_PLATFORM=linux/amd64 docker-compose up -d
```

2. **Using .env configuration**:
```bash
# Edit .env file
BUILDPLATFORM=linux/arm64  # or linux/amd64
docker-compose up --build
```

## Development Setup

### Prerequisites
- **Java 17+** (for backend development)
- **Maven 3.6+** (for dependency management)
- **MySQL 8.0+** (for local database)
- **Docker & Docker Compose** (for containerized deployment)
- **Git** (for version control)

### Backend Development (Local)

```bash
# Navigate to backend directory
cd src/server/OrderCoffeeBE

# Install dependencies and compile
./mvnw clean compile

# Run with development profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run with specific profile
java -jar target/*.jar --spring.profiles.active=dev

# Package as JAR for production
./mvnw clean package
```

### Frontend Development (Local)

```bash
# Open the project in your IDE (IntelliJ IDEA, NetBeans, Eclipse)
# Set JDK 17+ and build the project

# Run the main class to start the application
# Main class: Order.Modal.OrderMain
# Location: src/client/OderCoffeeClient/src/main/java/Order/Modal/OrderMain.java
```

### Database Setup

For local development, ensure MySQL is running and create the database:

```sql
CREATE DATABASE order_coffee CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

The application will automatically create tables and populate sample data when using the `dev` profile.

## Environment Variables

### Production Environment (.env file)
```bash
# Database Configuration
MYSQL_ROOT_PASSWORD=your_secure_password
MYSQL_DATABASE=order_coffee
MYSQL_USER=coffeeuser
MYSQL_PASSWORD=secure_user_password

# Application Configuration
SPRING_PROFILES_ACTIVE=prod
BACKEND_PORT=8080
VNC_PORT=5900
NOVNC_PORT=6080

# Build Configuration
BUILDPLATFORM=linux/amd64
VERSION=latest
```

### Development Environment
```bash
# Use development profile for local development
SPRING_PROFILES_ACTIVE=dev

# Enable debug logging
SPRING_JPA_SHOW_SQL=true
LOGGING_LEVEL_COM_EXAMPLE_ORDERCOFFEEBE=DEBUG
```

## Contributors

<!-- readme: contributors -start -->
<table>
	<tbody>
		<tr>
            <td align="center">
                <a href="https://github.com/HungPig">
                    <img src="https://avatars.githubusercontent.com/u/118031742?v=4" width="100;" alt="HungPig"/>
                    <br />
                    <sub><b>Hưng Heo</b></sub>
                </a>
            </td>
            <td align="center">
                <a href="https://github.com/VanKhoa2301">
                    <img src="https://avatars.githubusercontent.com/u/199430332?v=4" width="100;" alt="VanKhoa2301"/>
                    <br />
                    <sub><b>Khoa Cao Van</b></sub>
                </a>
            </td>
            <td align="center">
                <a href="https://github.com/duc-tuan0207">
                    <img src="https://avatars.githubusercontent.com/u/154722842?v=4" width="100;" alt="duc-tuan0207"/>
                    <br />
                    <sub><b>Duc Tuan</b></sub>
                </a>
            </td>
		</tr>
	</tbody>
</table>
<!-- readme: contributors -end -->

## Troubleshooting

### Common Issues

#### Docker Architecture Issues
If encountering "exec format error" messages:
- Verify your architecture: `uname -m`
- Set the platform explicitly: `DOCKER_DEFAULT_PLATFORM=linux/arm64 docker-compose up -d`
- Update BUILDPLATFORM in .env file accordingly
- Ensure base images support your architecture

#### Database Connection Issues
- Ensure MySQL container is healthy: `docker-compose ps`
- Check database credentials in .env file
- Verify network connectivity: `docker-compose logs mysql`

#### Application Startup Issues
- Check backend logs: `docker-compose logs server`
- Ensure all required environment variables are set
- Verify Java version compatibility (17+)

### Development Troubleshooting

#### Backend Compilation Issues
```bash
# Clean and rebuild
cd src/server/OrderCoffeeBE
./mvnw clean compile

# Check Java version
java -version  # Should be 17+
```

#### Database Schema Issues
```bash
# Reset database (development only)
docker-compose down -v
docker-compose up -d mysql
# Application will recreate schema on restart
```

## Performance Optimization

### Database Performance
- Indexes are automatically created for frequently queried columns
- Connection pooling is configured for optimal performance
- Use `prod` profile for production deployments

### Application Performance
- Desktop client uses auto-refresh with configurable intervals
- API responses are optimized with proper HTTP caching
- Docker containers use multi-stage builds for smaller images

## Security Considerations

### Production Deployment
- Change default passwords in `.env` file
- Use strong database credentials
- Configure HTTPS for production API access
- Limit network access to required ports only

### Development Security
- Development profile includes debug features - not for production
- Sample data includes test credentials - remove in production
- API documentation is enabled - disable in sensitive environments

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, please open an issue on GitHub or contact the development team.

## Changelog

### Version 1.2.0 (Latest)
- ✅ Enhanced barista interface with real-time order queue
- ✅ Sales dashboard with analytics and reporting
- ✅ Customer management system
- ✅ Improved order validation and pricing
- ✅ Swagger API documentation
- ✅ Multi-environment Docker setup
- ✅ Database optimization and sample data

### Version 1.1.1 (Previous)
- Basic order management
- Product and category management
- Simple table management
- Basic Docker setup

## Library Resources

- [FlatLaf](https://github.com/JFormDesigner/FlatLaf) - Modern UI design theme for Java Swing
- [MigLayout](https://github.com/mikaelgrev/miglayout) - Flexible layout management for Java applications
- [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client for Android and Java
- [Spring Boot](https://spring.io/projects/spring-boot) - Enterprise Java application framework
- [Swagger/OpenAPI](https://swagger.io/) - API documentation and testing framework
