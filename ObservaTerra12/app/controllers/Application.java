package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
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
