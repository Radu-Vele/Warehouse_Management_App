package businessLogic;

import dataAccess.OrderDAO;
import model.Order;

public class OrderBLL {
    OrderDAO orderDAO;
    public OrderBLL() {
        this.orderDAO = new OrderDAO();
    }

    public int insertOrder(Order order) {
        return orderDAO.insert(order);
    }

}
