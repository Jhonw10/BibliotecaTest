package dominio;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

import java.util.Calendar;
import java.util.Date;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String EL_LIBRO_NO_EXISTE = "El libro solicitado no existe";
	public static final String CODIGO_PALINDROMO = "Los libros palíndromos solo se pueden utilizar en la biblioteca";

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, String nombre) {
		Prestamo prestamo;
		Libro libro = consultarLibro(isbn);
		if (esPalindromo(isbn)){
			throw new PrestamoException(CODIGO_PALINDROMO);
		}
		if (esPrestado(isbn)){
			throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
		}
		String result = eliminarCaracteres(isbn);
		Integer sum = sumarDigitos(result);
		Calendar fechaActual = getFechaSolicitud();
		Date fechaEntregaMaxima = calcularFechaMaximaDeEntrega(sum, fechaActual);
		prestamo = new Prestamo(fechaActual.getTime(), libro, fechaEntregaMaxima, nombre);
		this.repositorioPrestamo.agregar(prestamo);
	}

	public String eliminarCaracteres(String isbn){
		return isbn.replaceAll("\\D+","");
	}

	public Date calcularFechaMaximaDeEntrega(int sumatoria, Calendar fechaActual){
		Date fechaEntregaMaxima = null;
		if(sumatoria > 30){
			fechaEntregaMaxima = calcularFechaMaxima(fechaActual);
		}
		return fechaEntregaMaxima;
	}

	public Calendar getFechaSolicitud(){
		return Calendar.getInstance();
	}

	public boolean esPrestado(String isbn) {
		Libro libro = this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		return libro != null;
	}

	public Libro consultarLibro(String isbn) {
		Libro libro = this.repositorioLibro.obtenerPorIsbn(isbn);
		if (libro == null){
			throw new PrestamoException(EL_LIBRO_NO_EXISTE);
		}
		return libro;
	}

	public boolean esPalindromo(String str) {
		return str.equals(new StringBuilder(str).reverse().toString());
	}

	public int sumarDigitos(String str){
		return str.chars().mapToObj(i -> (char) i).filter(Character::isDigit)
				.mapToInt(Character::getNumericValue).sum();
	}

	public Date calcularFechaMaxima(Calendar fechaInicial){
		fechaInicial.add(Calendar.DAY_OF_WEEK, 16);
		if(fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			fechaInicial.add(Calendar.DAY_OF_WEEK, 1);
		}
		return fechaInicial.getTime();
	}
}
