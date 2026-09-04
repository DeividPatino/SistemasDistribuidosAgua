/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servidorconsumoagua;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author patino
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    
    private static final int PUERTO = 5000;
    
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   SERVIDOR - CONSUMO DE AGUA");
        System.out.println("=================================");
        System.out.println("Iniciando servidor...");

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {

            System.out.println("Servidor iniciado correctamente.");
            System.out.println("Escuchando en el puerto " + PUERTO);
            System.out.println("Esperando conexiones...");
            System.out.println();

            // El servidor permanece activo esperando clientes
            while (true) {

                try (
                    Socket socket = servidor.accept();
                    DataInputStream entrada =
                            new DataInputStream(socket.getInputStream());
                    DataOutputStream salida =
                            new DataOutputStream(socket.getOutputStream())
                ) {

                    System.out.println("---------------------------------");
                    System.out.println("Cliente conectado.");

                    // Recibir datos enviados por el cliente
                    double litrosTotales = entrada.readDouble();
                    int habitantes = entrada.readInt();

                    System.out.println(
                            "Litros consumidos: " + litrosTotales
                    );

                    System.out.println(
                            "Cantidad de habitantes: " + habitantes
                    );

                    // Cálculo realizado EXCLUSIVAMENTE en el servidor
                    double promedio = litrosTotales / habitantes;

                    System.out.println(
                            "Calculando consumo promedio..."
                    );

                    // Enviar resultado al cliente
                    salida.writeDouble(promedio);
                    salida.flush();

                    System.out.println(
                            "Resultado enviado al cliente."
                    );

                    System.out.printf(
                            "Consumo promedio: %.2f litros por habitante.%n",
                            promedio
                    );

                    System.out.println("Cliente desconectado.");
                    System.out.println(
                            "Esperando una nueva conexión..."
                    );
                }

            }

        } catch (IOException e) {

            System.out.println(
                    "Error en el servidor: " + e.getMessage()
            );
        }
    }
}