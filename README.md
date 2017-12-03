# Project: Mercury

A custom Internet transfer protocol using UDP for network traversal.

## How to Use

This project was created in NetBeans 8.2 - if you want to clone and open the project there, use all of the files.

If you would like to open the project in another IDE, just clone the `.java` files.

## Packet Structure

- 0-31 Sequence Number
- 32-41 Length
- 42-49 Flags
    - 42-43 Packet Type
    - 44-45 Protocol Type
    - 46-49 Window Size
- 50-1023 Payload
