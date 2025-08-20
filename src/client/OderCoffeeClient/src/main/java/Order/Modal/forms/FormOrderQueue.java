package Order.Modal.forms;

import Order.Modal.Api.OrderAPI;
import Order.Modal.Entity.orders;
import Order.Modal.Response.ApiResponse;
import Order.Modal.Response.orders.OrderResponse;
import Order.Modal.System.Form;
import Order.Modal.utils.SystemForm;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@SystemForm(name = "Order Queue", description = "Real-time order queue for baristas", tags = {"barista", "orders", "queue"})
public class FormOrderQueue extends Form {
    
    private JTable pendingOrdersTable;
    private JTable inProgressOrdersTable;
    private DefaultTableModel pendingModel;
    private DefaultTableModel inProgressModel;
    private Timer refreshTimer;
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public FormOrderQueue() {
        init();
        setupAutoRefresh();
        loadPendingOrders();
        loadInProgressOrders();
    }
    
    @Override
    public void formRefresh() {
        loadPendingOrders();
        loadInProgressOrders();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow 50][fill,grow 50]"));
        
        // Header
        add(createHeader());
        
        // Pending Orders Panel
        add(createPendingOrdersPanel(), "height 300");
        
        // In-Progress Orders Panel
        add(createInProgressOrdersPanel(), "height 300");
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[grow][]"));
        
        JLabel title = new JLabel("Order Queue - Barista Interface");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +4");
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setIcon(new FlatSVGIcon("Order/icon/refresh.svg", 16, 16));
        refreshBtn.addActionListener(e -> formRefresh());
        
        panel.add(title);
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel createPendingOrdersPanel() {
        JPanel panel = new JPanel(new MigLayout("fill", "[fill]", "[][fill]"));
        panel.setBorder(new TitledBorder("Pending Orders"));
        
        // Create table model
        String[] columns = {"Order #", "Table", "Customer", "Items", "Total", "Time", "Action"};
        pendingModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only action column is editable
            }
        };
        
        pendingOrdersTable = new JTable(pendingModel);
        setupTable(pendingOrdersTable);
        
        // Add action buttons to the action column
        pendingOrdersTable.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        pendingOrdersTable.getColumn("Action").setCellEditor(new ActionCellEditor("Start"));
        
        JScrollPane scrollPane = new JScrollPane(pendingOrdersTable);
        panel.add(new JLabel("Orders waiting to be processed"), "wrap");
        panel.add(scrollPane, "grow");
        
        return panel;
    }
    
    private JPanel createInProgressOrdersPanel() {
        JPanel panel = new JPanel(new MigLayout("fill", "[fill]", "[][fill]"));
        panel.setBorder(new TitledBorder("In-Progress Orders"));
        
        // Create table model
        String[] columns = {"Order #", "Table", "Customer", "Items", "Total", "Started", "Action"};
        inProgressModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only action column is editable
            }
        };
        
        inProgressOrdersTable = new JTable(inProgressModel);
        setupTable(inProgressOrdersTable);
        
        // Add action buttons to the action column
        inProgressOrdersTable.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        inProgressOrdersTable.getColumn("Action").setCellEditor(new ActionCellEditor("Complete"));
        
        JScrollPane scrollPane = new JScrollPane(inProgressOrdersTable);
        panel.add(new JLabel("Orders currently being prepared"), "wrap");
        panel.add(scrollPane, "grow");
        
        return panel;
    }
    
    private void setupTable(JTable table) {
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Style the table
        table.putClientProperty(FlatClientProperties.STYLE, "" +
                "rowHeight:40;" +
                "showHorizontalLines:true;" +
                "intercellSpacing:0,1;" +
                "cellFocusColor:$TableHeader.hoverBackground;" +
                "selectionBackground:$TableHeader.hoverBackground;");
        
        // Set column widths
        if (table.getColumnCount() >= 7) {
            table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Order #
            table.getColumnModel().getColumn(1).setPreferredWidth(60);  // Table
            table.getColumnModel().getColumn(2).setPreferredWidth(120); // Customer
            table.getColumnModel().getColumn(3).setPreferredWidth(60);  // Items
            table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Total
            table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Time
            table.getColumnModel().getColumn(6).setPreferredWidth(100); // Action
        }
    }
    
    private void setupAutoRefresh() {
        // Auto-refresh every 30 seconds
        refreshTimer = new Timer(30000, e -> {
            loadPendingOrders();
            loadInProgressOrders();
        });
        refreshTimer.start();
    }
    
    private void loadPendingOrders() {
        OrderAPI.getInstance().getPendingOrders().enqueue(new Callback<ApiResponse<List<orders>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<orders>>> call, Response<ApiResponse<List<orders>>> response) {
                SwingUtilities.invokeLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        updatePendingOrdersTable(response.body().getData());
                    }
                });
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<orders>>> call, Throwable t) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormOrderQueue.this,
                            "Failed to load pending orders: " + t.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }
    
    private void loadInProgressOrders() {
        OrderAPI.getInstance().getInProgressOrders().enqueue(new Callback<ApiResponse<List<orders>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<orders>>> call, Response<ApiResponse<List<orders>>> response) {
                SwingUtilities.invokeLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        updateInProgressOrdersTable(response.body().getData());
                    }
                });
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<orders>>> call, Throwable t) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormOrderQueue.this,
                            "Failed to load in-progress orders: " + t.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }
    
    private void updatePendingOrdersTable(List<orders> orders) {
        pendingModel.setRowCount(0);
        for (orders order : orders) {
            Object[] row = {
                    "#" + order.getId(),
                    "Table " + order.getTable_id(),
                    order.getCustomer_id() != null ? "Customer " + order.getCustomer_id() : "Walk-in",
                    order.getItems() != null ? order.getItems().size() + " items" : "0 items",
                    currencyFormatter.format(order.getTotal_amount()),
                    order.getCreatedAt().format(timeFormatter),
                    "Action"
            };
            pendingModel.addRow(row);
        }
    }
    
    private void updateInProgressOrdersTable(List<orders> orders) {
        inProgressModel.setRowCount(0);
        for (orders order : orders) {
            Object[] row = {
                    "#" + order.getId(),
                    "Table " + order.getTable_id(),
                    order.getCustomer_id() != null ? "Customer " + order.getCustomer_id() : "Walk-in",
                    order.getItems() != null ? order.getItems().size() + " items" : "0 items",
                    currencyFormatter.format(order.getTotal_amount()),
                    order.getUpdatedAt() != null ? order.getUpdatedAt().format(timeFormatter) : 
                            order.getCreatedAt().format(timeFormatter),
                    "Action"
            };
            inProgressModel.addRow(row);
        }
    }
    
    private void updateOrderStatus(int orderId, String newStatus) {
        OrderAPI.getInstance().updateOrderStatus(orderId, newStatus).enqueue(new Callback<ApiResponse<orders>>() {
            @Override
            public void onResponse(Call<ApiResponse<orders>> call, Response<ApiResponse<orders>> response) {
                SwingUtilities.invokeLater(() -> {
                    if (response.isSuccessful()) {
                        JOptionPane.showMessageDialog(FormOrderQueue.this,
                                "Order status updated successfully!", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        formRefresh();
                    } else {
                        JOptionPane.showMessageDialog(FormOrderQueue.this,
                                "Failed to update order status", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
            
            @Override
            public void onFailure(Call<ApiResponse<orders>> call, Throwable t) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormOrderQueue.this,
                            "Failed to update order: " + t.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }
    
    // Custom cell renderer for action buttons
    private class ActionCellRenderer extends DefaultTableCellRenderer {
        private JButton button = new JButton();
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            if (table == pendingOrdersTable) {
                button.setText("Start Order");
                button.setBackground(new Color(76, 175, 80));
            } else {
                button.setText("Complete");
                button.setBackground(new Color(33, 150, 243));
            }
            
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorderPainted(false);
            
            return button;
        }
    }
    
    // Custom cell editor for action buttons
    private class ActionCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JButton button = new JButton();
        private String actionType;
        private int currentRow;
        private JTable currentTable;
        
        public ActionCellEditor(String actionType) {
            this.actionType = actionType;
            
            button.addActionListener(e -> {
                int orderId = getOrderIdFromRow(currentTable, currentRow);
                if (orderId != -1) {
                    String newStatus = actionType.equals("Start") ? "in-progress" : "completed";
                    updateOrderStatus(orderId, newStatus);
                }
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                int row, int column) {
            this.currentRow = row;
            this.currentTable = table;
            
            button.setText(actionType.equals("Start") ? "Start Order" : "Complete");
            button.setBackground(actionType.equals("Start") ? 
                    new Color(76, 175, 80) : new Color(33, 150, 243));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorderPainted(false);
            
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            return actionType;
        }
        
        private int getOrderIdFromRow(JTable table, int row) {
            try {
                String orderText = (String) table.getValueAt(row, 0);
                return Integer.parseInt(orderText.substring(1)); // Remove '#' prefix
            } catch (Exception e) {
                return -1;
            }
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.finalize();
    }
}