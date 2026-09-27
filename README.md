# Sistemas Distribuidos Agua

Aplicacion Java del Ejercicio 28: consumo de agua por habitante. El cliente solicita los litros consumidos durante un dia y los habitantes de la vivienda. El promedio se calcula exclusivamente en el servidor.

## Tecnologias

- Java 21
- Java Swing
- UDP/IP con `DatagramSocket` y `DatagramPacket`
- Arquitectura hexagonal
- NetBeans/Ant

## Estructura

Se conservan los dos proyectos independientes:

```text
ClienteConsumoAgua/src/clienteconsumoagua/
  domain/model       application       ports/input, ports/output
  adapters/udp       adapters/gui       Cliente.java
ServidorConsumoAgua/src/servidorconsumoagua/
  domain/model       domain/service     application
  ports/input        adapters/udp        Servidor.java
```

La GUI valida y envia la solicitud. El adaptador UDP interpreta la respuesta y el caso de uso del servidor delega el calculo a `CalculadorConsumoAgua`.

## Ejecucion

Ejecutar primero `servidorconsumoagua.Servidor` en `ServidorConsumoAgua` y despues `clienteconsumoagua.Cliente` en `ClienteConsumoAgua`.

```text
ant -f ServidorConsumoAgua/build.xml run
ant -f ClienteConsumoAgua/build.xml run
```

El servidor escucha en `localhost:5000` y el cliente usa un timeout de 3 segundos.

## Protocolo UDP

Solicitud: `litros;habitantes`, por ejemplo `1200;4`.

Respuesta exitosa: `OK;300.00`.

Respuesta de error: `ERROR;mensaje`.

## Pruebas

`1200` litros y `4` habitantes produce `300.00 litros por habitante`.

`1000` litros y `5` habitantes produce `200.00 litros por habitante`.

Los campos vacios, valores no numericos, cero y valores negativos se rechazan. La division no existe en el cliente: se ejecuta en el dominio del servidor.