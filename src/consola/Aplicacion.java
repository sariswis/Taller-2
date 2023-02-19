package consola;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import logica.Combo;
import logica.Ingrediente;
import logica.Pedido;
import logica.Producto;
import logica.ProductoMenu;
import logica.ProductoAjustado;
import logica.Restaurante;

public class Aplicacion {
	private static Restaurante restaurante;
	private static boolean continuar = true;
	private static String stringProductos = "\nProductos:\n";
	private static String stringIngredientes = "\nIngredientes adicionales:\n";
	private static String stringCombos = "\nCombos:\n";
	private static String stringBebidas = "\nBebidas:\n";
	
	public static void main(String[] args) {
		cargarArchivos();
		while (continuar) {
			mostrarOpciones();
			int opcionSeleccionada = index("Seleccione una opción: ");
			ejecutarOpcionSeleccionada(opcionSeleccionada);
		}
	}

	public static void mostrarOpciones() {
		System.out.println("****************************************************************");
		System.out.println("¡Bienvenido!");
		System.out.println("1. Ver todo el menú");
		System.out.println("2. Iniciar un nuevo pedido");
		System.out.println("3. Agregar un elemento a un pedido");
		System.out.println("4. Cerrar un pedido y guardar la factura");
		System.out.println("5. Consultar la información de un pedido dado su id");
		System.out.println("6. Salir");
		System.out.println("****************************************************************");
	}
	
	 public static void cargarArchivos() {
		 try {
				FileReader archivoIngredientes = new FileReader("data/ingredientes.txt");
				FileReader archivoMenu = new FileReader("data/menu.txt");
				FileReader archivoBebidas = new FileReader("data/bebidas.txt");		
				FileReader archivoCombos = new FileReader("data/combos.txt");
				restaurante = Restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoBebidas, archivoCombos);
				generarMenu();
			} catch (FileNotFoundException e) {
				System.out.println("\nArchivo no encontrado\n");
			} catch (IOException e) {
				System.out.println("\nExcepción encontrada\n");
			}
	 }
	 
	 public static void generarMenu() {
		 ArrayList<ProductoMenu> menuBase = restaurante.getMenuBase();
		 ArrayList<Ingrediente> ingredientes = restaurante.getIngredientes();
		 ArrayList<ProductoMenu> bebidas = restaurante.getBebidas();
		 ArrayList<Combo> combos = restaurante.getCombos();
		 int i = 0;
		 for (Producto producto: menuBase) {
			 stringProductos += i + ". " + producto.getNombre() + " - $" + producto.getPrecio() + "\n";
			 i++;
		 }
		 i = 0;
		 for (Ingrediente ingrediente: ingredientes) {
			 stringIngredientes += i + ". " + ingrediente.getNombre() + " - $" + ingrediente.getCostoAdicional() + "\n";
			 i++;
		 }
		 i = 0;
		 for (Combo combo: combos) {
			 stringCombos += i + ". " + combo.getNombre() + " " + combo.getItems() + " - $" + combo.getPrecio() + "\n";
			 i++;
		 }
		 i = 0;
		 for (Producto bebida: bebidas) {
			 stringBebidas += i + ". " + bebida.getNombre() + " - $" + bebida.getPrecio() + "\n";
			 i++;
		 }
	 }
	 
	 public static void ejecutarOpcionSeleccionada(int opcionSeleccionada) {
			if (opcionSeleccionada == 1) {
				mostrarMenu();
			} else if (opcionSeleccionada == 2) {
				iniciarPedido();
			} else if (opcionSeleccionada == 3) {
				if (restaurante.getPedidoEnCurso() == null) {
					System.out.println("\nInicie un pedido primero\n");
				} else {
					agregarElemento();
				}
			} else if (opcionSeleccionada == 4) {
				if (restaurante.getPedidoEnCurso() == null) {
					System.out.println("\nInicie un pedido primero\n");
				} else {
					try {
						cerrarPedido();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if (opcionSeleccionada == 5) {
				try {
					consultarPedido();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (opcionSeleccionada == 6) {
				continuar = false;
			} else {
				System.out.println("\nIngrese una opción válida\n");
			}
	 }
	 
	public static void mostrarMenu() {
		System.out.print(stringProductos);
		System.out.print(stringIngredientes);
		System.out.print(stringCombos);
		System.out.print(stringBebidas);
		System.out.println("");
	}
			
	 public static void iniciarPedido() {
		String nombreCliente = input("Nombre: ");
		String direccionCliente = input("Dirección: ");
		restaurante.iniciarPedido(nombreCliente, direccionCliente);
		System.out.println("\n¡Ya puede empezar a ordenar!\n");
	 }
	 
	 public static void agregarElemento() {
		 int i_producto_combo = index("\nEscriba si desea un: \n1. Producto del menú \n2. Combo\n3. Bebida\n");
		 if (i_producto_combo == 1) {
			 pedirProducto();
		 } else if (i_producto_combo == 2) {
			 pedirCombo();
		 } else if (i_producto_combo == 3) {
			 pedirBebida();
		 } else {
			 System.out.println("\nIngrese un número válido\n");
		 }
	 }
	 
	 public static void pedirProducto() {
		 System.out.print(stringProductos);
		 int i_producto = index("\nEscriba el índice de su producto: ");
		 if (0 <= i_producto && i_producto < restaurante.getTamaño("Productos")) {
			 ProductoMenu producto = restaurante.getProducto(i_producto);
			 int i_cambio = index("\n¿Desea cambiar los ingredientes?\n1. Sí\n2. No\n");
			 if (i_cambio == 1) {
				 pedirCambio(producto);
			 } else if (i_cambio == 2) {
				 restaurante.agregarProducto(producto);
				 System.out.println("\n¡Producto agregado!\n");
			 } else {
				 System.out.println("\nIngrese un número válido\n");
			 }
		 } else {
			 System.out.println("\nIngrese un número válido\n");
		 }
	 }
	 
	 public static void pedirCambio(ProductoMenu producto) {
		 boolean seguir = true;
		 ProductoAjustado ajustado = restaurante.ajustarProducto(producto);
		 System.out.print(stringIngredientes);
		 while (seguir) {
			 int i_ingred = index("\nEscriba el índice del ingrediente a cambiar: ");
			 if (0 <= i_ingred && i_ingred < restaurante.getTamaño("Ingredientes")) {
				 Ingrediente ingrediente = restaurante.getIngrediente(i_ingred);
				 int i_tipo = index("\nDesea\n1. Agregarlo\n2. Eliminarlo\n");
				 if (i_tipo == 1) {
					 ajustado.agregarIngrediente(ingrediente);
				 } else if (i_tipo == 2) {
					 ajustado.eliminarIngrediente(ingrediente);
				 } else {
					 System.out.println("\nIngrese un número válido\n");
				 }
			 } else {
				 System.out.println("\nIngrese un número válido\n");
			 }
			 
			 int i_seguir = index("\n¿Desea seguir cambiando el producto?\n1. Sí\n2. No\n");
			 if (i_seguir != 1) {
				 restaurante.agregarProducto(ajustado);
				 System.out.println("\n¡Producto modificado agregado!\n");
				 seguir = false;
			 }
		 }
	 }
	 
	 public static void pedirCombo() {
		 System.out.print(stringCombos);
		 int i_combo = index("\nEscriba el índice de su combo: ");
		 if (0 <= i_combo && i_combo < restaurante.getTamaño("Combos")) {
			 Combo combo = restaurante.getCombo(i_combo);
			restaurante.agregarProducto(combo);
			System.out.println("\n¡Combo agregado!\n");
		 } else {
			 System.out.println("\nIngrese un número válido\n");
		 }
	 }
	 
	 public static void pedirBebida() {
		 System.out.print(stringBebidas);
		 int i_bebida = index("\nEscriba el índice de su bebida: ");
		 if (0 <= i_bebida && i_bebida < restaurante.getTamaño("Bebidas")) {
			 ProductoMenu bebida = restaurante.getBebida(i_bebida);
			 int i_cambio = index("\n¿Desea cambiar los ingredientes?\n1. Sí\n2. No\n");
			 if (i_cambio == 1) {
				 pedirCambio(bebida);
			 } else if (i_cambio == 2) {
				 restaurante.agregarProducto(bebida);
				 System.out.println("\n¡Bebida agregada!\n");
			 } else {
				 System.out.println("\nIngrese un número válido\n");
			 }
		 } else {
			 System.out.println("\nIngrese un número válido\n");
		 }
	 }
	 
	 public static void cerrarPedido() throws IOException {
		 restaurante.cerrarYGuardarPedido();
		 System.out.println("\n¡Su pedido ha sido cerrado y guardado!\n");
	 }
	 
	 public static void consultarPedido() throws IOException {
		 int id = index("\nEscriba el id del pedido: ");
		 Pedido pedido = restaurante.getPedido(id);
		 if (pedido != null) {
			System.out.println("\nInformación del pedido:\n");
			FileReader factura = new FileReader("facturas/" + id + ".txt");
			BufferedReader br = new BufferedReader(factura);
			String linea = br.readLine();	
			while (linea != null) {
				System.out.println(linea);
				linea = br.readLine();
			}
			System.out.println("");
			br.close();
			String esIdentico = restaurante.pedidoIdentico(pedido);
			System.out.println(esIdentico);
		 } else {
			 System.out.println("\nNo existe dicho pedido\n");
		 }
	 }
	 
	public static String input(String mensaje){
		try {
			System.out.print(mensaje);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		} catch (IOException e) {
			System.out.println("\nError leyendo de la consola\n");
			return null;
		} 
	 }
	 
	public static int index(String mensaje) {
		try {
			int index = Integer.parseInt(input(mensaje));
			return index;
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}
}
