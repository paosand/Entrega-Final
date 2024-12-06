package inventario;

import producto.Producto;

import java.io.*;
import java.util.*;

public class Inventario {

    List<Producto> productos = new ArrayList<>();
    private static final String archivoInventario = "productos.txt";

    // Constructor: Carga los productos desde el archivo

    public Inventario() {
        cargarProductos();
    }

    //Metodo cargar productos

    public void cargarProductos() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoInventario))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\s*,\\s*");
                if (partes.length == 5) {
                    productos.add(new Producto(
                            partes[0],
                            partes[1],
                            partes[2],
                            Integer.parseInt(partes[3]),
                            Integer.parseInt(partes[4])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el inventario: " + e.getMessage());
            System.out.println("*********************************************");

        }
    }

    // Metodo Agregar Producto

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        guardarCambios();
    }

    // Metodo Actualizar producto


    public void actualizarProducto(String id, Producto nuevoProducto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(id)) {
                productos.set(i, nuevoProducto);
                guardarCambios();
                return;
            }
        }
        System.out.println("Producto no encontrado.");
        System.out.println("*********************************************");

    }

    // metodo Eliminar producto

    public void eliminarProducto(String id) {
        productos.removeIf(p -> p.getId().equals(id));
        guardarCambios();
    }

    public List<String> buscarPorCategoria(String palabra) {
        List<String> lineasEncontradas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoInventario))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.contains(palabra)) {
                    lineasEncontradas.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar Palabra clave: " + e.getMessage());
            System.out.println("*********************************************");

        }

        return lineasEncontradas;
    }

    public void mostrarSubMenuCategorias() {
        // 1. Extraer del archivo todas la categorias
        Set<String> categorias = new HashSet<>();
        for (Producto producto : productos) {
            categorias.add(producto.getCategoria());
        }

        Set<String> ids = new HashSet<>();
        for (Producto producto : productos) {
            ids.add(producto.getId());
        }

        // 2. Presentar el menú con las categorias que existen
        Scanner scanner = new Scanner(System.in);
        System.out.println("*********************************************");
        System.out.println("Seleccione su tipo de busqueda:");
        System.out.println("1. Por categoría de productos");
        System.out.println("2. Por nombre de producto");
        System.out.println("3. Por id del producto");
        int tipo;
        tipo = scanner.nextInt();

            switch (tipo) {
                case 1 -> {
                    int opcion;
                    System.out.println("*********************************************");
                        System.out.println("----CATEGORIAS----");
                        int i = 1;
                        for (String categoria : categorias) {
                            System.out.println(i + "." + categoria);
                            i++;
                        }
                        System.out.print("Ingrese el número de la categoria: ");
                        opcion = scanner.nextInt();

                        // 3. Filtrar y mostrar los productos
                        if (opcion >= 1 && opcion <= categorias.size()) {
                            String categoriaSeleccionada = categorias.toArray(new String[0])[opcion - 1];
                            List<String> productosEncontrados = buscarPorCategoria(categoriaSeleccionada);
                            if(productosEncontrados.size()>0){
                                System.out.println("Productos en la categoría: " + categoriaSeleccionada );
                                for (String producto : productosEncontrados) {
                                    System.out.println(producto);
                                }
                            }else
                                System.out.println("No existen productos en la categoria "+categoriaSeleccionada+".");
                            System.out.println("*********************************************");

                        } else if (opcion != 0) {
                            System.out.println("Opción inválida.");
                        }
                }
                case 2 -> {
                    Scanner scn = new Scanner(System.in);
                    System.out.print("Ingrese el nombre del producto ");
                    String palabra = scn.nextLine();
                    List<String> productosEncontrados = buscarPorCategoria(palabra);

                    if(productosEncontrados.size()>0){
                        System.out.println("Productos con el nombre: " + palabra);
                        for (String producto : productosEncontrados) {
                            System.out.println(producto);
                        }
                    }else
                        System.out.println("No existen productos con el nombre "+palabra+".");
                    System.out.println("*********************************************");

                }
                case 3 -> {
                    int idProducto;
                    System.out.print("Ingrese el id del producto ");
                    idProducto = scanner.nextInt();
                    int i = 1;
                    boolean encontrado = false;
                    for (String id : ids) {
                        String idEntras= String.valueOf(idProducto);
                        if(idEntras.equals(id))
                        {
                            List<String> productosEncontrados = buscarPorCategoria(String.valueOf(idProducto));
                            if(productosEncontrados.size()>0){
                                System.out.println("Productos con el id: " + idProducto );
                                for (String producto : productosEncontrados) {
                                    System.out.println(producto);
                                    encontrado= true;
                                }
                            }else
                                System.out.println("No existen productos con el id "+idProducto+".");
                        }
                        i++;
                    }
                    if (encontrado==false)
                        System.out.println("No existen productos con el id "+idProducto+".");
                    System.out.println("*********************************************");
                }
                default -> System.out.println("Opción inválida.");
            }
    }
    // Calcular precio Producto

    public Producto productoMasCaro() {
        return productos.stream().max(Comparator.comparingInt(Producto::getPrecio)).orElse(null);
    }

    // Calcular cantidad de Productos

    public Map<String, Integer> cantidadPorCategoria() {
        Map<String, Integer> conteo = new HashMap<>();
        for (Producto p : productos) {
            conteo.put(p.getCategoria(), conteo.getOrDefault(p.getCategoria(), 0) + p.getCantidadDisponible());
        }
        return conteo;
    }

    // Reporte de Inventario

    public void generarReporte() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_inventario.txt"))) {
            // Encabezado del reporte
            bw.write("id\tNombre\tCategoría\tPrecio\t\tCantidad\tTotal");
            bw.newLine();

            int valorTotal = 0;
            for (Producto p : productos) {
                int totalProducto = p.getPrecio() * p.getCantidadDisponible();
                valorTotal += totalProducto;
                //formato para organizar el reporte

                bw.write(String.format("%-1s \t%-1s \t%-1s \t%1s \t\t%1s \t%1s",
                        p.getId(),
                        p.getNombre(),
                        p.getCategoria(),
                        String.format("%8d", p.getPrecio()),
                        p.getCantidadDisponible(),
                        totalProducto));
                bw.newLine();
            }

            // Pie de página
            bw.newLine();
            bw.write("Valor total del inventario: " + "$ " + valorTotal);

            System.out.println("Se genero el reporte satisfactoriamente.");
            System.out.println("*********************************************");
        } catch (IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

// Metodo que guarda los cambios

    private void guardarCambios() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoInventario))) {
            for (Producto p : productos) {
                bw.write(p.toString());
                bw.newLine();
            }
            System.out.println("*********************************************");
        } catch (IOException e) {
            System.out.println("Error al guardar cambios: " + e.getMessage());
        }
    }
}
