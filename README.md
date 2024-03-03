# Concurrent Web Server with TCP

This project implements a concurrent web server in Java using the TCP (Transmission Control Protocol) protocol. It comprises two main classes: `Server` and `ServerWorker`.

## Overview

The web server allows multiple clients to connect simultaneously, enabling efficient handling of HTTP requests. The `Server` class initializes and manages the server socket, while the `ServerWorker` class handles each client connection in a separate thread.

### Features

- **Concurrency**: Handles multiple client connections concurrently using threads.
- **TCP Protocol**: Utilizes the TCP protocol for reliable data transmission.
- **Scalability**: Designed to scale with the number of incoming client connections.
- **Customizable**: Easily customizable and extensible to fit specific project requirements.
<br>
<br>
    # Commands

        .list: List all users connected to the char.
        .w (username): Send a whisper to a specific user. 
        .quit: Quit from the server.


## ServerWorker Class

The `ServerWorker` class represents a worker thread responsible for managing individual client connections. It performs the following tasks:

- Initializes input and output streams for communication with the client.
- Prompts the client for their name and notifies the server upon connection.
- Listens for incoming messages from the client.
- Implements commands such as `/list` to print the list of clients and `/quit` to disconnect a client.

## Server Class

The `Server` class is responsible for initializing the server socket, accepting incoming client connections, and managing the list of active client connections. Key functionalities include:

- Starts the server and listens for incoming client connections on a specified port.
- Accepts incoming client connections and assigns a `ServerWorker` thread to handle each connection.
- Implements methods for broadcasting messages to all clients, sending whispers to specific clients, and managing client disconnections.
