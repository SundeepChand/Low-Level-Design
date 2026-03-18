package org.sundeep.two.phase.commit.simple.order;

import org.sundeep.two.phase.commit.simple.order.service.OrderService;

public class Main {
    private static final OrderService orderService = new OrderService();

    public static void main(String[] args) {
        // As the number of threads increases, the contention between them increases and hence the total number of transactions
        // that succeed is very less than the total of items in the DB that can be assigned. This is because a transaction
        // can lock an agent but not be able to lock a food item & vice versa. To solve for this the resources should be
        // locked in a particular order. For example, if we always try to lock the agent first and then the food item, then
        // we can avoid the deadlock scenario and increase the number of successful transactions.
        for (int i = 0; i < 100; i++) {
            String orderId = "order-" + i;
            new Thread(() -> orderService.placeOrder(orderId)).start();
        }

        /*
        UPDATE AGENTS SET RESERVED_FOR=NULL;
        UPDATE AGENTS SET CURRENT_ORDER=NULL;
        UPDATE FOODS SET RESERVED_FOR=NULL;
        UPDATE FOODS SET CURRENT_ORDER=NULL;

         */

        // Query to view assigned orders
        // SELECT AGENTS.CURRENT_ORDER AS ORDER_ID, AGENTS.ID AS AGENT_ID, FOODS.ID AS FOOD_ID FROM AGENTS, FOODS WHERE AGENTS.CURRENT_ORDER = FOODS.CURRENT_ORDER;
    }
}
