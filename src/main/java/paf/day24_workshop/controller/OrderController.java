package paf.day24_workshop.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import paf.day24_workshop.exception.OrderException;
import paf.day24_workshop.models.Order;
import paf.day24_workshop.models.OrderDetails;
import paf.day24_workshop.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderSvc;
    
    private static final Logger logger = Logger.getLogger(OrderController.class.getName());

    @GetMapping()
    public String getOrder() {
        return "order_template";
    }
    
    @PostMapping("/cart")
    public String postOrder(@RequestBody MultiValueMap<String, String> form, 
        Model model, HttpSession sess) {
        List<OrderDetails> ods = (List<OrderDetails>) sess.getAttribute("order");

        if(null == ods) {
            ods = new LinkedList<>();
            sess.setAttribute("order", ods);
        }

        String product = form.getFirst("product");
        float unitPrice = Float.parseFloat(form.getFirst("unit_price"));
        float discount = Float.parseFloat(form.getFirst("discount"));
        Integer qty = Integer.parseInt(form.getFirst("quantity"));
        ods.add(new OrderDetails(product, unitPrice, discount, qty));
        Order order = new Order();
        order.setCustomerName(form.getFirst("customer_name"));
        order.setOrderDetails(ods);
        for(OrderDetails od : ods) {
            logger.info("Product: %s, Unit Price: %.2f, Discount: %.2f, Quantity: %d"
                .formatted(od.getProduct(), od.getUnitPrice(), od.getDiscount(), od.getQuantity()));
        }

        sess.setAttribute("checkoutOrder", order);
        model.addAttribute("ods", ods);
        return "order_template";
    }

    @PostMapping("/customer_checkout")
    public String getCheckout() {
        return "customer_checkout";
    }

    @PostMapping("/checkout")
    public String postCheckout(Model model, @RequestBody MultiValueMap<String, String> form, HttpSession sess) throws OrderException {
        List<OrderDetails> ods = (List<OrderDetails>) sess.getAttribute("order");
        String shipAddress = form.getFirst("ship_address");
        String notes = form.getFirst("notes");
        Float tax = Float.parseFloat(form.getFirst("tax"));
        Order order = (Order) sess.getAttribute("checkoutOrder");
        order.setShipAddress(shipAddress);
        order.setNotes(notes);
        order.setTax(tax);
        orderSvc.createNewOrder(order);
        model.addAttribute("total", ods.size());
        sess.invalidate();
        return "checkout";
    }
}
