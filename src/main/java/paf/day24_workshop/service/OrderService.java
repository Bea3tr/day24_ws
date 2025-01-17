package paf.day24_workshop.service;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import paf.day24_workshop.exception.OrderException;
import paf.day24_workshop.models.Order;
import paf.day24_workshop.models.PurchaseOrder;
import paf.day24_workshop.repository.LineItemRepository;
import paf.day24_workshop.repository.OrdersRepository;
import paf.day24_workshop.repository.PurchaseOrderRepository;

@Service
public class OrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepo;

    @Autowired
    private LineItemRepository lineItemRepo;

    @Autowired
    private OrdersRepository ordersRepo;

    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    @Transactional(rollbackFor = OrderException.class)
    public void createNewOrder(PurchaseOrder po) throws OrderException {
        String orderId = UUID.randomUUID().toString().substring(0, 8);
        logger.info("[OrderService] Order Id >>> " + orderId);

        po.setOrderId(orderId);
        purchaseOrderRepo.insertPurchaseOrder(po);
        if(po.getLineItems().size() > 5)
            throw new OrderException("Maximum 5 items");
        lineItemRepo.addLineItems(po);
    }

    @Transactional(rollbackFor = OrderException.class)
    public void createNewOrder(Order order) throws OrderException {
        ordersRepo.insertOrder(order);
        int orderId = ordersRepo.getOrderId(order);
        order.setOrderId(orderId);
        if(order.getOrderDetails().size() > 5)
            throw new OrderException("Maximum 5 items");
        ordersRepo.addOrderDetails(order);
    }
}
