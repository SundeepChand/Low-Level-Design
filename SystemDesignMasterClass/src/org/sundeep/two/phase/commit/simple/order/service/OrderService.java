package org.sundeep.two.phase.commit.simple.order.service;

import org.sundeep.two.phase.commit.simple.order.config.UrlConfig;
import org.sundeep.two.phase.commit.simple.order.utils.HttpClient;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class OrderService {

    public void placeOrder(String orderId) {
        // Try to reserve food and agent parallelly.
        String reserveFoodUrl = UrlConfig.FOOD_SERVICE_URL + "/prepare?orderId=" + orderId;
        CompletableFuture<HttpResponse<String>> reserveFoodResponse = HttpClient.getAsync(reserveFoodUrl);

        String reserveAgentUrl = UrlConfig.DELIVERY_SERVICE_URL + "/prepare?orderId=" + orderId;
        CompletableFuture<HttpResponse<String>> reserveAgentResponse = HttpClient.getAsync(reserveAgentUrl);

        CompletableFuture.allOf(reserveAgentResponse, reserveFoodResponse).join();

        try {
            int agentId = Integer.parseInt(reserveAgentResponse.join().body());
            int foodId = Integer.parseInt(reserveFoodResponse.join().body());
            System.out.println("[OrderService][INFO]: Order " + orderId + " reserved with agent " + agentId + " and food " + foodId);

            // If both reservations are successful, proceed to commit phase
            String commitAgentUrl = UrlConfig.DELIVERY_SERVICE_URL + "/assign?orderId=" + orderId + "&agentId=" + agentId;
            CompletableFuture<HttpResponse<String>> commitAgentResponse = HttpClient.getAsync(commitAgentUrl);

            String commitFoodUrl = UrlConfig.FOOD_SERVICE_URL + "/assign?orderId=" + orderId + "&foodId=" + foodId;
            CompletableFuture<HttpResponse<String>> commitFoodResponse = HttpClient.getAsync(commitFoodUrl);

            CompletableFuture.allOf(commitAgentResponse, commitFoodResponse).join();

            if (commitAgentResponse.join().statusCode() == 200 && commitFoodResponse.join().statusCode() == 200) {
                System.out.println("[OrderService][INFO]: Order " + orderId + " committed successfully with agent " + agentId + " and food " + foodId);
            } else {
                System.out.println("[OrderService][ERROR]: Failed to commit order " + orderId + ". Agent Resp: " + commitAgentResponse.join().body() + " | Food Resp: " + commitFoodResponse.join().body());
            }
        } catch (Exception e) {
            System.out.println("Failed to place order " + orderId + ": " + e.getMessage());
        }
    }

}
