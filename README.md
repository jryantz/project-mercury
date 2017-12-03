# Project: Mercury

A custom Internet transfer protocol using UDP for network traversal.

## How to Use

This project was created in NetBeans 8.2 - if you want to clone and open the project there, use all of the files.

If you would like to open the project in another IDE, just clone the `.java` files.

## Packet Structure

`
|‾‾‾‾‾‾‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
|  Sequence #  |  Length  | Flag 1 | Flag 2 |   Flag 3   |                              Payload                               |
|     32 b     |   10 b   |  2 b   |  2 b   |    4 b     |                                1024 b                              |
|______________|__________|________|________|____________|____________________________________________________________________|
`
