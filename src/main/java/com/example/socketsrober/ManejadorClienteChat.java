package com.example.socketsrober;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejadorClienteChat implements Runnable {

    private final Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private String nombre;

    public ManejadorClienteChat(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            salida = new PrintWriter(
                    socket.getOutputStream(), true
            );

            nombre = entrada.readLine();
            System.out.println("Cliente conectado: " + nombre);

            enviarATodos( nombre + " se ha unido al chat");

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {

                if (mensaje.equalsIgnoreCase("salir")) {
                    enviarATodos( nombre + " salió del chat");
                    break;
                }

                enviarATodos(nombre + ": " + mensaje);
            }

        } catch (IOException e) {
            System.err.println("Error con cliente " + nombre);
        } finally {
            ServidorChat.clientes.remove(this);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void enviarATodos(String mensaje) {
        for (ManejadorClienteChat cliente : ServidorChat.clientes) {
            cliente.salida.println(mensaje);
        }
    }
}