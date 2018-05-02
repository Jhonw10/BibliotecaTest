package dominio.integracion;

import static org.junit.Assert.fail;

import dominio.Prestamo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import org.mockito.Mockito;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.LibroTestDataBuilder;

import java.util.Calendar;

public class BibliotecarioTest {

	private static final String CRONICA_DE_UNA_MUERTA_ANUNCIADA = "Cronica de una muerta anunciada";
	private static final String EL_NUMERO_1 = "El numero 1";

	private SistemaDePersistencia sistemaPersistencia;
	
	private RepositorioLibro repositorioLibros;
	private RepositorioPrestamo repositorioPrestamo;

	@Before
	public void setUp() {
		
		sistemaPersistencia = new SistemaDePersistencia();
		
		repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
		repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();
		
		sistemaPersistencia.iniciar();
	}
	

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void prestarLibroTest() {

		// arrange
		String nombre = "Jhon Olarte";
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), nombre);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()));

	}

	@Test
	public void prestarLibroNoDisponibleTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		String nombre = "Jhon Olarte";
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), nombre);
		try {
			
			blibliotecario.prestar(libro.getIsbn(), nombre);
			fail();
			
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
		}
	}

	@Test
	public void debeAlmacenarNombreDeUsuarioYFechaMaximaDeEntregaTest(){
		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(EL_NUMERO_1).conIsbn("9999T").build();
		String nombre = "Jhon Olarte";
		repositorioLibros.agregar(libro);
		Calendar fechaInicial = Calendar.getInstance();
		fechaInicial.set(Calendar.MONTH, 4);
		fechaInicial.set(Calendar.DAY_OF_MONTH, 26);
		fechaInicial.set(Calendar.YEAR, 2017);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		blibliotecario = Mockito.spy(blibliotecario);
		Mockito.doReturn(fechaInicial).when(blibliotecario).getFechaSolicitud();

		// act
		blibliotecario.prestar(libro.getIsbn(), nombre);
		Prestamo prestamo = repositorioPrestamo.obtener(libro.getIsbn());
		Calendar fechaMaxima = Calendar.getInstance();
		fechaMaxima.setTime(prestamo.getFechaEntregaMaxima());

		// assert
		Mockito.verify(blibliotecario).esPrestado(libro.getIsbn());
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(prestamo);
		Assert.assertTrue(prestamo.getNombreUsuario().equals("Jhon Olarte"));
		Assert.assertTrue(fechaMaxima.get(Calendar.DAY_OF_MONTH) == 12);
		Assert.assertTrue(fechaMaxima.get(Calendar.MONTH) == 5);
		Mockito.verify(blibliotecario).getFechaSolicitud();
		Mockito.verify(blibliotecario).esPalindromo(libro.getIsbn());
		Mockito.verify(blibliotecario).eliminarCaracteres(libro.getIsbn());
		Mockito.verify(blibliotecario).sumarDigitos(Mockito.any());
		Mockito.verify(blibliotecario).calcularFechaMaxima(Mockito.any());
	}
}
