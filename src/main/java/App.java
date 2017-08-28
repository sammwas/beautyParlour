import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";
    
    ProcessBuilder process = new ProcessBuilder();
    Integer port;

    // This tells our app that if Heroku sets a port for us, we need to use that port.
    // Otherwise, if they do not, continue using port 4567.

    if (process.environment().get("PORT") != null) {
        port = Integer.parseInt(process.environment().get("PORT"));
    } else {
        port = 4567;
    }

    setPort(port);
//index route
    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//new stylist route Form
    get("/stylists/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/stylistForm.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//Adding new Stylist details with POST
    post("/stylists", (request, response) ->{
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String skills = request.queryParams("skills");
      Stylist stylist = new Stylist(name,skills);
      stylist.save();
      model.put("template", "templates/stylistSuccess.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//All stylists route
    get("/stylists", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//Shows details for a particular stylist
    get("/stylists/:id" ,(request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("stylist",stylist);
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


//update stylists
    post("/stylists/:id" ,(request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("name");
      String skills = request.queryParams("skills");
      Stylist newStylist = new Stylist(name,skills);
      newStylist.update(name, skills);
      String url = String.format("/stylists/%d", stylist.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


//delete stylists
    post("/stylists/:id/delete" ,(request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      stylist.delete();
      model.put("stylists",Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//New client Form
    get("/stylists/:id/clients/new" ,(request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("stylist", stylist);
      model.put("template", "templates/clientForm.vtl");
      return new ModelAndView(model, layout);
   }, new VelocityTemplateEngine());

//Saving a new client Object
   post("/clients" ,(request, response) -> {
     Map<String, Object> model = new HashMap<String, Object>();
     Stylist stylist = Stylist.find(Integer.parseInt(request.queryParams("stylistId")));
     model.put("stylist", stylist);
     String name = request.queryParams("name");
     int stylistId = Integer.parseInt(request.queryParams("stylistId"));
     Client client = new Client(name, stylistId);
     client.save();
     model.put("client", client);
     model.put("template", "templates/clientSuccess.vtl");
     return new ModelAndView(model, layout);
   }, new VelocityTemplateEngine());


//Displaying a particular client's details
   get("/stylists/:stylist_id/clients/:id" ,(request, response) -> {
     Map<String, Object> model = new HashMap<String, Object>();
     Stylist stylist = Stylist.find(Integer.parseInt(request.params(":stylist_id")));
     Client client= Client.find(Integer.parseInt(request.params(":id")));
     model.put("stylist",stylist);
     model.put("client", client);
     model.put("template", "templates/client.vtl");
     return new ModelAndView(model, layout);
   }, new VelocityTemplateEngine());

   //update client's details
       post("/stylists/:stylist_id/clients/:id" ,(request, response) -> {
         Map<String, Object> model = new HashMap<String, Object>();
         Client client = Client.find(Integer.parseInt(request.params(":id")));
         Stylist stylist = Stylist.find(client.getStylistId());
         String name = request.queryParams("name");
         client.update(name);
         String url = String.format("/stylists/%d/clients/%d", stylist.getId(), client.getId());
         response.redirect(url);
         return new ModelAndView(model, layout);
       }, new VelocityTemplateEngine());


   //delete client
       post("/stylists/:stylist_id/clients/:id/delete" ,(request, response) -> {
         HashMap<String, Object> model = new HashMap<String, Object>();
         Client client = Client.find(Integer.parseInt(request.params(":id")));
         Stylist stylist = Stylist.find(client.getStylistId());
         client.delete();
         model.put("client",client);
         model.put("stylist", stylist);
         model.put("template", "templates/stylist.vtl");
         return new ModelAndView(model, layout);
       }, new VelocityTemplateEngine());


  }
}