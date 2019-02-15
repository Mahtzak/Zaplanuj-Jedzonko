package pl.coderslab.dao;

import pl.coderslab.exception.NotFoundException;
import pl.coderslab.model.Recipe;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDao {
    // ZAPYTANIA SQL
    private static final String CREATE_RECIPE_QUERY = "INSERT INTO recipe(name,ingredients,description,created,updated,preparation_time,admin_id,preparation) VALUES (?,?,?,NOW(),NOW(),?,?,?)";
    private static final String DELETE_RECIPE_QUERY = "DELETE FROM recipe where id = ?";
    private static final String FIND_ALL_RECIPE_QUERY = "SELECT * FROM recipe";
    private static final String READ_RECIPE_QUERY = "SELECT * from recipe where id = ?";
    private static final String UPDATE_RECIPE_QUERY = "UPDATE	recipe SET name = ?, ingredients = ?, description = ?,updated = NOW(),preparation_time = ?, preparation = ?, admin_id = ? WHERE id = ?";
    private static final String SELECT_COUNT_RECIPE_QUERY = "select count(*) as recipeCount FROM recipe where admin_id = ?";
    private static final String FIND_ALL_RECIPES_FROM_USER_QUERY = "SELECT * FROM recipe WHERE admin_id = ?";

    /**
     * Get recipe by id
     *
     * @param recipeId
     * @return
     */
    public static Recipe read(int recipeId) {
        Recipe recipe = new Recipe();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement( READ_RECIPE_QUERY );
        ) {
            statement.setInt( 1, recipeId );
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    recipe.setId( resultSet.getInt( "id" ) );
                    recipe.setName( resultSet.getString( "name" ) );
                    recipe.setIngredients( resultSet.getString( "ingredients" ) );
                    recipe.setDescription( resultSet.getString( "description" ) );
                    recipe.setCreated( resultSet.getString( "created" ) );
                    recipe.setUpdated( resultSet.getString( "updated" ) );
                    recipe.setPreparationTime( resultSet.getInt( "preparation_time" ) );
                    recipe.setPreparation( resultSet.getString( "preparation" ) );
                    recipe.setAdminId( resultSet.getInt( "admin_id" ) );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipe;

    }

    /**
     * Return all recipe
     *
     * @return recipe
     */
    public List<Recipe> findAll() {
        List<Recipe> recipeList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement( FIND_ALL_RECIPE_QUERY );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Recipe recipeToAdd = new Recipe();
                recipeToAdd.setId( resultSet.getInt( "id" ) );
                recipeToAdd.setName( resultSet.getString( "name" ) );
                recipeToAdd.setIngredients( resultSet.getString( "ingredients" ) );
                recipeToAdd.setDescription( resultSet.getString( "description" ) );
                recipeToAdd.setCreated( resultSet.getString( "created" ) );
                recipeToAdd.setUpdated( resultSet.getString( "updated" ) );
                recipeToAdd.setPreparationTime( resultSet.getInt( "preparationTime" ) );
                recipeToAdd.setPreparation( resultSet.getString( "preparation" ) );
                recipeToAdd.setAdminId( resultSet.getInt( "adminId" ) );
                recipeList.add( recipeToAdd );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipeList;

    }

    /**
     * Create recipe
     *
     * @param recipe
     * @return
     */
//    poprawic sety i Stm - co ustawiac?
    public Recipe create(Recipe recipe) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement insertStm = connection.prepareStatement( CREATE_RECIPE_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS )) {
            insertStm.setString( 1, recipe.getName() );
            insertStm.setString( 2, recipe.getIngredients() );
            insertStm.setString( 3, recipe.getDescription() );
//            insertStm.setString(4, recipe.getCreated());
//            insertStm.setString(5, recipe.getUpdated());
            insertStm.setInt( 4, recipe.getPreparationTime() );
            insertStm.setInt( 5, recipe.getAdminId() );
            insertStm.setString( 6, recipe.getPreparation() );
            int result = insertStm.executeUpdate();

            if (result != 1) {
                throw new RuntimeException( "Execute update returned " + result );
            }

            try (ResultSet generatedKeys = insertStm.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    recipe.setId( generatedKeys.getInt( 1 ) );
                    return recipe;
                } else {
                    throw new RuntimeException( "Generated key was not found" );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Remove recipe by id
     *
     * @param recipeId
     */
    static public void delete(int recipeId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement( DELETE_RECIPE_QUERY );) {
            statement.setInt( 1, recipeId );
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException( "Recipe not found" );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Update recipe
     *    private static final String UPDATE_RECIPE_QUERY = "UPDATE	recipe SET name = ?,
     *    ingredients = ?, description = ?,updated = NOW(),preparation_time = ?, preparation = ?,
     *    admin_id = ? WHERE id = ?";
     * @param recipe
     */
    public void update(Recipe recipe) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement( UPDATE_RECIPE_QUERY );) {
            statement.setInt( 7, recipe.getId() );
            statement.setString( 1, recipe.getName() );
            statement.setString( 2, recipe.getIngredients() );
            statement.setString( 3, recipe.getDescription() );
//            statement.setString(4, recipe.getUpdated());
            statement.setInt( 4, recipe.getPreparationTime() );
            statement.setString( 5, recipe.getPreparation() );
            statement.setInt( 6, recipe.getAdminId() );

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int recipeCount(int id) {
        int count = 0;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement( SELECT_COUNT_RECIPE_QUERY )) {
            statement.setInt( 1, id );
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                count = resultSet.getInt( "recipeCount" );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public static List<Recipe> findAllFromUser(int id) {
        List<Recipe> recipeList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement( FIND_ALL_RECIPES_FROM_USER_QUERY );) {
            statement.setInt( 1, id );
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Recipe recipeToAdd = new Recipe();
                    recipeToAdd.setId( resultSet.getInt( "id" ) );
                    recipeToAdd.setName( resultSet.getString( "name" ) );
                    recipeToAdd.setIngredients( resultSet.getString( "ingredients" ) );
                    recipeToAdd.setDescription( resultSet.getString( "description" ) );
                    recipeToAdd.setCreated( resultSet.getString( "created" ) );
                    recipeToAdd.setUpdated( resultSet.getString( "updated" ) );
                    recipeToAdd.setPreparationTime( resultSet.getInt( "preparation_time" ) );
                    recipeToAdd.setPreparation( resultSet.getString( "preparation" ) );
                    recipeToAdd.setAdminId( resultSet.getInt( "admin_id" ) );
                    recipeList.add( recipeToAdd );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipeList;

    }

}