package controllers;

import static play.data.Form.form;

import java.io.File;
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
    	if (session().get("userName") != null)
    		return ok(user_panel.render());
    	return redirect(routes.Application.error());
    }

    public static Result error() {
        return badRequest(error.render());
    }
    
    public static Result logout() {
    	session().clear();
        return redirect(routes.Application.index());
    }
    
    public static Result documents() {
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
    	
    	List<Document> documentos;
    	
    	try {	 	    	
	    	User user = usuariosDao.buscarUsuario(session().get("userName"));
	    	documentos = documentosDao.listarRepositoriosUsuario(user);
    	} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
    	
    	return ok(documents.render(documentos));
    }
    
    public static Result downloadFile(Long id) {
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	
    	Document documento;
    	
		try {
			documento = documentosDao.leerDocumento(id);
		} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
		
        return ok(documento.getFile());
    }
    
    public static Result uploadFile() {
    	MultipartFormData body = request().body().asMultipartFormData();
		FilePart file = body.getFile("file");
		
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
    	
		try {
	    	Document documento = new Document();
	    	
	    	documento.setFile(file.getFile());
	    	
	    	User user = usuariosDao.buscarUsuario(session().get("userName"));
	    	documento.setUser(user);
	    	
	    	documento.setName(file.getFilename());
	    	
	    	documentosDao.guardarDocumento(documento);
		} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
		
        return ok(Messages.get("documents_document_upload_successfull"));
    }
}