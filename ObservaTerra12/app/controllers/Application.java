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
    	try {
	    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
	    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
	    	
	    	User user = usuariosDao.buscarUsuario(session().get("userName"));
	    	List<Document> documentos = documentosDao.listarRepositoriosUsuario(user);
	    	
	        return ok(documents.render(documentos));
	        
    	} catch (SQLException e) {
            return badRequest(error.render());
    	} catch (IOException e) {
        return badRequest(error.render());
	}
    }
}