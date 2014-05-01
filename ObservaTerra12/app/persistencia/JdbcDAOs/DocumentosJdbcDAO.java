package persistencia.JdbcDAOs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Document;
import model.User;
import persistencia.DocumentosDAO;
import persistencia.implJdbc.DocumentosJdbc;
import persistencia.implJdbc.UsuariosJdbc;
import utils.DBConnection;

public class DocumentosJdbcDAO implements DocumentosDAO {

	private DocumentosJdbc documentosJDBC;

	public DocumentosJdbcDAO() {
		this.documentosJDBC = new DocumentosJdbc();
	}

	public DocumentosJdbc getDocumentosJDBC() {
		return documentosJDBC;
	}

	public void setDocumentosJDBC(DocumentosJdbc documentosJDBC) {
		this.documentosJDBC = documentosJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOseDocumentosDAO#guardarDocumento(model.Document)
	 */
	@Override
	public Document guardarDocumento(Document documento) throws SQLException,
			IOException {
		if (documento == null)
			throw new IllegalArgumentException(
					"No se ha indicado el documento a guardar");
		else if (documento.getFile() == null)
			throw new IllegalArgumentException(
					"No hay ningún archivo para guardar");
		else if (documento.getUser() == null)
			throw new IllegalArgumentException(
					"Se requiere al usuario propietario del documento");

		Connection con = null;
		Long idDocumento = null;
		try {
			con = DBConnection.getConnection();
			con.setAutoCommit(false);
			this.documentosJDBC.setConnection(con);
			idDocumento = this.documentosJDBC.guardarDocumento(
					documento.getUser(), documento.getFile(),
					documento.getName());
			documento.setIdDocumento(idDocumento);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw new SQLException("");
		} finally {
			con.close();
		}

		return documento;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.DocumentosDAO#leerDocumento(java.lang.Long)
	 */
	@Override
	public Document leerDocumento(Long idDocumento) throws SQLException,
			IOException {
		if (idDocumento == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador del documento");

		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);

		// Leer el documento
		Document doc = this.documentosJDBC.leerDocumento(idDocumento);
		doc.setName(doc.getName());

		// Buscar al usuario
		UsuariosJdbc usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection(con);
		User usuario = usuariosJDBC.leerUsuario(doc.getUser().getIdUser());

		// asignar al documento
		doc.setUser(usuario);

		con.close();
		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.DocumentosDAO#borrarDocumento(model.Document)
	 */
	@Override
	public void borrarDocumento(Document documento) throws SQLException {
		if (documento == null)
			throw new IllegalArgumentException(
					"No se ha indicado el documento a guardar");
		else if (documento.getFile() == null)
			throw new IllegalArgumentException(
					"No hay ningún archivo para guardar");
		else if (documento.getUser() == null)
			throw new IllegalArgumentException(
					"Se requiere al usuario propietario del documento");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.documentosJDBC.setConnection(con);
			this.documentosJDBC.anularComparticionesDocumento(documento);
			this.documentosJDBC.borrarDocumento(documento.getUser(),
					documento.getIdDocumento());
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} catch (SecurityException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.DocumentosDAO#listarRepositoriosUsuario(model.User)
	 */
	@Override
	public List<Document> listarRepositoriosUsuario(User usuario)
			throws SQLException, IOException {
		if (usuario == null)
			throw new IllegalArgumentException("No se ha indicado el usuario.");
		else if (usuario.getIdUser() == null)
			throw new IllegalArgumentException(
					"El usuario indicado no tiene identificador único.");

		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		// Carga la lista de identificadores
		List<Long> a = this.documentosJDBC.listarRepositoriosUsuario(usuario);

		List<Document> documentos = new ArrayList<Document>();

		// Crea un objeto del modelo de dominio, leyendo los archivos
		for (Long identificador : a) {
			Document documento = this.documentosJDBC
					.leerDocumento(identificador);
			documento.setUser(usuario);
			documentos.add(documento);
		}

		con.close();
		return documentos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.DocumentosDAO#listarRespositoriosAccesiblesUsuario
	 * (model.User)
	 */
	@Override
	public List<Document> listarRespositoriosAccesiblesUsuario(User usuario)
			throws SQLException, IOException {
		if (usuario == null)
			throw new IllegalArgumentException("No se ha indicado el usuario.");
		else if (usuario.getIdUser() == null)
			throw new IllegalArgumentException(
					"El usuario indicado no tiene identificador único.");

		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		List<Long> a = this.documentosJDBC
				.listarRespositoriosAccesiblesUsuario(usuario);

		List<Document> documentos = new ArrayList<Document>();
		// Crea un objeto del modelo de dominio, leyendo los archivos
		for (Long identificador : a) {
			Document documento = this.documentosJDBC
					.leerDocumento(identificador);
			documento.setIdDocumento(identificador);
			documentos.add(documento);
		}

		con.close();
		return documentos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.DocumentosDAO#compartirRepositorioConUsuario(model
	 * .Document, model.User)
	 */
	@Override
	public void compartirRepositorioConUsuario(Document documento,
			User usuarioACompartir) throws SQLException {
		if (documento == null || documento.getUser() == null
				|| documento.getFile() == null)
			throw new IllegalArgumentException("Parámetro documento malformado");
		else if (usuarioACompartir == null)
			throw new IllegalArgumentException("No se ha indicado el usuario.");
		else if (usuarioACompartir.getIdUser() == null
				|| documento.getUser().getIdUser() == null)
			throw new IllegalArgumentException(
					"El usuario indicado no tiene identificador único.");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.documentosJDBC.setConnection(con);
			this.documentosJDBC.compartirRepositorioConUsuario(
					documento.getIdDocumento(), usuarioACompartir,
					documento.getUser());
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.DocumentosDAO#anularCompartirRepositorioConUsuario
	 * (model.Document, model.User)
	 */
	@Override
	public void anularCompartirRepositorioConUsuario(Document documento,
			User usuarioACompartir) throws SQLException {
		if (documento == null || documento.getUser() == null
				|| documento.getFile() == null)
			throw new IllegalArgumentException("Parámetro documento malformado");
		else if (usuarioACompartir == null)
			throw new IllegalArgumentException("No se ha indicado el usuario.");
		else if (usuarioACompartir.getIdUser() == null
				|| documento.getUser().getIdUser() == null)
			throw new IllegalArgumentException(
					"El usuario indicado no tiene identificador único.");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.documentosJDBC.setConnection(con);
			this.documentosJDBC.anularCompartirRepositorioConUsuario(
					documento.getIdDocumento(), usuarioACompartir,
					documento.getUser());
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

}
