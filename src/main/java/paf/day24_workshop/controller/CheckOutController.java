package paf.day24_workshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import paf.day24_workshop.exception.OrderException;
import paf.day24_workshop.models.LineItem;
import paf.day24_workshop.models.PurchaseOrder;
import paf.day24_workshop.service.OrderService;

@Controller
@RequestMapping("/checkout")
public class CheckOutController {

    @Autowired
    private OrderService orderSvc;

    @PostMapping
    public String postCheckout(Model model, HttpSession sess) throws OrderException {
        List<LineItem> lineItems = (List<LineItem>) sess.getAttribute("cart");
        PurchaseOrder po = (PurchaseOrder) sess.getAttribute("checkoutCart");
        orderSvc.createNewOrder(po);
        sess.invalidate();
        model.addAttribute("total", lineItems.size());
        return "checkout";
    }
    
}
