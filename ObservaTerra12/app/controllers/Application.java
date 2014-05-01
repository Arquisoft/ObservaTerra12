
package controllers;

import static play.data.Form.form;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Document;
import model.Organization;
import model.User;
import persistencia.DocumentosDAO;
import persistencia.OrganizacionesDAO;
import persistencia.PersistenceFactory;
import persistencia.UsuariosDAO;
import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
	
	/**
	 * Clase utilizada para el formulario de acceso a la aplicación.
	 * 
	 * @author Nacho & Manuel
	 */
	public static class Login {

		@Required(message="index_form_error_user_name_required")
		public String userName;
		@Required(message="index_form_error_password_required")
		public String password;
		
		public String validate() {
			try {
				UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
				User user = usuariosDao.leerUsuario(userName, password);
				if (user == null)
					return Messages.get("index_form_error_login");
				return null;
			} catch (SQLException e) {
				e.printStackTrace();
				return Messages.get("error_bd");
			}
		}
	}
	
	/**
	 * Clase utilizada para el formulario de registro.
	 * 
	 * @author Nacho & Manuel
	 */
	public static class Register {
		
		@Required(message="register_form_error_user_name_required")
		public String userName;
		@Required(message="register_form_error_password_required")
		public String password;

		@Required(message="register_form_error_name_required")
		public String name;
		@Required(message="register_form_error_surname_required")
		public String surname;
		@Required(message="register_form_error_email_required")
		@Email(message="register_form_error_email_invalid")
		public String email;
		@Required(message="register_form_error_organization_required")
		public String organization;
		
		public String validate() {
			try {
				UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
				User user = usuariosDao.buscarUsuario(userName);
				if (user != null)
					return Messages.get("register_form_error_already_in_use");
				return null;
			} catch (SQLException e) {
				e.printStackTrace();
				return Messages.get("error_bd");
			}
		}
	}
	
    /**
     * Clase utilizada para el formulario de compartición de documentos.
     * 
     * @author Manuel & Nacho
     */
    public static class Share {
    	
    	public String userNames;
    	public List<String> compartidos;
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

    public static Result register() {
        return ok(register.render(form(Register.class)));
    }

    public static Result registerUser() {
	    Form<Register> registerForm = form(Register.class).bindFromRequest();
	    if (registerForm.hasErrors())
	    	return badRequest(register.render(registerForm));
	    else {
			UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
	    	OrganizacionesDAO organizacionesDao = PersistenceFactory.createOrganizacionesDAO();
	    	
	    	User user = new User();
	    	
	    	user.setUserName(registerForm.get().userName);
	    	user.setPassword(registerForm.get().password);

	    	user.setName(registerForm.get().name);
	    	user.setSurname(registerForm.get().surname);
	    	user.setEmail(registerForm.get().email);
	    	
	    	user.setRol("PENDIENTE");
	    	
	    	try {
		    	// Comprueba las organizaciones	
		    	Organization organizacion = organizacionesDao.buscarOrganizacionPorNombre(registerForm.get().organization);
		    	if (organizacion == null) {
		    		organizacion = new Organization();
		    		organizacion.setNombre(registerForm.get().organization);
		    		organizacion = organizacionesDao.crearOrganizacion(organizacion);
		    	}
		    	user.setOrganization(organizacion);
		    	
		    	user = usuariosDao.crearUsuario(user);
			} catch (SQLException e) {
				return badRequest(error.render());
			}
	    	
	    	// Loguea automáticamente al usuario
	    	session().clear();
	    	session("userName", user.getUserName());
	    	return redirect(routes.Application.userPanel());
	    }
    }

    public static Result error() {
        return badRequest(error.render());
    }
    
    public static Result userPanel() {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
		return ok(user_panel.render());
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
    	
    	return ok(documents.render(form(Share.class), documentos, compartidos));
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
    
    public static Result shareFile() {
    	if (session().get("userName") == null)
        	return redirect(routes.Application.error());
    	
	    Form<Share> shareForm = form(Share.class).bindFromRequest();
	    String[] nombres = shareForm.get().userNames.split(",");
    	
	    if(shareForm.get().compartidos == null || shareForm.get().compartidos.isEmpty())
	    	return documents();
	    	
    	DocumentosDAO documentosDao = PersistenceFactory.createDocumentosDAO();
    	UsuariosDAO usuariosDao = PersistenceFactory.createUsuariosDAO();
    	
		try {
	    	User usuario = usuariosDao.buscarUsuario(session().get("userName"));
			
			List<User> usuarios = new ArrayList<User>();
			for (String nombre : nombres) {
				if(nombre == null || nombre == "")
					continue;
				User user = usuariosDao.buscarUsuario(nombre);
				if (user != null)
					usuarios.add(user);
			}

			List<Document> documentos = new ArrayList<Document>();
			shareForm.get().compartidos.removeAll(Collections.singleton(null));
			for (String identificador : shareForm.get().compartidos) {
				Document document = documentosDao.leerDocumento(Long.valueOf(identificador));
				if (document != null && document.getUser().equals(usuario))
					documentos.add(document);
			}
			
			for (User user : usuarios)
				for (Document document : documentos)
					documentosDao.compartirRepositorioConUsuario(document, user);
			
		} catch (SQLException | IOException e) {
			return badRequest(error.render());
		}
		
        return documents();
    }
}