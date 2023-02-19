package logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Restaurante {
	private static HashMap<String, ProductoMenu> buscador = new HashMap<String, ProductoMenu>();
	private static HashMap<Integer, Pedido> pedidos = new HashMap<Integer, Pedido>();
	private ArrayList<Ingrediente> ingredientes;
	private ArrayList<ProductoMenu> menuBase;
	private ArrayList<ProductoMenu> bebidas;
	private ArrayList<Combo> combos;
	private Pedido pedidoEnCurso;
	
	public Restaurante(ArrayList<Ingrediente> ingredientes, ArrayList<ProductoMenu> menuBase, ArrayList<ProductoMenu> bebidas, ArrayList<Combo> combos) {
		this.ingredientes = ingredientes;
		this.menuBase = menuBase;
		this.bebidas = bebidas;
		this.combos = combos;
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
	
	public ArrayList<ProductoMenu> getBebidas() {
		return bebidas;
	}
	
	public ProductoMenu getBebida(int i_bebida) {
		return bebidas.get(i_bebida);
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
		} else if (tipo == "Bebidas") {
			return bebidas.size();
		} else if (tipo == "Combos") {
			return combos.size();
		} else {
			return 0;
		}
	}
	
	 public String pedidoIdentico(Pedido original) {
		 ArrayList<ProductoMenu> originalMenu = original.getItemsMenu();
		 ArrayList<Combo> originalCombo = original.getItemsCombo();
		 ArrayList<ProductoAjustado> originalAjustado = original.getItemsAjustado();
		 String esIdentico = "\nEl pedido no es idéntico a ningún otro\n";
		 int idOriginal = original.getIdPedido();
		 int idPedido;
		 
		 for (Pedido pedido: pedidos.values()) {
			 idPedido = pedido.getIdPedido();
			 if (idOriginal != idPedido) {
				 ArrayList<ProductoMenu> pedidoMenu = pedido.getItemsMenu();
				 ArrayList<Combo> pedidoCombo = pedido.getItemsCombo();
				 ArrayList<ProductoAjustado> pedidoAjustado = pedido.getItemsAjustado();
				 
				 boolean p1 = compararMenu(originalMenu, pedidoMenu);
				 if (p1) {
					 boolean p2 = compararCombo(originalCombo, pedidoCombo);
					 if (p2) {
						 boolean p3 = compararAjustado(originalAjustado, pedidoAjustado);
						 if (p3) {
							 esIdentico = "\nEl pedido " + idOriginal + " es idéntico al pedido " + idPedido + "\n";
							 break;
						 }
					 }
				 }
			 }
		 }
		 return esIdentico;
	 }
	 
	 public static boolean compararMenu(ArrayList<ProductoMenu> originalMenu,  ArrayList<ProductoMenu> pedidoMenu) {
		 boolean p = true;
		 for (ProductoMenu original: originalMenu) {
			 boolean q = true;
			 for (ProductoMenu pedido: pedidoMenu) {
				boolean igual = original.equals(pedido);
				if (igual) {
					q = true;
					break;
				}
				q = false;
			 }
			 if (q == false) {
				 p = false;
				 break;
			 }
		 }
		 return p;
	 }
	 
	 public static boolean compararCombo(ArrayList<Combo> originalCombo, ArrayList<Combo> pedidoCombo) {
		 boolean p = true;
		 for (Combo original: originalCombo) {
			 boolean q = true;
			 for (Combo pedido: pedidoCombo) {
				boolean igual = original.equals(pedido);
				if (igual) {
					q = true;
					break;
				}
				q = false;
			 }
			 if (q == false) {
				 p = false;
				 break;
			 }
		 }
		 return p;
	 }
	 
	 public static boolean compararAjustado(ArrayList<ProductoAjustado> originalAjustado, ArrayList<ProductoAjustado> pedidoAjustado) {
		 boolean p = true;
		 for (ProductoAjustado original: originalAjustado) {
			 boolean q = true;
			 
			 String on = original.getNombre();
			 int op = original.getPrecio();
			 ArrayList<Ingrediente> oa = original.getAgregados();
			 ArrayList<Ingrediente> oe = original.getEliminados();
			 
			 for (ProductoAjustado pedido: pedidoAjustado) {		 
				 String pn = pedido.getNombre();
				 int pp = pedido.getPrecio();
				 ArrayList<Ingrediente> pa = pedido.getAgregados();
				 ArrayList<Ingrediente> pe = pedido.getEliminados();
				 
				 boolean igual = original.equals(pedido);
				boolean igual1 = on.equals(pn);
				boolean igual2 = op == pp;
				boolean igual3 = oa.equals(pa);
				boolean igual4 = oe.equals(pe);
				if (igual || (igual1 && igual2 && igual3 && igual4)) {
					q = true;
					break;
				}
				q = false;
			 }
			 if (q == false) {
				 p = false;
				 break;
			 }
		 }
		 return p;
	 }
	
	public static Restaurante cargarInformacionRestaurante(FileReader archivoIngredientes, FileReader archivoMenu, FileReader archivoBebidas, FileReader archivoCombos) throws IOException {
		Restaurante restaurante = new Restaurante(cargarIngredientes(archivoIngredientes), cargarMenu(archivoMenu), cargarBebidas(archivoBebidas), cargarCombos(archivoCombos));
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
	
	private static ArrayList<ProductoMenu> cargarBebidas(FileReader archivoBebidas) throws IOException {
		ArrayList<ProductoMenu> lasBebidas = new ArrayList<ProductoMenu>();
		BufferedReader br = new BufferedReader(archivoBebidas);
		String linea = br.readLine();	
		while (linea != null) {
				String[] partes = linea.split(";");
				String nombre = partes[0];
				int precioBase = Integer.parseInt(partes[1]);
				ProductoMenu producto = new ProductoMenu(nombre, precioBase);
				lasBebidas.add(producto);
				buscador.put(nombre, producto);
				linea = br.readLine();
		}
		br.close();
		return lasBebidas;
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
