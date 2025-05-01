# Real-Time Tic-Tac-Toe Game  🎲

[![Java Version](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Framework](https://img.shields.io/badge/Framework-Spring%20Boot-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build Tool](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE) <!-- Add a LICENSE file (e.g., MIT) -->

A simple, web-based, real-time Tic-Tac-Toe game built using Java Spring Boot and WebSockets. Play against a friend in your browser!

This project demonstrates basic WebSocket communication for a turn-based game, making it a great starting point for learning about real-time web applications.


## ✨ Features

*   **Real-time Gameplay:** Play Tic-Tac-Toe instantly with another player using WebSockets.
*   **Two-Player Mode:** Designed for two players connecting from different browser tabs or windows.
*   **Simple Web Interface:** Clean and intuitive UI built with basic HTML, CSS, and JavaScript.
*   **Win/Draw Detection:** Automatically detects winning moves and draw conditions.
*   **Turn Management:** Clearly indicates whose turn it is to play.
*   **Player Identification:** Prompts players for their names upon joining.
*   **Auto-Reset:** The game board resets automatically after a win or draw, ready for a new round.

## 💻 Technology Stack

*   **Backend:** Java 17, Spring Boot (Web, WebSocket)
*   **Frontend:** HTML, CSS, JavaScript
*   **Build Tool:** Maven
*   **Real-time Communication:** WebSockets

## 🚀 Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

*   **Java Development Kit (JDK):** Version 17 or later. [Download JDK](https://adoptium.net/)
*   **Maven:** Apache Maven (optional, as the project includes a Maven Wrapper). [Download Maven](https://maven.apache.org/download.cgi)
*   **Git:** Version control system. [Download Git](https://git-scm.com/downloads)

### Installation & Running

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/shubhambhagat3226-tic-tac-toe.git
    cd shubhambhagat3226-tic-tac-toe
    ```

2.  **Build and Run the application using Maven Wrapper:**

    *   On macOS/Linux:
        ```bash
        ./mvnw spring-boot:run
        ```
    *   On Windows:
        ```bash
        .\mvnw.cmd spring-boot:run
        ```
    This command will download dependencies, compile the code, and start the Spring Boot application.

3.  **Access the game:**
    Open your web browser and navigate to:
    `http://localhost:8080`

## ▶️ How to Play

1.  Open `http://localhost:8080` in **two separate browser tabs or windows**. These will represent Player 1 and Player 2.
2.  In each tab, you will be prompted to **enter your name**. Enter a unique name for each player.
3.  The first player to connect will be assigned 'X', and the second player will be 'O'.
4.  The game interface will indicate whose turn it is (`"Your turn"` or `"[Opponent's Name] turn"`).
5.  Click on an empty cell on the board to make your move when it's your turn.
6.  The board updates in real-time for both players.
7.  The game ends when a player gets three of their marks in a row (horizontally, vertically, or diagonally) or when all cells are filled (a draw).
8.  An alert message will announce the winner or if it's a draw.
9.  After a short delay, the board will automatically clear, ready for a new game. The turn order remains the same (X starts).

## 📁 Project Structure
```
shubhambhagat3226-tic-tac-toe/
├── .mvn/ 
├── src/
│ ├── main/
│ │ ├── java/com/dct/Tic_Tac_Toe/ # Java source code
│ │ └── resources/
│ │ ├── static/ # Frontend files (HTML, CSS, JS)
│ │ └── application.properties # Spring Boot configuration
│ └── test/ # Unit tests
├── mvnw # Maven Wrapper script (Linux/macOS)
├── mvnw.cmd # Maven Wrapper script (Windows)
└── pom.xml # Maven Project Object Model (dependencies, build config)
```

## 🤝 Contributing

Contributions are welcome! If you'd like to improve the game or add features:

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Happy Gaming! 😊
