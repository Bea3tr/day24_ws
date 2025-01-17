package paf.day24_workshop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import paf.day24_workshop.models.PurchaseOrder;
import static paf.day24_workshop.repository.Queries.*;

@Repository
public class PurchaseOrderRepository {

    @Autowired
    private JdbcTemplate template;

    public boolean insertPurchaseOrder(PurchaseOrder po){
        return template.update(SQL_INSERT_PURCHASE_ORDER,
            po.getOrderId(), po.getName()) > 0;
    }
    
}
