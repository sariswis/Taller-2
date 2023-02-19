package logica;

import java.util.ArrayList;

public class ProductoAjustado implements Producto {
	private ProductoMenu base;
	private ArrayList<Ingrediente> agregados;
	private ArrayList<Ingrediente> eliminados;
	
	public ProductoAjustado(ProductoMenu base) {
		this.base = base;
		this.agregados = new ArrayList<Ingrediente>();
		this.eliminados = new ArrayList<Ingrediente>();
	}
	
	public void agregarIngrediente(Ingrediente ingrediente) {
		int aumento = ingrediente.getCostoAdicional();
		base.aumentarPrecio(aumento);
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
		String agreg = cadena.trim() + ")";
		String elim = cadena2.trim() + ")";
		return agreg + " " + elim;
	}

	@Override
	public int getPrecio() {
		return base.getPrecio();
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
