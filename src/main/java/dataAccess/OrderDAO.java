package dataAccess;

import model.OrderT;

public class OrderDAO extends GenericDAO<OrderT>{
    public int insert(OrderT order) {
        return super.insert(order);
    }
}
