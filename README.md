# Real-Time Chat

A real-time chat application developed in Java using JavaFX and WebSocket.

## Features

- Real-time chat using WebSocket
- Support for multiple chat rooms
- File sharing
- Modern graphical interface with JavaFX
- Message history
- Event notifications (connection, disconnection, etc.)

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/eduardowanderleyde/java_project.git
```

2. Enter the project directory:

```bash
cd java_project
```

3. Build the project:

```bash
mvn clean package
```

4. Start the server:

```bash
mvn exec:java -Dexec.mainClass="com.eduardowanderley.server.ChatServer"
```

5. In another terminal, start the client:

```bash
mvn javafx:run -Dprism.order=sw
```

## Project Structure

```
src/
├── main/
│   ├── java/com/eduardowanderley/
│   │   ├── controller/
│   │   │   └── ChatController.java
│   │   ├── model/
│   │   │   └── Message.java
│   │   ├── server/
│   │   │   └── ChatServer.java
│   │   ├── util/
│   │   │   └── LocalDateTimeAdapter.java
│   │   └── ChatApp.java
│   └── resources/
│       ├── fxml/
│       │   └── chat.fxml
│       └── styles/
│           └── chat.css
└── test/
    └── java/com/eduardowanderley/
        └── (tests)
```

## Technologies Used

- JavaFX for the graphical interface
- Java-WebSocket for real-time communication
- Gson for JSON serialization
- Maven for dependency management

## How to Use

1. Start the server
2. Start the client
3. Enter your name and click "Connect"
4. Enter the room name and click "Join Room"
5. Start chatting!
6. Use the "File" button to share files

## Contributing

Feel free to contribute to the project by submitting pull requests.
