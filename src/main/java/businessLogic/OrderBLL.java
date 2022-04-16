package businessLogic;

import dataAccess.OrderDAO;
import model.Order;

public class OrderBLL {
    public OrderBLL() {
    }

    public int insertOrder(Order order) {
        return OrderDAO.insert(order);
    }

}
