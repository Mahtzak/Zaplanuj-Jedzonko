package pl.coderslab.web;

import org.h2.engine.Session;
import pl.coderslab.dao.AdminsDao;
import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AppRecipeAdd", urlPatterns = "/app/recipe/add")
public class AppRecipeAdd extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String recipeName = request.getParameter("recipeName");   // nazwa
        String description = request.getParameter("description");   //opis
        String preparation = request.getParameter("preparation");   //sposob
        String preparationTime = request.getParameter("preparationTime");   // czas
        String ingredients = request.getParameter("ingredients");   //skladniki
        HttpSession session = request.getSession();
        int id = (Integer)session.getAttribute("id");
        Recipe recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setPreparation(preparation);
        recipe.setDescription(description);
        recipe.setPreparationTime( Integer.parseInt( preparationTime ) );
        recipe.setIngredients(ingredients);
        recipe.setAdminId(id);
        RecipeDao recipeDao = new RecipeDao();
        recipeDao.create(recipe);
        response.sendRedirect("/app/recipe/list");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/appAddRecipe.jsp");
    }
}
