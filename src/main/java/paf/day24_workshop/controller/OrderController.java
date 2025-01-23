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

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;
import paf.day24_workshop.exception.OrderException;
import paf.day24_workshop.models.Order;
import paf.day24_workshop.models.OrderDetails;
import paf.day24_workshop.service.RedisService;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisService redisSvc;
    
    private static final Logger logger = Logger.getLogger(OrderController.class.getName());

    @GetMapping()
    public String getOrder(Model model) {
        model.addAttribute("regList", redisSvc.getRegistrations());
        return "order_template";
    }
    
    @PostMapping("/cart")
    public String postOrder(@RequestBody MultiValueMap<String, String> form, 
        Model model, HttpSession sess) throws OrderException{
        List<OrderDetails> ods = (List<OrderDetails>) sess.getAttribute("order");

        if(null == ods) {
            ods = new LinkedList<>();
            sess.setAttribute("order", ods);
        }

        ods.add(new OrderDetails(form.getFirst("product"), Float.parseFloat(form.getFirst("unit_price")), 
            Float.parseFloat(form.getFirst("discount")), Integer.parseInt(form.getFirst("quantity"))));
        
        Order order = new Order();
        order.setCustomerName(form.getFirst("customer_name"));
        order.setShipAddress(form.getFirst("ship_address"));
        order.setNotes(form.getFirst("notes"));
        order.setTax(Float.parseFloat(form.getFirst("tax")));
        order.setOrderDetails(ods);

        sess.setAttribute("checkoutOrder", order);
        model.addAttribute("ods", ods);
        return "order_template";
    }

    @PostMapping("/checkout")
    public String postCheckout(Model model, HttpSession sess) throws OrderException {
        List<OrderDetails> ods = (List<OrderDetails>) sess.getAttribute("order");
        Order order = (Order) sess.getAttribute("checkoutOrder");
        
        JsonArrayBuilder liArr = Json.createArrayBuilder();
        for(OrderDetails od : ods) {
            liArr.add(Json.createObjectBuilder()
                .add("product", od.getProduct())
                .add("unit_price", od.getUnitPrice())
                .add("discount", od.getDiscount())
                .add("quantity", od.getQuantity())
                .build());
            logger.info("Product: %s, Unit Price: %.2f, Discount: %.2f, Quantity: %d"
                .formatted(od.getProduct(), od.getUnitPrice(), od.getDiscount(), od.getQuantity()));
        }

        // Json Object
        JsonObject obj = Json.createObjectBuilder()
            .add("customer_name", order.getCustomerName())
            .add("ship_address", order.getShipAddress())
            .add("notes", order.getNotes())
            .add("tax", order.getTax())
            .add("line_items", liArr.build())
            .build();
            
        redisSvc.insertJson(order.getCustomerName(), obj);
        model.addAttribute("total", ods.size());
        sess.invalidate();
        return "checkout";
    }
}
