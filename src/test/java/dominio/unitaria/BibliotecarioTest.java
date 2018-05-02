package dominio.unitaria;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import dominio.excepcion.PrestamoException;
import org.junit.Assert;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import testdatabuilder.LibroTestDataBuilder;

import java.util.Calendar;
import java.util.Date;

public class BibliotecarioTest {

	@Test
	public void esPrestadoTest() {

		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();

		Libro libro = libroTestDataBuilder.build();

		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(libro);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act 
		boolean esPrestado = bibliotecario.esPrestado(libro.getIsbn());

		//assert
		assertTrue(esPrestado);
	}

	@Test
	public void libroNoPrestadoTest() {

		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();

		Libro libro = libroTestDataBuilder.build();

		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act 
		boolean esPrestado = bibliotecario.esPrestado(libro.getIsbn());

		//assert
		assertFalse(esPrestado);
	}

	@Test
	public void fechaMaximaDePrestamoDebeSerElNueveDeJunioCuandoFechaDeSolicitudEs24DeMayoTest() {
		// arrange
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		Calendar fechaInicial = Calendar.getInstance();
		fechaInicial.set(Calendar.MONTH, 4);
		fechaInicial.set(Calendar.DAY_OF_MONTH, 24);
		fechaInicial.set(Calendar.YEAR, 2017);

		// act
		Date fechaMaxima = bibliotecario.calcularFechaMaxima(fechaInicial);
		Calendar result = Calendar.getInstance();
		result.setTime(fechaMaxima);

		//assert
		assertTrue(result.get(Calendar.YEAR) == 2017);
		assertTrue(result.get(Calendar.MONTH) == 5);
		assertTrue(result.get(Calendar.DAY_OF_MONTH) == 9);
	}

	@Test
	public void fechaMaximaDePrestamoDebeSerElDoceDeJunioCuandoFechaDeSolicitudEs26DeMayoTest() {
		// arrange
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		Calendar fechaInicial = Calendar.getInstance();
		fechaInicial.set(Calendar.MONTH, 4);
		fechaInicial.set(Calendar.DAY_OF_MONTH, 26);
		fechaInicial.set(Calendar.YEAR, 2017);

		// act
		Date fechaMaxima = bibliotecario.calcularFechaMaxima(fechaInicial);
		Calendar result = Calendar.getInstance();
		result.setTime(fechaMaxima);

		//assert
		assertTrue(result.get(Calendar.YEAR) == 2017);
		assertTrue(result.get(Calendar.MONTH) == 5);
		assertTrue(result.get(Calendar.DAY_OF_MONTH) == 12);
	}

	@Test
	public void sumarDigitosTest() {
		//arrange
		String sbn = "TPRUEBA1234";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		int result = bibliotecario.sumarDigitos(sbn);

		//assert
		assertTrue(result == 10);
	}

	@Test
	public void sumarDigitosDebeRetornarCeroCuandoNoExistanNumerosTest() {
		//arrange
		String sbn = "TPRUEBA";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		int result = bibliotecario.sumarDigitos(sbn);

		//assert
		assertTrue(result == 0);
	}

	@Test
	public void esPalindromoDebeRetornarTrue() {
		//arrange
		String sbn = "PRUURP";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		boolean result = bibliotecario.esPalindromo(sbn);

		//assert
		assertTrue(result);
	}

	@Test
	public void esPalindromoDebeRetornarFalse() {
		//arrange
		String sbn = "PRUURPA123";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		boolean result = bibliotecario.esPalindromo(sbn);

		//assert
		assertFalse(result);
	}

	@Test
	public void debeRetornarStringConSoloDigitosCuandoExista() {
		//arrange
		String sbn = "PRUURPA123";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		String result = bibliotecario.eliminarCaracteres(sbn);

		//assert
		assertEquals(result, "123");
	}

	@Test
	public void debeRetornarStringVacioCuandoNoExistanNumeros() {
		//arrange
		String sbn = "PRUURPA";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		String result = bibliotecario.eliminarCaracteres(sbn);

		//assert
		assertEquals(result, "");
	}

	@Test
	public void prestarLibroDebeRetornarExcepcionCuandoNoExista() {

		// arrange
		String isbn = "1234Pru";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		when(repositorioLibro.obtenerPorIsbn(isbn)).thenReturn(null);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		try {
			blibliotecario.consultarLibro(isbn);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_EXISTE, e.getMessage());
			verify(repositorioLibro, times(1)).obtenerPorIsbn(isbn);
		}
	}

	@Test
	public void prestarLibroNoDebeGenerarErrorCuandoLibroExiste() {
		// arrange
		String isbn = "1234Pru";
		String titulo = "Titulo";
		Libro libro = new Libro(isbn, titulo, 2016);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		when(repositorioLibro.obtenerPorIsbn(isbn)).thenReturn(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		//act
		Libro result = blibliotecario.consultarLibro(isbn);

		//assert
		assertEquals(result.getIsbn(), isbn);
		assertEquals(result.getTitulo(), titulo);
		assertEquals(result.getAnio(), 2016);
		verify(repositorioLibro, times(1)).obtenerPorIsbn(isbn);
	}

	@Test
	public void prestarDebeRetornarExcepcionCuandoEsPalindromo(){
		// arrange
		String isbn = "1234P4321";
		String nombre = "Jhon Olarte";
		String titulo = "Titulo";
		Libro libro = new Libro(isbn, titulo, 2016);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		when(repositorioLibro.obtenerPorIsbn(isbn)).thenReturn(libro);


		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		blibliotecario = Mockito.spy(blibliotecario);
		Mockito.doReturn(true).when(blibliotecario).esPalindromo(isbn);

		// act
		try {
			blibliotecario.prestar(isbn, nombre) ;
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.CODIGO_PALINDROMO, e.getMessage());
			verify(blibliotecario, times(1)).esPalindromo(isbn);
			verify(repositorioLibro, times(1)).obtenerPorIsbn(isbn);
			verify(blibliotecario, times(0)).esPrestado(isbn);
		}
	}

}
