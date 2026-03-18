package org.sundeep.two.phase.commit.simple.delivery.service;

import org.sundeep.two.phase.commit.simple.delivery.confgs.DeliveryDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AgentService {
    private final DeliveryDb deliveryDb = new DeliveryDb();

    public int reserveAgentForOrder(String orderId) throws IOException {
        // Logic to reserve an agent for the given order ID
        // This could involve checking available agents, reserving one, and returning the agent details
        // Reserve the first available agent for assignment
        Integer agentId = null;

        try (Connection conn = deliveryDb.getDataSource().getConnection()) {

            PreparedStatement selectStmt = conn.prepareStatement("SELECT ID, RESERVED_FOR, CURRENT_ORDER FROM AGENTS " +
                    "WHERE RESERVED_FOR IS NULL AND CURRENT_ORDER IS NULL LIMIT 1 FOR UPDATE");

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    agentId = rs.getInt(1);
                }
            }
            if (agentId == null) {
                throw new IOException("No available agents to reserve for order " + orderId);
            }

            PreparedStatement updateStmt = conn.prepareStatement("UPDATE AGENTS SET RESERVED_FOR = ? WHERE ID = ?");
            updateStmt.setString(1, orderId);
            updateStmt.setInt(2, agentId);

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new IOException("Failed to reserve agent for order " + orderId);
            }

        } catch (Exception e) {
            System.out.println("[ReserveAgent][ERROR]: " + e.getMessage());
            throw new IOException("Failed to reserve agent for order " + orderId, e);
        }
        System.out.println("[ReserveAgent][INFO]: Reserved agent " + agentId + " to order " + orderId + " for assignment");
        return agentId;
    }

    public void assignAgentToOrder(String orderId, String agentId) throws IOException {
        // Logic to assign the reserved agent to the order
        // This could involve updating the order status, notifying the agent, etc.
        try (Connection conn = deliveryDb.getDataSource().getConnection()) {

            PreparedStatement selectStmt = conn.prepareStatement("SELECT ID, RESERVED_FOR, CURRENT_ORDER FROM AGENTS " +
                    "WHERE RESERVED_FOR = ? AND CURRENT_ORDER IS NULL AND ID = ? FOR UPDATE");
            selectStmt.setString(1, orderId);
            selectStmt.setInt(2, Integer.parseInt(agentId));

            Integer fetchedAgentId = null;
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    fetchedAgentId = rs.getInt(1);
                }
            }
            if (fetchedAgentId == null || fetchedAgentId != Integer.parseInt(agentId)) {
                throw new IOException("No available agents to reserve for order " + orderId);
            }

            PreparedStatement updateStmt = conn.prepareStatement("UPDATE AGENTS SET RESERVED_FOR = NULL, CURRENT_ORDER = ? WHERE ID = ?");
            updateStmt.setString(1, orderId);
            updateStmt.setInt(2, fetchedAgentId);

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                conn.rollback();
                throw new IOException("Failed to assign agent for order " + orderId);
            }

        } catch (Exception e) {
            System.out.println("[AssignAgent][ERROR]: " + e.getMessage());
            throw new IOException("Failed to assign agent for order " + orderId, e);
        }
        System.out.println("[AssignAgent][INFO]: Assigned agent " + agentId + " to order " + orderId + " successfully");
    }
}
