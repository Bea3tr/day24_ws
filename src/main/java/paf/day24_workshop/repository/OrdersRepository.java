package paf.day24_workshop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import paf.day24_workshop.models.OrderDetails;
import paf.day24_workshop.models.Order;
import static paf.day24_workshop.repository.Queries.*;

import java.util.List;

@Repository
public class OrdersRepository {

    @Autowired
    private JdbcTemplate template;

    public int getOrderId(Order order) {
        int id = 0;
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDER_ID, order.getCustomerName(), order.getShipAddress());
        while(rs.next()) 
            id = rs.getInt("order_id");
        return id;
    }

    public boolean insertOrder(Order order) {
        return template.update(SQL_INSERT_ORDER, order.getCustomerName(),
            order.getShipAddress(), order.getNotes(), order.getTax()) > 0;
    }

    public void addOrderDetails(Order order) {
        addOrderDetails(order.getOrderDetails(), order.getOrderId());
    }

    public void addOrderDetails(List<OrderDetails> ods, int orderId) {
        List<Object[]> data = ods.stream()
            .map(od -> {
                Object[] obj = new Object[5];
                obj[0] = od.getProduct();
                obj[1] = od.getUnitPrice();
                obj[2] = od.getDiscount();
                obj[3] = od.getQuantity();
                obj[4] = orderId;
                return obj;
            })
            .toList();
        template.batchUpdate(SQL_INSERT_ORDER_DETAILS, data);
    }
    
}
