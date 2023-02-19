package logica;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Pedido {
	private static int numeroPedidos = 0;
	private int idPedido;
	private String nombreCliente;
	private String direccionCliente;
	private ArrayList<Producto> itemsPedido;
	private ArrayList<ProductoMenu> itemsMenu;
	private ArrayList<Combo> itemsCombo;
	private ArrayList<ProductoAjustado> itemsAjustado;
	
	public Pedido(String nombreCliente, String direccionCliente) {
		numeroPedidos ++;
		this.idPedido = numeroPedidos;
		this.nombreCliente = nombreCliente;
		this.direccionCliente = direccionCliente;
		this.itemsPedido = new ArrayList<Producto>();
		this.itemsMenu = new ArrayList<ProductoMenu>();
		this.itemsCombo = new ArrayList<Combo>();
		this.itemsAjustado = new ArrayList<ProductoAjustado>();
	}
	
	public int getIdPedido() {
		return idPedido;
	}
	
	public String getNombreCliente() {
		return nombreCliente;
	}
	
	public String getDireccionCliente() {
		return direccionCliente;
	}
	
	public ArrayList<ProductoMenu> getItemsMenu() {
		return itemsMenu;
	}
	
	public ArrayList<Combo> getItemsCombo() {
		return itemsCombo;
	}
	
	public ArrayList<ProductoAjustado> getItemsAjustado() {
		return itemsAjustado;
	}
	
	public void agregarProducto(Producto nuevoItem) {
		itemsPedido.add(nuevoItem);
		if (nuevoItem instanceof ProductoMenu) {
			itemsMenu.add((ProductoMenu) nuevoItem);
		} else if (nuevoItem instanceof Combo) {
			itemsCombo.add((Combo) nuevoItem);
		} else if (nuevoItem instanceof ProductoAjustado) {
			itemsAjustado.add((ProductoAjustado) nuevoItem);
		} else {
			System.out.println("Problema de identificación de clases");
		}
	}
	
	private double getPrecioNetoPedido() {
		double neto = 0;
		for (Producto producto: itemsPedido) {
			 neto += producto.getPrecio();
		 }
		return neto;
	}
	
	private double getPrecioIVAPedido(double neto) {
		return neto*0.19;
	}
	
	private double getPrecioTotalPedido(double neto) {
		return neto*1.19;
	}
	
	private String generarTextoFactura() {
		String factura = "";
		factura += "Nombre del cliente: " + nombreCliente + "\n";
		factura += "Dirección del cliente: " + direccionCliente + "\n";
		factura += "\nProductos escogidos:\n";
		for (Producto producto: itemsPedido) {
			 factura += producto.generarTextoFactura();
		 }
		double neto = getPrecioNetoPedido();
		factura += "\nValor Neto: $" + neto + "\n";
		factura += "IVA (19%): $" + getPrecioIVAPedido(neto) + "\n";
		factura += "Valor Total: $" + getPrecioTotalPedido(neto) + "\n";
		return factura;
	}
	
	public void guardarFactura(File archivo) throws IOException {
		FileWriter fw = new FileWriter(archivo);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(generarTextoFactura());
        bw.close();
	}
}
