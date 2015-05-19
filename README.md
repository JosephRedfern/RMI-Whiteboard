Java RMI-based Whiteboard
=========================

This software is the result of a mini-project for the "Emerging Technologies" module at Cardiff University.

The UI is lacking - the exercise was not in Swing UI development, but in the RMI side of things.

The main method for the server is held in ServerApp - it uses a self-hosted RMI Registry.

The main method for the client is contained in WhiteboardClient - the URL of the RMI Registry can be passed in a as a command line argument.


# Basic Architecture
Clients connect to the server, passing themselves in during registration.

Server periodiccally "pings" each client to ensure that they are still online - if no response is recieved, they are removed from the list of active clients, with all other clients being notified of this fact.

When a client draws a shape, it is sent to the server, after being wrapped in a class containing the shape, the client that drew the shape and the time at which it was drawn. The server notfiies all other clients on receipt of this shape, sending only the new shape to the clients (to save bandwidth).

Much is to be desired from a security point of view - while some (very) basic protections have been employed, hardening was outside of the scope of this acedemic exercise.
