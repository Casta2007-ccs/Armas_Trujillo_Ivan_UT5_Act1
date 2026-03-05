
import model.nota;

import service.AuthService;
import service.NotaService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final NotaService notaService = new NotaService();
    private static String usuarioLogueado = null;

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            if (usuarioLogueado == null) {
                salir = menuPrincipal();
            } else {
                menuUsuario();
            }
        }
        System.out.println("Saliendo del programa...");
        scanner.close();
    }

    private static boolean menuPrincipal() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.println("0. Salir");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine();

        if (opcion.equals("1"))
            registrar();
        else if (opcion.equals("2"))
            login();
        else if (opcion.equals("0"))
            return true;
        else
            System.out.println("Opción incorrecta.");

        return false;
    }

    private static void registrar() {
        System.out.print("Introduce email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Introduce contraseña: ");
        String pass = scanner.nextLine().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            System.out.println("El email y la contraseña no pueden estar vacíos.");
            return;
        }

        if (authService.registrarUsuario(email, pass)) {
            System.out.println("Usuario registrado correctamente.");
        }
    }

    private static void login() {
        System.out.print("Introduce email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Introduce contraseña: ");
        String pass = scanner.nextLine().trim();

        if (authService.iniciarSesion(email, pass)) {
            usuarioLogueado = email;
            System.out.println("¡Hola, " + email + "!");
        } else {
            System.out.println("Credenciales incorrectas o usuario no existe.");
        }
    }

    private static void menuUsuario() {
        System.out.println("\n--- MENÚ DE " + usuarioLogueado + " ---");
        System.out.println("1. Crear nota");
        System.out.println("2. Listar notas");
        System.out.println("3. Ver nota por número");
        System.out.println("4. Eliminar nota (por número)");
        System.out.println("0. Cerrar sesión");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine();

        if (opcion.equals("1"))
            crearNota();
        else if (opcion.equals("2"))
            listarNotas();
        else if (opcion.equals("3"))
            verNota();
        else if (opcion.equals("4"))
            eliminarNota();
        else if (opcion.equals("0")) {
            usuarioLogueado = null;
            System.out.println("Sesión cerrada.");
        } else
            System.out.println("Opción incorrecta.");
    }

    private static void crearNota() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Contenido: ");
        String contenido = scanner.nextLine();

        // Evitar que el usuario rompa el archivo usando punto y coma
        titulo = titulo.replace(";", ",");
        contenido = contenido.replace(";", ",");

        nota nuevaNota = new nota(titulo, contenido);
        notaService.crearNota(usuarioLogueado, nuevaNota);
    }

    private static void listarNotas() {
        List<nota> notas = notaService.listarNotas(usuarioLogueado);
        System.out.println("\n--- TUS NOTAS ---");

        if (notas.isEmpty()) {
            System.out.println("No tienes notas aún.");
            return;
        }

        for (int i = 0; i < notas.size(); i++) {
            // Uso de printf para dar buen formato [cite: 6]
            System.out.printf("%d. %s\n", (i + 1), notas.get(i).getTitulo());
        }
    }

    private static void verNota() {
        listarNotas();
        System.out.print("\nIntroduce el número de la nota a ver: ");
        try {
            int numero = Integer.parseInt(scanner.nextLine());
            List<nota> notas = notaService.listarNotas(usuarioLogueado);

            if (numero >= 1 && numero <= notas.size()) {
                nota nota = notas.get(numero - 1);
                System.out.println("\n--- " + nota.getTitulo() + " ---");
                System.out.println(nota.getContenido());
                System.out.println("-------------------------");
            } else {
                System.out.println("Número no válido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número.");
        }
    }

    private static void eliminarNota() {
        listarNotas();
        System.out.print("\nIntroduce el número de nota a eliminar: ");
        try {
            int numero = Integer.parseInt(scanner.nextLine());
            notaService.eliminarNota(usuarioLogueado, numero);
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número.");
        }
    }
}