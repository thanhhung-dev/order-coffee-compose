package Order.Modal.forms;

import Order.Modal.Api.OrderAPI;
import Order.Modal.Entity.orders;
import Order.Modal.Response.ApiResponse;
import Order.Modal.System.Form;
import Order.Modal.utils.SystemForm;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SystemForm(name = "Sales Dashboard", description = "Daily sales summary and statistics", tags = {"dashboard", "sales", "analytics"})
public class FormSalesDashboard extends Form {
    
    private JLabel totalOrdersLabel;
    private JLabel totalRevenueLabel;
    private JLabel avgOrderValueLabel;
    private JLabel completedOrdersLabel;
    private JLabel pendingOrdersLabel;
    private JLabel cancelledOrdersLabel;
    
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public FormSalesDashboard() {
        init();
        loadDashboardData();
    }
    
    @Override
    public void formRefresh() {
        loadDashboardData();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));
        
        // Header
        add(createHeader());
        
        // Dashboard content
        add(createDashboardContent(), "grow");
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[grow][]"));
        
        JLabel title = new JLabel("Sales Dashboard - " + LocalDate.now().format(dateFormatter));
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +4");
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> formRefresh());
        
        panel.add(title);
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel createDashboardContent() {
        JPanel mainPanel = new JPanel(new MigLayout("fillx,wrap", "[fill,grow][fill,grow]", "[][][][]"));
        
        // Statistics Cards
        mainPanel.add(createStatCard("Total Orders", "0", Color.decode("#2196F3")), "");
        mainPanel.add(createStatCard("Total Revenue", "0 VND", Color.decode("#4CAF50")), "wrap");
        
        mainPanel.add(createStatCard("Average Order Value", "0 VND", Color.decode("#FF9800")), "");
        mainPanel.add(createStatCard("Completed Orders", "0", Color.decode("#8BC34A")), "wrap");
        
        mainPanel.add(createStatCard("Pending Orders", "0", Color.decode("#FFC107")), "");
        mainPanel.add(createStatCard("Cancelled Orders", "0", Color.decode("#F44336")), "wrap");
        
        // Order Status Chart Panel
        mainPanel.add(createOrderStatusPanel(), "span,grow");
        
        return mainPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new MigLayout("fillx", "[grow]", "[][]"));
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(color.brighter().brighter());
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +1");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setForeground(color.darker());
        
        // Store labels for updating
        switch (title) {
            case "Total Orders":
                totalOrdersLabel = valueLabel;
                break;
            case "Total Revenue":
                totalRevenueLabel = valueLabel;
                break;
            case "Average Order Value":
                avgOrderValueLabel = valueLabel;
                break;
            case "Completed Orders":
                completedOrdersLabel = valueLabel;
                break;
            case "Pending Orders":
                pendingOrdersLabel = valueLabel;
                break;
            case "Cancelled Orders":
                cancelledOrdersLabel = valueLabel;
                break;
        }
        
        card.add(titleLabel, "wrap");
        card.add(valueLabel, "wrap");
        
        return card;
    }
    
    private JPanel createOrderStatusPanel() {
        JPanel panel = new JPanel(new MigLayout("fill", "[fill]", "[][fill]"));
        panel.setBorder(new TitledBorder("Today's Order Summary"));
        
        JLabel infoLabel = new JLabel("Order breakdown by status");
        infoLabel.putClientProperty(FlatClientProperties.STYLE, "font:italic");
        
        // Simple text-based summary (could be enhanced with charts later)
        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setText("Loading order summary...");
        summaryArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(infoLabel, "wrap");
        panel.add(scrollPane, "grow");
        
        return panel;
    }
    
    private void loadDashboardData() {
        // Load all orders to calculate statistics
        OrderAPI.getInstance().getAllOrder().enqueue(new Callback<Order.Modal.Response.orders.OrderResponse>() {
            @Override
            public void onResponse(Call<Order.Modal.Response.orders.OrderResponse> call, 
                    Response<Order.Modal.Response.orders.OrderResponse> response) {
                SwingUtilities.invokeLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        updateDashboard(response.body().getData());
                    }
                });
            }
            
            @Override
            public void onFailure(Call<Order.Modal.Response.orders.OrderResponse> call, Throwable t) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(FormSalesDashboard.this,
                            "Failed to load dashboard data: " + t.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }
    
    private void updateDashboard(List<orders> allOrders) {
        if (allOrders == null || allOrders.isEmpty()) {
            return;
        }
        
        // Calculate statistics
        int totalOrders = allOrders.size();
        long totalRevenue = 0;
        int completedCount = 0;
        int pendingCount = 0;
        int inProgressCount = 0;
        int cancelledCount = 0;
        
        Map<String, Integer> statusCount = new HashMap<>();
        
        for (orders order : allOrders) {
            totalRevenue += order.getTotal_amount();
            
            String status = order.getStatus().toLowerCase();
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
            
            switch (status) {
                case "completed":
                    completedCount++;
                    break;
                case "pending":
                    pendingCount++;
                    break;
                case "in-progress":
                    inProgressCount++;
                    break;
                case "cancelled":
                    cancelledCount++;
                    break;
            }
        }
        
        long avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;
        
        // Update UI
        if (totalOrdersLabel != null) {
            totalOrdersLabel.setText(String.valueOf(totalOrders));
        }
        if (totalRevenueLabel != null) {
            totalRevenueLabel.setText(currencyFormatter.format(totalRevenue));
        }
        if (avgOrderValueLabel != null) {
            avgOrderValueLabel.setText(currencyFormatter.format(avgOrderValue));
        }
        if (completedOrdersLabel != null) {
            completedOrdersLabel.setText(String.valueOf(completedCount));
        }
        if (pendingOrdersLabel != null) {
            pendingOrdersLabel.setText(String.valueOf(pendingCount));
        }
        if (cancelledOrdersLabel != null) {
            cancelledOrdersLabel.setText(String.valueOf(cancelledCount));
        }
        
        // Update summary text
        updateSummaryText(statusCount, totalOrders, totalRevenue);
    }
    
    private void updateSummaryText(Map<String, Integer> statusCount, int totalOrders, long totalRevenue) {
        StringBuilder summary = new StringBuilder();
        summary.append("=== TODAY'S SUMMARY ===\n\n");
        summary.append(String.format("Total Orders: %d\n", totalOrders));
        summary.append(String.format("Total Revenue: %s\n\n", currencyFormatter.format(totalRevenue)));
        
        summary.append("Order Status Breakdown:\n");
        summary.append("─────────────────────────\n");
        
        statusCount.forEach((status, count) -> {
            double percentage = totalOrders > 0 ? (count * 100.0 / totalOrders) : 0;
            summary.append(String.format("%-12s: %3d orders (%5.1f%%)\n", 
                    capitalize(status), count, percentage));
        });
        
        summary.append("\n");
        summary.append("Last updated: " + java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        // Find the summary text area and update it
        updateSummaryTextArea(summary.toString());
    }
    
    private void updateSummaryTextArea(String text) {
        findAndUpdateTextArea(this, text);
    }
    
    private void findAndUpdateTextArea(Container container, String text) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextArea) {
                ((JTextArea) component).setText(text);
                return;
            } else if (component instanceof Container) {
                findAndUpdateTextArea((Container) component, text);
            } else if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                if (scrollPane.getViewport().getView() instanceof JTextArea) {
                    ((JTextArea) scrollPane.getViewport().getView()).setText(text);
                    return;
                }
            }
        }
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}