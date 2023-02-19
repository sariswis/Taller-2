package logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Restaurante {
	private static HashMap<String, ProductoMenu> buscador;
	private ArrayList<Ingrediente> ingredientes;
	private ArrayList<ProductoMenu> menuBase;
	private ArrayList<Combo> combos;
	private HashMap<Integer, Pedido> pedidos;
	private Pedido pedidoEnCurso;
	
	public Restaurante(ArrayList<Ingrediente> ingredientes, ArrayList<ProductoMenu> menuBase, ArrayList<Combo> combos) {
		this.ingredientes = ingredientes;
		this.menuBase = menuBase;
		this.combos = combos;
		this.pedidos = new HashMap<Integer, Pedido>();
	}
	
	public void iniciarPedido(String nombreCliente, String direccionCliente) {
		pedidoEnCurso = new Pedido(nombreCliente, direccionCliente);
	}
	
	public void agregarProducto(Producto producto) {
		pedidoEnCurso.agregarProducto(producto);
	}
	
	public ProductoAjustado ajustarProducto(ProductoMenu producto) {
		return new ProductoAjustado(producto);
	}
	
	public void cerrarYGuardarPedido() throws IOException {
		int id = pedidoEnCurso.getIdPedido();
		File archivo = new File("facturas/" + id + ".txt");
		archivo.createNewFile();
		pedidoEnCurso.guardarFactura(archivo);
		pedidos.put(id, pedidoEnCurso);
		pedidoEnCurso = null;
	}
	
	public Pedido getPedidoEnCurso() {
		return pedidoEnCurso;
	}
	
	public Pedido getPedido(int id) {
		return pedidos.get(id);
	}
	
	public ArrayList<Ingrediente> getIngredientes() {
		return ingredientes;
	}
	
	public Ingrediente getIngrediente(int i_ingred) {
		return ingredientes.get(i_ingred);
	}
	
	public ArrayList<ProductoMenu> getMenuBase() {
		return menuBase;
	}
	
	public ProductoMenu getProducto(int i_producto) {
		return menuBase.get(i_producto);
	}
	
	public ArrayList<Combo> getCombos() {
		return combos;
	}
	
	public Combo getCombo(int i_combo) {
		return combos.get(i_combo);
	}

	public int getTamaño(String tipo) {
		if (tipo == "Productos") {
			return menuBase.size();
		} else if (tipo == "Ingredientes") {
			return ingredientes.size();
		} else if (tipo == "Combos") {
			return combos.size();
		} else {
			return 0;
		}
	}
	
	public static Restaurante cargarInformacionRestaurante(FileReader archivoIngredientes, FileReader archivoMenu, FileReader archivoCombos) throws IOException {
		Restaurante restaurante = new Restaurante(cargarIngredientes(archivoIngredientes), cargarMenu(archivoMenu), cargarCombos(archivoCombos));
		return restaurante;
	}
	
	private static ArrayList<Ingrediente> cargarIngredientes(FileReader archivoIngredientes) throws IOException {
		ArrayList<Ingrediente> losIngredientes = new ArrayList<Ingrediente>();
		BufferedReader br = new BufferedReader(archivoIngredientes);
		String linea = br.readLine();	
		while (linea != null) {
				String[] partes = linea.split(";");
				String nombre = partes[0];
				int costoAdicional = Integer.parseInt(partes[1]);
				Ingrediente ingrediente = new Ingrediente(nombre, costoAdicional);
				losIngredientes.add(ingrediente);
				linea = br.readLine();
		}
		br.close();
		return losIngredientes;
	}
	
	private static ArrayList<ProductoMenu> cargarMenu(FileReader archivoMenu) throws IOException {
		ArrayList<ProductoMenu> elMenu = new ArrayList<ProductoMenu>();
		buscador = new HashMap<String, ProductoMenu>();
		BufferedReader br = new BufferedReader(archivoMenu);
		String linea = br.readLine();	
		while (linea != null) {
				String[] partes = linea.split(";");
				String nombre = partes[0];
				int precioBase = Integer.parseInt(partes[1]);
				ProductoMenu producto = new ProductoMenu(nombre, precioBase);
				elMenu.add(producto);
				buscador.put(nombre, producto);
				linea = br.readLine();
		}
		br.close();
		return elMenu;
	}
	
	private static ArrayList<Combo> cargarCombos(FileReader archivoCombos) throws IOException {
		ArrayList<Combo> losCombos = new ArrayList<Combo>();
		BufferedReader br = new BufferedReader(archivoCombos);
		String linea = br.readLine();	
		while (linea != null) {
				String[] partes = linea.split(";");
				String nombre = partes[0];
				double descuento = Double.parseDouble(partes[1].replaceAll("%", ""))/100;
				Combo combo = new Combo(nombre, descuento);
				for (int i = 2;  i < partes.length; i++) {
					ProductoMenu producto = buscador.get(partes[i]);
					combo.agregarItemACombo(producto);
				}
				combo.calcularPrecio();
				losCombos.add(combo);
				linea = br.readLine();
		}
		br.close();
		return losCombos;
	}
}
