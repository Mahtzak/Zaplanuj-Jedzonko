package pl.coderslab.web;

import pl.coderslab.dao.RecipeDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AppRecipeDelete", urlPatterns = "/app/recipe/delete")
public class AppRecipeDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt( request.getParameter( "recipeIdtoDelete" ) );
        System.out.println( "ID recipe to DELETE is: " + id );
        RecipeDao.delete( id );
response.sendRedirect( "/appRecipes.jsp" );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
