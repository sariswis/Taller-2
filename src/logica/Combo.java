package logica;

import java.util.ArrayList;

public class Combo implements Producto {
	private double descuento;
	private String nombreCombo;
	private ArrayList<ProductoMenu> itemsCombo;
	private int precio;
	
	public Combo(String nombreCombo, double descuento) {
		this.descuento = descuento;
		this.nombreCombo = nombreCombo;
		this.itemsCombo = new ArrayList<ProductoMenu>();
	}
	
	public void agregarItemACombo(ProductoMenu itemCombo) {
		itemsCombo.add(itemCombo);
	}

	public void calcularPrecio() {
		int total = 0;
		for (ProductoMenu producto: itemsCombo) {
			total += producto.getPrecio();
		}
		precio = (int) (total*(1 - descuento));
	}
	
	public String getItems(){
		String cadena = "(";
		for (ProductoMenu producto: itemsCombo) {
			cadena += producto.getNombre() + ", ";
		}
		String items = cadena.substring(0, cadena.length() - 2) + ")";
		return items;
	}
	
	@Override
	public int getPrecio() {
		return precio;
	}

	@Override
	public String getNombre() {
		return nombreCombo;
	}

	@Override
	public String generarTextoFactura() {
		return "- " + nombreCombo + " " + getItems() + " - $" + precio + "\n";
	}
}
