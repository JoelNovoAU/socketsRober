package com.example.socketsrober;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorChat {

    private static final int PUERTO = 8080;
    private static final int MAX_CLIENTES = 10;

    protected static Set<ManejadorClienteChat> clientes =
            ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES);
        System.out.println("Servidor de chat iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {

            while (true) {
                Socket socket = serverSocket.accept();

                ManejadorClienteChat manejador =
                        new ManejadorClienteChat(socket);

                clientes.add(manejador);
                pool.execute(manejador);
            }

        } catch (IOException e) {
            System.err.println("Error en servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
