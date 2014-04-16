package controllers;

import static play.data.Form.form;

import java.sql.SQLException;

import akka.dispatch.Filter;
import model.User;
import persistence.JDBCFactory;
import persistence.fachada.UsuariosGateway;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

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
				UsuariosGateway usuarios = JDBCFactory.createUsuariosFactory();
				User user = usuarios.leerUsuario(userName, password);
				if (user == null)
					return "Usuario o contraseña incorrecta.";
				return null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "Error al acceder a la base de datos.";
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
    		return redirect(routes.Application.options());
    	}
    }
    
    public static Result options() {
    	if (session().get("userName") != null)
    		return ok(options.render());
    	return redirect(routes.Application.error());
    }

    public static Result error() {
        return badRequest(error.render());
    }
    
    public static Result logout() {
    	session().clear();
        return redirect(routes.Application.index());
    }
    
    public static Result histogram() {
        return ok(histogram.render("Histogram"));
    }
    
    public static Result pieChart() {
        return ok(piechart.render("Pie Chart"));
    }
    
    public static Result bubbleChart() {
        return ok(piechart.render("BubbleChart"));
    }

}
