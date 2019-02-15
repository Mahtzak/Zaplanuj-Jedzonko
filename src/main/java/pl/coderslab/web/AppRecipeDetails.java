package pl.coderslab.web;

import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AppRecipeDetails", urlPatterns = "/app/recipe/details")
public class AppRecipeDetails extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        response.sendRedirect( "/appRecipeDetails.jsp" );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt( request.getParameter( "recipeIdDetails" ) );
        System.out.println( "ID recipe to SHOW DETAILS is: " + id );
        Recipe recipe = new Recipe(  );
        recipe = RecipeDao.read( id );
        System.out.println(recipe.toString());

        HttpSession session = request.getSession();
        session.setAttribute( "name", recipe.getName() );
        session.setAttribute( "description", recipe.getDescription() );
        session.setAttribute( "preparationTime", recipe.getPreparationTime() );
        session.setAttribute( "preparation", recipe.getPreparation() );
        session.setAttribute( "ingredients", recipe.getIngredients() );


        response.sendRedirect( "/appRecipeDetails.jsp" );
    }
}
