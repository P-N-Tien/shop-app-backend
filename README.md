# Shop App Backend - Spring Boot API

The robust backend engine for the Shop App, built with Java 22 and Spring Boot 3.4.4. This API handles everything from
secure JWT authentication to complex order processing and payment integration.

> 🔗 Live API: https://shop-app-mdax.onrender.com

> [!NOTE]
> The backend is hosted on a free tier on Render; please wait a few minutes for the server to restart if the API is
> initially unresponsive.

## Tech Stack

Language: Java 22 (Latest features)
Framework: Spring Boot 3.4.4
Database: PostgreSQL (Hosted on Neon.tech)
Security: Spring Security & JWT (JSON Web Token)
Media Management: Cloudinary (Product images storage)
DevOps: Docker & Docker Compose
Deployment: Render

## Key Features

Security: State-of-the-art authentication with JWT and secure Cookie handling.
RESTful APIs: Clean and scalable endpoints for Categories, Products, and Orders.
Payment Integration: Ready-to-use logic for VNPay and COD workflows.
Image Processing: Automatic image upload and optimization via Cloudinary API.
Containerization: Fully Dockerized for "write once, run anywhere" convenience.

## Running with Docker

Quickly spin up the entire environment (Backend + Postgres) using Docker Compose:

docker-compose up -d

## Local Installation

Clone the project:

> git clone git@github.com:P-N-Tien/shop-app-backend.git
> cd shop-app-backend


