package paf.day24_workshop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import paf.day24_workshop.models.LineItem;
import paf.day24_workshop.models.PurchaseOrder;
import static paf.day24_workshop.repository.Queries.*;

@Repository
public class LineItemRepository {

    @Autowired
    private JdbcTemplate template;

    public void addLineItems(PurchaseOrder po) {
        addLineItems(po.getLineItems(), po.getOrderId());
    }

    public void addLineItems(List<LineItem> lineItems, String orderId) {
        List<Object[]> data = lineItems.stream()
            .map(li -> {
                Object[] obj = new Object[3];
                obj[0] = li.getDescription();
                obj[1] = li.getQuantity();
                obj[2] = orderId;
                return obj;
            })
            .toList();
        template.batchUpdate(SQL_INSERT_LINE_ITEM, data);
    }
    
}
