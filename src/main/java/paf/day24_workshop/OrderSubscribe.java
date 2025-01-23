package paf.day24_workshop;

import java.io.StringReader;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import paf.day24_workshop.exception.OrderException;
import paf.day24_workshop.models.Order;
import paf.day24_workshop.service.OrderService;

@Component
public class OrderSubscribe implements MessageListener {

    @Autowired
    private OrderService orderSvc;

    private static final Logger logger = Logger.getLogger(OrderSubscribe.class.getName());

    @Override
    public void onMessage(@NonNull Message message, @Nullable byte[] pattern) {
        String txt = new String(message.getBody());
        logger.info("[Sub] Message received: " + txt);
        JsonReader reader = Json.createReader(new StringReader(txt));
        Order order = Order.JsonToOrder(reader.readObject());
        try {
            orderSvc.createNewOrder(order);
            logger.info("[Sub] New order created: " + order);
        } catch (OrderException ex) {
            ex.printStackTrace();
        }
    }
    
}
