package paf.day24_workshop.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import paf.day24_workshop.models.LineItem;
import paf.day24_workshop.models.PurchaseOrder;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = Logger.getLogger(CartController.class.getName());

    @PostMapping
    public String postCart(@RequestBody MultiValueMap<String, String> form, 
        Model model, HttpSession sess) {
        List<LineItem> lineItems = (List<LineItem>) sess.getAttribute("cart");

        if(null == lineItems) {
            lineItems = new LinkedList<>();
            sess.setAttribute("cart", lineItems);
        }

        String item = form.getFirst("item");
        Integer qty = Integer.parseInt(form.getFirst("quantity"));
        lineItems.add(new LineItem(item, qty));
        PurchaseOrder po = new PurchaseOrder();
        po.setName(form.getFirst("name"));
        po.setLineItems(lineItems);
        for(LineItem li : lineItems) {
            logger.info("Item: %s, Quantity: %d".formatted(li.getDescription(), li.getQuantity()));
        }

        sess.setAttribute("checkoutCart", po);
        model.addAttribute("lineItems", lineItems);
        return "cart_template";
    }

}
