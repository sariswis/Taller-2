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
	
	public Pedido(String nombreCliente, String direccionCliente) {
		numeroPedidos ++;
		this.idPedido = numeroPedidos;
		this.nombreCliente = nombreCliente;
		this.direccionCliente = direccionCliente;
		this.itemsPedido = new ArrayList<Producto>();
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
	
	public void agregarProducto(Producto nuevoItem) {
		itemsPedido.add(nuevoItem);
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
