# 🌐 Multi-Threaded Java HTTP Server

A lightweight HTTP server built from scratch using Java sockets and multithreading concepts. The server handles multiple clients concurrently, supports HTTP GET requests, serves static HTML pages, and provides logging with response time measurement.

## 📷 Demo
<img width="1325" height="904" alt="image" src="https://github.com/user-attachments/assets/9c69790d-0cf1-4e7b-bae6-90719ff4306c" />
<img width="949" height="652" alt="image" src="https://github.com/user-attachments/assets/1ed35beb-1d0f-459d-9bc8-c4cefafd1d9d" />


---

## ✨ Features

* Handle multiple clients concurrently using a thread-per-request architecture.
* Support HTTP GET requests.
* Serve static HTML files.
* Measure response time for each request.
* Log timestamps, thread names, and client IP addresses.
* Custom 404 error handling.
* Demonstrate low-level HTTP and TCP communication without external frameworks.

---

## 🛠 Technologies

* Java
* TCP Sockets
* Multithreading
* HTTP Protocol
* File I/O

---

## 📚 Key Concepts

* Client-Server Architecture
* Socket Programming
* Concurrent Request Handling
* HTTP Request / Response Cycle
* Performance Monitoring
* Logging and Error Handling

---

## 📂 Project Structure

```text
src/com/webserver/    Java source files
www/                  Static HTML pages
start_server.bat      Server launcher
README.md             Project documentation
```

---

## 🚀 Run

### 1. Compile

```bash
javac src/com/webserver/*.java
```

### 2. Run

```bash
java com.webserver.Server
```

or

```bash
start_server.bat
```

### 3. Open in Browser

```text
http://localhost:8080
```

---

## 📝 Example Log

```text
[10:32:15] Thread-3 | 127.0.0.1 | GET /index.html | 4 ms
[10:32:20] Thread-5 | 127.0.0.1 | GET /about.html | 3 ms
```

---

## 🔮 Future Improvements

* Support HTTP POST requests.
* Implement Thread Pool architecture.
* Add HTTPS/TLS support.
* Support file uploads.
* Improve caching and performance.

---

## 🎯 Learning Outcomes

This project was developed to gain hands-on experience with:

* Java socket programming
* HTTP protocol internals
* Multithreading and concurrent processing
* Client-server architecture
* Performance monitoring and logging

It provides a practical understanding of how web servers process multiple requests simultaneously.
