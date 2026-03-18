package org.sundeep.two.phase.commit.simple.food.service;

import org.sundeep.two.phase.commit.simple.food.configs.DeliveryDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodService {
    private final DeliveryDb deliveryDb = new DeliveryDb();

    public int reserveFoodForOrder(String orderId) throws IOException, SQLException {
        // Logic to reserve an agent for the given order ID
        // This could involve checking available food, reserving one, and returning the food details
        // Reserve the first available agent for assignment
        Integer foodId = null;

        Connection conn = deliveryDb.getDataSource().getConnection();
        try {

            PreparedStatement selectStmt = conn.prepareStatement("SELECT ID, RESERVED_FOR, CURRENT_ORDER FROM FOODS " +
                    "WHERE RESERVED_FOR IS NULL AND CURRENT_ORDER IS NULL LIMIT 1 FOR UPDATE");

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    foodId = rs.getInt(1);
                }
            }
            if (foodId == null) {
                throw new IOException("No available foods to reserve for order " + orderId);
            }

            PreparedStatement updateStmt = conn.prepareStatement("UPDATE FOODS SET RESERVED_FOR = ? WHERE ID = ?");
            updateStmt.setString(1, orderId);
            updateStmt.setInt(2, foodId);

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new IOException("Failed to reserve agent for order " + orderId);
            }

            conn.commit();
            System.out.println("[ReserveFood][INFO]: Reserved food " + foodId + " to order " + orderId + " for assignment");

        } catch (Exception e) {
            conn.rollback();
            System.out.println("[ReserveFood][ERROR]: " + e.getMessage());
            throw new IOException("Failed to reserve agent for order " + orderId, e);
        } finally {
            conn.close();
        }
        return foodId;
    }

    public void assignFoodToOrder(String orderId, String foodId) throws IOException, SQLException {
        // Logic to assign the reserved food to the order
        // This could involve updating the order status, notifying the agent, etc.
        Connection conn = deliveryDb.getDataSource().getConnection();

        try {

            PreparedStatement selectStmt = conn.prepareStatement("SELECT ID, RESERVED_FOR, CURRENT_ORDER FROM FOODS " +
                    "WHERE RESERVED_FOR = ? AND CURRENT_ORDER IS NULL AND ID = ? FOR UPDATE");
            selectStmt.setString(1, orderId);
            selectStmt.setInt(2, Integer.parseInt(foodId));

            Integer fetchedFoodId = null;
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    fetchedFoodId = rs.getInt(1);
                }
            }
            if (fetchedFoodId == null || fetchedFoodId != Integer.parseInt(foodId)) {
                throw new IOException("No available food to reserve for order " + orderId);
            }

            PreparedStatement updateStmt = conn.prepareStatement("UPDATE FOODS SET RESERVED_FOR = NULL, CURRENT_ORDER = ? WHERE ID = ?");
            updateStmt.setString(1, orderId);
            updateStmt.setInt(2, fetchedFoodId);

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new IOException("Failed to assign food for order " + orderId);
            }

            conn.commit();

            System.out.println("[AssignFood][INFO]: Assigned food " + foodId + " to order " + orderId + " successfully");

        } catch (Exception e) {

            conn.rollback();
            System.out.println("[AssignFood][ERROR]: " + e.getMessage());
            throw new IOException("Failed to assign food for order " + orderId, e);
        } finally {
            conn.close();
        }
    }
}
