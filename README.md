# Akka Persistence Protobuf Example

Prototypes an akka persistence project including *google protocol buffers (protobuf)*

## Description

Project is written in **scala**. Used **akka persistence** toolkit.

SBT and prepared postgres-server required

- [Prepare your PostgreSQL-server](https://github.com/WegenenVerkeer/akka-persistence-postgresql) (or use different journal and snapshot plugin)
- See config located in ``./src/main/resources/application.conf`` and opt. change PostgreSQL-server config (or use different journal and snapshot plugin)
- Run project with ``sbt run``

### Used technologies

- Akka Http: *Http server*
- Akka Persistence: *Event sourcing*
- Google Protocol Buffers (protobuf): *Serializer* (scalaPB - sbt plugin - used)

### Important files

- ``./src/main/protobuf/GarageAggregate.proto``: *Protobuf definition*
- ``./src/main/scala/de/mb/akka/protobufexample/utils/ProtobufSerializer.scala``: *Serializer definition*
- ``./src/main/resources/application.conf``: *Serializer binding*

### Flow

1. Protobuf compiler (scalaPB) compiles scala-classes from ``*.proto``-files
2. Project compiles

### Journal and snapshot-storage

There are many possibilities to run journal and snapshot-storage. In this project is included:

- [PostgreSQL journal and snapshot-storage](https://github.com/WegenenVerkeer/akka-persistence-postgresql)

You can extend this project with any other journal and snapshot-storage extension(s)