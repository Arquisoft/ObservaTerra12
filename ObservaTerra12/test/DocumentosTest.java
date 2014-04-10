import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import persistencia.JdbcDAOs.DocumentosJdbc;


import model.User;

public class DocumentosTest {

	private File file;
	private Long idDocumento;
	
	@Before
	public void un_fichero_de_texto() throws Throwable 
	{
		//INSERTAR UN ARCHIVO DE PRUEBA
		this.file = new File("");
		DocumentosJdbc docs = new DocumentosJdbc();
		User usuario = new User();
		usuario.setIdUsuario(1);
		idDocumento = docs.guardarDocumento(usuario, file);
	}
	
	@Test
	public void lo_recupero_y_comparo() throws SQLException, IOException
	{
		DocumentosJdbc docs = new DocumentosJdbc();
		File recovered = docs.leerDocumento(idDocumento);
	
		//Descomentar para crear fichero recovered.createNewFile();
		
		BufferedReader bfRecovered = new BufferedReader(new FileReader(recovered));
		BufferedReader bfOriginal = new BufferedReader(new FileReader(file));
		
		
		String line = bfOriginal.readLine();
		String line2;
		
		while(line != null)
		{	
			line2 = bfRecovered.readLine();
			org.junit.Assert.assertEquals(line, line2);
			line = bfOriginal.readLine();
		}
		
		bfRecovered.close();
		bfOriginal.close();	
	}
	
	
}
	