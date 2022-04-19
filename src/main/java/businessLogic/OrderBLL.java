package businessLogic;

import dataAccess.OrderDAO;
import model.OrderT;

public class OrderBLL {
    OrderDAO orderDAO;
    public OrderBLL() {
        this.orderDAO = new OrderDAO();
    }

    public int insertOrder(OrderT order) {
        return orderDAO.insert(order);
    }

}
