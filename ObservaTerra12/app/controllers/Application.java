
package controllers;

import static play.data.Form.form;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Document;
import model.User;
import persistencia.DocumentosDAO;
import persistencia.PersistenceFactory;
import persistencia.UsuariosDAO;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.documents;
import views.html.error;
import views.html.index;
import views.html.user_panel;

public class Application extends Controller {
	
	/**
	 * Clase utilizada para el formulario de acceso a la aplicación.
	 * 
	 * @author Nacho & Manuel
	 */
	public static class Login {
		
		public String userName;
		public String password;
		
		public String validate() {
			try {
				UsuariosDAO usuarios = PersistenceFactory.createUsuariosDAO();
				User user = usuarios.leerUsuario(userName, password);
				if (user == null)
					return Messages.get("index_form_error_login");
				return null;
			} catch (SQLException e) {
				e.printStackTrace();
				return Messages.get("index_form_error_bd");
			}
		}
	}

    public static Result index() {
        return ok(index.render(form(Login.class)));
    }

    public static Result authenticate() {
	    Form<Login> loginForm = form(Login.class).bindFromRequest();
	    if (loginForm.hasErrors())
	    	return badRequest(index.render(loginForm));
	    else {
	    	session().clear();
	    	session("userName", loginForm.get().userName);
	    	return redirect(routes.Application.userPanel());
	    }
    }
    
    public static Result userPanel() {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
		return ok(user_panel.render());
    }

    public static Result error() {
        return badRequest(error.render());
    }
    
    public static Result logout() {
    	session().clear();
        return redirect(routes.Application.index());
    }
    
    public static Result documents() {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();

    	List<Document> documentos;
    	List<Document> compartidos;
    	
    	try {
	    	User user = usuariosDao.buscarUsuario(session().get("userName"));
	    	
	    	documentos = documentosDao.listarRepositoriosUsuario(user);
	    	compartidos = documentosDao.listarRespositoriosAccesiblesUsuario(user);
	    	
    	} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
    	
    	return ok(documents.render(documentos, compartidos));
    }
    
    public static Result downloadFile(Long id) {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
    	
    	Document documento;
    	
		try {
			documento = documentosDao.leerDocumento(id);
	    	
	    	// Permisos
			// * El usuario no es el dueño del fichero ni está compartido con él
	    	User user = usuariosDao.buscarUsuario(session().get("userName"));
			if (!documento.getUser().equals(user) &&
					!documentosDao.listarRespositoriosAccesiblesUsuario(user).contains(documento))
				return redirect(routes.Application.error());
			
		} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
		
        return ok(documento.getFile());
    }
    
    public static Result removeFile(Long id) {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	
		try {
			Document documento = documentosDao.leerDocumento(id);
			
	    	// Permisos
			// * El usuario no es el dueño del fichero
			if (!documento.getUser().getUserName().equals(session().get("userName")))
				return redirect(routes.Application.error());
			
			documentosDao.borrarDocumento(documento);
			
		} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
		
        return documents();
    }
    
    public static Result unshareFile(Long id) {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
    	
		try {
	    	User user = usuariosDao.buscarUsuario(session().get("userName"));
			Document documento = documentosDao.leerDocumento(id);
			
	    	// Permisos
			// * No necesarios, no hay problema al descompartir archivos
			
			documentosDao.anularCompartirRepositorioConUsuario(documento, user);
			
		} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
		
        return documents();
    }
    
    public static Result uploadFile() {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
    	MultipartFormData part = request().body().asMultipartFormData();
    	FilePart file = part.getFile("file");
	    
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();

		try {
	    	Document documento = new Document();

	    	documento.setFile(file.getFile());
	    	
	    	documento.setName(file.getFilename());
	    	
	    	User user = usuariosDao.buscarUsuario(session().get("userName"));
	    	documento.setUser(user);
	    	
	    	documentosDao.guardarDocumento(documento);
	    	
		} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
    	return documents();
    }
}