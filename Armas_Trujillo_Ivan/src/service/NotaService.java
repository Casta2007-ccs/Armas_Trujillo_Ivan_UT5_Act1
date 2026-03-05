package service;

import model.nota;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NotaService {

    private String getRutaNotas(String email) {
        return "data/usuarios/" + email + "/notas.txt";
    }

    public void crearNota(String email, nota nota) {
        String ruta = getRutaNotas(email);
        // Escritura secuencial al final del archivo [cite: 52]
        try (FileWriter writer = new FileWriter(ruta, true)) {
            writer.write(nota.toString() + "\n");
            System.out.println("Nota guardada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    public List<nota> listarNotas(String email) {
        List<nota> listaNotas = new ArrayList<>();
        String ruta = getRutaNotas(email);

        // Lectura con BufferedReader [cite: 58]
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";", 2);
                if (partes.length == 2) {
                    listaNotas.add(new nota(partes[0], partes[1]));
                }
            }
        } catch (IOException e) {
            // No pasa nada si el usuario aún no tiene notas
        }
        return listaNotas;
    }

    public void eliminarNota(String email, int numero) {
        Path ruta = Path.of(getRutaNotas(email));

        try {
            // 1) Leer todo el archivo a memoria [cite: 77]
            List<String> lineas = Files.readAllLines(ruta);

            if (numero < 1 || numero > lineas.size()) {
                System.out.println("Número de nota no válido.");
                return;
            }

            // 2) Modificar en memoria (eliminar la línea) [cite: 77]
            lineas.remove(numero - 1);

            // 3) Reescribir el archivo completo (try-with-resources) [cite: 77]
            try (BufferedWriter writer = Files.newBufferedWriter(ruta)) {
                for (String linea : lineas) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
            System.out.println("Archivo modificado correctamente (Nota eliminada).");

        } catch (IOException e) {
            System.out.println("Error al modificar el archivo: " + e.getMessage());
        }
    }
}