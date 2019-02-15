package pl.coderslab.dao;

import pl.coderslab.exception.NotFoundException;
import pl.coderslab.model.Plan;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanDao {
    private static final String CREATE_PLAN_QUERY = "INSERT INTO plan(name,description,created,admin_id) VALUES (?,?,NOW(),?)";
    private static final String DELETE_PLAN_QUERY = "DELETE FROM plan where id = ?";
    private static final String FIND_ALL_PLANS_QUERY = "SELECT * FROM plan";
    private static final String READ_PLAN_QUERY = "SELECT * from plan where id = ?";
    private static final String UPDATE_PLAN_QUERY = "UPDATE	plan SET name = ? , description = ?  WHERE	id = ?";
    private static final String HOW_MANY_PLANS = "SELECT COUNT(*) as plans FROM plan WHERE admin_id = ?";
    private static final String LAST_PLAN = "SELECT name FROM plan WHERE admin_id = ? ORDER BY created desc LIMIT 1";
    private static final String FIND_ALL_FROM_USER_QUERY = "SELECT * from plan where admin_id = ?";
    private static final String INSERT_INTO_RECIPE_PLAN_QUERY = "INSERT INTO recipe_plan (recipe_id,meal_name,ordernr,day_name_id,plan_id) VALUES (?,?,?,?,?)";

    /**
     * Get plan by id
     *
     * @param planId
     * @return
     */
    static public Plan read(int planId) {
        Plan plan = new Plan();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_PLAN_QUERY)
        ) {
            statement.setInt(1, planId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    plan.setId(resultSet.getInt("id"));
                    plan.setName(resultSet.getString("name"));
                    plan.setDescription(resultSet.getString("description"));
                    plan.setCreated(resultSet.getString("created"));
                    plan.setAdminId(resultSet.getInt("adminId"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plan;

    }

    /**
     * Return all plans
     *
     * @return
     */
    public static List<Plan> findAll() {
        List<Plan> planList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PLANS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Plan planToAdd = new Plan();
                planToAdd.setId(resultSet.getInt("id"));
                planToAdd.setName(resultSet.getString("name"));
                planToAdd.setDescription(resultSet.getString("description"));
                planToAdd.setCreated(resultSet.getString("created"));
                planToAdd.setAdminId(resultSet.getInt("adminId"));
                planList.add(planToAdd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planList;

    }

    /**
     * Create plan
     *
     * @param plan
     * @return
     */
    public static Plan create(Plan plan) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement insertStm = connection.prepareStatement(CREATE_PLAN_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStm.setString(1, plan.getName());
            insertStm.setString(2, plan.getDescription());
            //   insertStm.setString( 3, plan.getCreated() );
            insertStm.setInt(3, plan.getAdminId());
            int result = insertStm.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = insertStm.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    plan.setId(generatedKeys.getInt(1));
                    return plan;
                } else {
                    throw new RuntimeException("Generated key was not found");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Remove plan by id
     *
     * @param planId
     */
    static public void delete(int planId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PLAN_QUERY)) {
            statement.setInt(1, planId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Product not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Update plan
     *
     * @param plan
     */
    public void update(Plan plan) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PLAN_QUERY)) {
            statement.setInt(3, plan.getId());
            statement.setString(1, plan.getName());
            statement.setString(2, plan.getDescription());


            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int howManyPlans(int id) {

        int count = 666;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(HOW_MANY_PLANS);) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                count = resultSet.getInt("plans");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Plan> findAllFromUser(int id) {
        List<Plan> planList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_FROM_USER_QUERY);) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next())
                    if (id == resultSet.getInt("admin_id")) {
                        Plan planToAdd = new Plan();
                        planToAdd.setId(resultSet.getInt("id"));
                        planToAdd.setName(resultSet.getString("name"));
                        planToAdd.setDescription(resultSet.getString("description"));
                        planToAdd.setCreated(resultSet.getString("created"));
                        planToAdd.setAdminId(resultSet.getInt("admin_id"));
                        planList.add(planToAdd);
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planList;

    }

    public static String lastPlan(int id) {
        String lastPlan = "";
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(LAST_PLAN);) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                lastPlan = resultSet.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastPlan;
    }

    public static void newRecipePlan(int recipeId, String mealName, int order, int dayNameId, int planId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_RECIPE_PLAN_QUERY);) {
            statement.setInt(1, recipeId);
            statement.setString(2, mealName);
            statement.setInt(3, order);
            statement.setInt(4, dayNameId);
            statement.setInt(5, planId);
            statement.executeUpdate();
            /* ResultSet resultSet = statement.executeQuery();
             resultSet.close();*/
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
