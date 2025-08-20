-- Sample Data for Order Coffee Management System

-- Insert sample categories
INSERT IGNORE INTO categories (id, name) VALUES 
(1, 'Coffee'),
(2, 'Tea'),
(3, 'Pastries'),
(4, 'Cold Drinks'),
(5, 'Sandwiches');

-- Insert sample products
INSERT IGNORE INTO products (id, name, description, price, image, status, category_id) VALUES 
-- Coffee products
(1, 'Americano', 'Classic black coffee', 3500, 'americano.jpg', 1, 1),
(2, 'Cappuccino', 'Coffee with steamed milk foam', 4500, 'cappuccino.jpg', 1, 1),
(3, 'Latte', 'Coffee with steamed milk', 4500, 'latte.jpg', 1, 1),
(4, 'Espresso', 'Strong black coffee', 3000, 'espresso.jpg', 1, 1),
(5, 'Mocha', 'Coffee with chocolate', 5000, 'mocha.jpg', 1, 1),

-- Tea products
(6, 'Green Tea', 'Fresh green tea', 3000, 'green_tea.jpg', 1, 2),
(7, 'Earl Grey', 'Black tea with bergamot', 3500, 'earl_grey.jpg', 1, 2),
(8, 'Chamomile Tea', 'Herbal chamomile tea', 3500, 'chamomile.jpg', 1, 2),

-- Cold drinks
(9, 'Iced Coffee', 'Cold brewed coffee', 4000, 'iced_coffee.jpg', 1, 4),
(10, 'Lemonade', 'Fresh lemonade', 3500, 'lemonade.jpg', 1, 4),
(11, 'Orange Juice', 'Fresh orange juice', 4000, 'orange_juice.jpg', 1, 4),

-- Pastries
(12, 'Croissant', 'Buttery croissant', 2500, 'croissant.jpg', 1, 3),
(13, 'Muffin', 'Blueberry muffin', 3000, 'muffin.jpg', 1, 3),
(14, 'Donut', 'Glazed donut', 2000, 'donut.jpg', 1, 3),

-- Sandwiches
(15, 'Club Sandwich', 'Classic club sandwich', 6500, 'club_sandwich.jpg', 1, 5),
(16, 'BLT Sandwich', 'Bacon lettuce tomato', 5500, 'blt_sandwich.jpg', 1, 5);

-- Insert sample tables
INSERT IGNORE INTO tables (id, status) VALUES 
(1, 'available'),
(2, 'available'),
(3, 'occupied'),
(4, 'available'),
(5, 'available'),
(6, 'occupied'),
(7, 'available'),
(8, 'available'),
(9, 'reserved'),
(10, 'available');

-- Insert sample customers
INSERT IGNORE INTO customers (id, name, email, phone) VALUES 
(1, 'John Doe', 'john.doe@email.com', '+1234567890'),
(2, 'Jane Smith', 'jane.smith@email.com', '+1234567891'),
(3, 'Mike Johnson', 'mike.johnson@email.com', '+1234567892'),
(4, 'Sarah Wilson', 'sarah.wilson@email.com', '+1234567893'),
(5, 'David Brown', 'david.brown@email.com', '+1234567894');

-- Insert sample orders
INSERT IGNORE INTO orders (id, table_id, customer_id, status, total_amount, deleted) VALUES 
(1, 1, 1, 'pending', 8000, 0),
(2, 3, 2, 'in-progress', 12500, 0),
(3, 6, 3, 'completed', 6500, 0),
(4, 2, 4, 'pending', 9500, 0),
(5, 5, 5, 'cancelled', 4500, 0);

-- Insert sample order items
INSERT IGNORE INTO orders_items (id, order_id, product_id, quantity, subtotal, status, notes) VALUES 
-- Order 1 items
(1, 1, 1, 1, 3500, 1, 'Extra hot'),
(2, 1, 12, 1, 2500, 1, ''),
(3, 1, 13, 1, 2000, 1, 'No nuts'),

-- Order 2 items  
(4, 2, 2, 2, 9000, 1, ''),
(5, 2, 15, 1, 6500, 1, 'No mayo'),

-- Order 3 items
(6, 3, 3, 1, 4500, 1, ''),
(7, 3, 14, 1, 2000, 1, ''),

-- Order 4 items
(8, 4, 5, 1, 5000, 1, 'Extra chocolate'),
(9, 4, 11, 1, 4500, 1, ''),

-- Order 5 items (cancelled)
(10, 5, 6, 1, 3000, 1, ''),
(11, 5, 12, 1, 1500, 1, '');