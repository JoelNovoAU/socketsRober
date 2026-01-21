package com.example.socketsrober;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientFXController {

    @FXML
    private TextArea areaChat;

    @FXML
    private TextField campoMensaje;

    private static final String HOST = "localhost";
    private static final int PUERTO = 8080;

    private PrintWriter salida;
    private BufferedReader entrada;
    private String nombre;

    @FXML
    public void initialize() {
        pedirNombre();
        conectarAlServidor();
    }

    private void pedirNombre() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombre de usuario");
        dialog.setHeaderText("Introduce tu nombre");
        dialog.setContentText(null);

        nombre = dialog.showAndWait().orElse("Anónimo");
    }

    private void conectarAlServidor() {
        try {
            Socket socket = new Socket(HOST, PUERTO);

            entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            salida = new PrintWriter(
                    socket.getOutputStream(), true
            );

            salida.println(nombre);

            areaChat.appendText("Conectado como " + nombre + "\n");

            Thread receptor = new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readLine()) != null) {
                        String mensajeFinal = mensaje;
                        Platform.runLater(() ->
                                areaChat.appendText(mensajeFinal + "\n")
                        );
                    }
                } catch (IOException e) {
                    Platform.runLater(() ->
                            areaChat.appendText("Conexión cerrada\n")
                    );
                }
            });

            receptor.setDaemon(true);
            receptor.start();

        } catch (IOException e) {
            areaChat.appendText("❌ No se pudo conectar al servidor\n");
        }
    }

    @FXML
    private void enviarMensaje() {
        String mensaje = campoMensaje.getText().trim();
        if (!mensaje.isEmpty()) {
            salida.println(mensaje);
            campoMensaje.clear();
        }
    }
}
