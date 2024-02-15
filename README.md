# Message exchange with AES & Diffie-Hellman algorithm 

## About the project
This project was made combining materials from two courses, Object Oriented Programming 2 and Security in Information Systems. 
It uses client-server architecture, where the communication between clients, Alice and Bob, is established through a TCP protocol.
The Graphical User Interface (GUI) is developed using the JavaFX framework. The communication channels are managed through the use of the Socket class and they are implemented to work locally.

## Code structure
- **ClientKey** class represents client's private key that is being sent to the server as soon as the connection is made from Client class.
- **Client** is base class for Alice and Bob classes. Besides UI logic it also uses several threads that are communicating with server using Sockets. Client invokes public method decrypt() from class Messages, that decrypts encrypted message from server.
- **Server** class acts as the central communication hub, coordinating the key exchange between clients and managing the secure message exchange. When client is connected, server adds client's private key to the map clientKeys that is being accesed by Message class
 to generate a symetric key that will be used for AES algorithm.It also creates separate threads, each handling the communication with a specific client. In this case only Alice and Bob.
- **ServerThread** class represents a thread on the server responsible for handling communication with a specific client. It ensures secure message exchange by encrypting messages using the AES algorithm.
- **Message** class handles the creation, encryption, and decryption logic of messages exchanged between clients.
- **Alice/Bob** classes represent two clients that should be started separately.

## Encryption
This program uses AES encryption algorithm with 128-bit key size. **p** and **q** are 128-bit prime numbers that are used for generating a shared key using Diffie-Hellman algorithm. Mode of encryption used is ECB, with PKCS5Padding, which fills bytes of plaintext 
in case it has smaller size in comparison to block size. After Encryption, program uses Base64 encoding on array of bytes. Similarly, the decryption process invokes the decryption method using AES/ECB/PKCS5Padding and returns a string from the decrypted array of bytes.

## How to run
To run this project, compile files using Java compiler. Since this application is created with client-server architecture, meaning it allows communication between several different programs at the same time, you should fist run Server class, followed by 
Alice and Bob classes. Last part is sending message from one client (Alice or Bob) to the other by clicking the "Send message" button below textfield. Server will display message in it's encrypted format and who has sent it. Other client will recieve message and show 
it's decrypted value, with sender's info.
