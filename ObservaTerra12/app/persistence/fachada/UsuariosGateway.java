package persistence.fachada;

import java.sql.SQLException;

import model.User;

public interface UsuariosGateway {
	
	public User leerUsuario(String nombreUsuario, String claveUsuario) throws SQLException;
}