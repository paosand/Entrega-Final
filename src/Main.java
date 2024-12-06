import inventario.Inventario;
import menu.Menu_Principal;
import producto.Producto;

import java.awt.*;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Inventario inventario = new Inventario();
        Menu menuPrincipal = new Menu();
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {

            Menu_Principal.menuPrincipal();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1 -> {
                    System.out.print("ID:");
                    String id = scanner.nextLine();
                    System.out.print("Nombre:");
                    String nombre = scanner.nextLine();
                    System.out.print("Categoría:");
                    String categoria = scanner.nextLine();
                    System.out.print("Precio:");
                    int precio = scanner.nextInt();
                    System.out.print("Cantidad disponible:");
                    int cantidad = scanner.nextInt();
                    inventario.agregarProducto(new Producto(id,nombre,categoria,precio,cantidad));
                }
                case 2 -> {
                    System.out.print("ID del producto a actualizar: ");
                    String id = scanner.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Nueva categoría: ");
                    String categoria = scanner.nextLine();
                    System.out.print("Nuevo precio: ");
                    int precio = scanner.nextInt();
                    System.out.print("Nueva cantidad: ");
                    int cantidad = scanner.nextInt();
                    inventario.actualizarProducto(id, new Producto(id,nombre,categoria,precio,cantidad));
                }
                case 3 -> {
                    System.out.print("ID del producto a eliminar: ");
                    String id = scanner.nextLine();
                    inventario.eliminarProducto(id);
                }
                case 4 -> {
                    inventario.mostrarSubMenuCategorias();
                }
                case 5 -> inventario.generarReporte();
                case 6 -> inventario.cantidadPorCategoria().forEach((cat, cant) ->
                        System.out.println(cat + ": " + cant));
                case 7 -> {
                    Producto caro = inventario.productoMasCaro();
                    System.out.println(caro != null ? caro : "No hay productos.");
                }
                case 8 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 8);
        scanner.close();
    }

}


