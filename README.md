# Mobile Wallet Application

## Overview

This Mobile Wallet Application allows users to create and manage their mobile wallets, facilitating various transactions like sending and receiving money. The application provides a robust solution to track and view transaction histories, ensuring data integrity and availability through Kafka streams. The backend utilizes a PostgreSQL database to store user and transaction data securely and efficiently.

## Features

- **Wallet Creation**: Users can create a personal mobile wallet securely.
- **Money Transactions**: Support for sending and receiving money between wallets.
- **Transaction History**: View detailed logs of all transactions conducted through the wallet.
- **Real-time Processing**: Utilize Kafka for real-time transaction processing and reliable data handling.
- **Data Storage**: Leverage PostgreSQL for efficient data storage and retrieval.

## Technology Stack

- **Backend**: Spring Boot (Java)
- **Message Broker**: Apache Kafka for event streaming and transaction logging.
- **Database**: PostgreSQL, providing robust data management and query capabilities.
- **Deployment**: AWS (Elastic Beanstalk/EC2), ensuring scalable and reliable hosting.
- **API Documentation**: Swagger/OpenAPI for clear and interactive API documentation.

## Getting Started

### Prerequisites

- Java JDK 17+
- Maven 3.8+
- Docker (for running PostgreSQL and Kafka locally)
- AWS CLI (optional, for deployment)

### Setup and Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourgithubusername/mobile-wallet-app.git
   cd mobile-wallet-app

2. **Start PostgreSQL and Kafka**
    ```bash
   docker-compose up -d  # Make sure you have a docker-compose.yml configured

3. **Build the application**
   ```bash
   mvn clean install
   
4. **Run the application**
   ```bash
   java -jar target/wallet-app-0.0.1-SNAPSHOT.jar

## Configuration
> Configure the application.properties file to point to your PostgreSQL instance and Kafka brokers:
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/wallet
spring.datasource.username=postgres
spring.datasource.password=password
spring.kafka.bootstrap-servers=localhost:9092
