package service;

import utils.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.*;
import java.io.IOException;

public class AuthService {
    private final Path carpetaData = Path.of("data");
    private final Path ficheroUsers = carpetaData.resolve("user.txt");

    public AuthService() {
        inicializarSistema();
    }

    private void inicializarSistema() {
        try {
            if (!Files.exists(carpetaData)) {
                Files.createDirectories(carpetaData);
            }
            if (!Files.exists(ficheroUsers)) {
                Files.createFile(ficheroUsers);
            }
        } catch (IOException e) {
            System.out.println("Error crítico inicializando ficheros: " + e.getMessage());
        }
    }

    public boolean registrarUsuario(String email, String password) {
        if (email.isBlank() || password.isBlank())
            return false;
        if (usuarioExiste(email)) {
            System.out.println("Error: El usuario ya está registrado.");
            return false;
        }
        String hashedPassword = SecurityUtils.hashPassword(password);
        String linea = email + ";" + hashedPassword;

        try (BufferedWriter writer = Files.newBufferedWriter(ficheroUsers, StandardOpenOption.APPEND)) {
            writer.write(linea);
            writer.newLine();

            Path carpetaUsuario = carpetaData.resolve("usuarios").resolve(email);
            Files.createDirectories(carpetaUsuario);
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean iniciarSesion(String email, String password) {
        String hashedInput = SecurityUtils.hashPassword(password);

        try (BufferedReader reader = Files.newBufferedReader(ficheroUsers)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 2 && partes[0].equals(email) && partes[1].equals(hashedInput)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer usuarios: " + e.getMessage());
        }
        return false;
    }

    private boolean usuarioExiste(String email) {
        try (BufferedReader reader = Files.newBufferedReader(ficheroUsers)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.split(";")[0].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {

        }
        return false;
    }
}