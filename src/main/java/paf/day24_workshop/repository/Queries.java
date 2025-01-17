package paf.day24_workshop.repository;

public class Queries {

        public static String SQL_INSERT_PURCHASE_ORDER = """
                        INSERT INTO purchase_order (order_id, name, order_date)
                        VALUES (?, ?, SYSDATE())
                        """;

        public static String SQL_INSERT_LINE_ITEM = """
                        INSERT INTO line_item (description, quantity, order_id)
                        VALUES (?, ?, ?)
                        """;

        public static String SQL_INSERT_ORDER = """
                        INSERT INTO orders (order_date, customer_name, ship_address, notes, tax)
                        VALUES (SYSDATE(), ?, ?, ?, ?)
                        """;

        public static String SQL_INSERT_ORDER_DETAILS = """
                        INSERT INTO order_details (product, unit_price, discount, quantity, order_id)
                        VALUES (?, ?, ?, ?, ?)
                        """;

        public static String SQL_GET_ORDER_ID = """
                        SELECT order_id FROM orders WHERE customer_name = ? AND ship_address = ?
                        """;

}
