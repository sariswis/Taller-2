package logica;

import java.util.ArrayList;

public class ProductoAjustado implements Producto {
	private ProductoMenu base;
	private ArrayList<Ingrediente> agregados;
	private ArrayList<Ingrediente> eliminados;
	private int precio;
	
	public ProductoAjustado(ProductoMenu base) {
		this.base = base;
		this.agregados = new ArrayList<Ingrediente>();
		this.eliminados = new ArrayList<Ingrediente>();
		this.precio = base.getPrecio();
	}
	
	public void agregarIngrediente(Ingrediente ingrediente) {
		int aumento = ingrediente.getCostoAdicional();
		precio += aumento;
		agregados.add(ingrediente);
	}
	
	public void eliminarIngrediente(Ingrediente ingrediente) {
		eliminados.add(ingrediente);
	}
	
	public String getAgregadosEliminados() {
		String cadena = "+(";
		String cadena2 = "-(";
		for (Ingrediente ingrediente: agregados) {
			cadena += ingrediente.getNombre() + ", ";
		}
		for (Ingrediente ingrediente: eliminados) {
			cadena2 += ingrediente.getNombre() + ", ";
		}
		String agreg = cadena.substring(0, cadena.length() - 2) + ")";
		String elim = cadena2.substring(0, cadena2.length() - 2) + ")";
		return agreg + " " + elim;
	}
	
	public ArrayList<Ingrediente> getAgregados(){
		return agregados;
	}
	
	public ArrayList<Ingrediente> getEliminados(){
		return eliminados;
	}
	
	@Override
	public int getPrecio() {
		return precio;
	}

	@Override
	public String getNombre() {
		return base.getNombre();
	}

	@Override
	public String generarTextoFactura() {
		return "- " + getNombre() + " " + getAgregadosEliminados() + " - $" + getPrecio() + "\n";
	}
}
