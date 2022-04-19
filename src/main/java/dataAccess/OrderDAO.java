package dataAccess;

import connection.ConnectionFactory;
import model.Order;

import java.sql.*;
import java.util.logging.Logger;

public class OrderDAO extends GenericDAO<Order>{
    protected static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    public int insert(Order order) {
        return super.insert(order);
    }
}
