package logica;

public class ProductoMenu implements Producto {
	private String nombre;
	private int precioBase;
	
	public ProductoMenu(String nombre, int precioBase){
		this.nombre = nombre;
		this.precioBase = precioBase;
	}
	
	public void aumentarPrecio(int aumento) {
		precioBase += aumento;
	}
	
	@Override
	public int getPrecio() {
		return precioBase;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

	@Override
	public String generarTextoFactura() {
		return "- " + nombre + " - $" + precioBase + "\n";
	}
}
