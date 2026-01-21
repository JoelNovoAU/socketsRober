package com.example.socketsrober;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatCliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 8080;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(HOST, PUERTO);
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(), true
                );
                Scanner scanner = new Scanner(System.in)
        ) {

            Thread receptor = new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readLine()) != null) {
                        System.out.println("\n" + mensaje);
                        System.out.print(">> ");
                    }
                } catch (IOException e) {
                    System.out.println("🔴 Conexión cerrada");
                }
            });
            receptor.start();

            System.out.println("🟢 Conectado al chat");
            System.out.println("Escribe mensajes ('salir' para terminar)");

            while (true) {
                System.out.print(">> ");
                String mensaje = scanner.nextLine();
                salida.println(mensaje);

                if (mensaje.equalsIgnoreCase("salir")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
