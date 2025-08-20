package Order.Modal.Entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class orders implements Serializable {
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("table_id")
    private Integer table_id;
    
    @SerializedName("customer_id")
    private Integer customer_id;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("total_amount")
    private Integer total_amount;
    
    @SerializedName("createdAt")
    private String created_at;
    
    @SerializedName("updatedAt")
    private String updated_at;
    
    @SerializedName("items")
    private List<orders_items> items;
    
    public int calculateTotalAmount() {
        int total = 0;
        if (items != null) {
            for (orders_items item : items) {
                total += item.getSubtotal();
            }
        }
        return total;
    }
    
    public LocalDateTime getCreatedAt() {
        if (created_at != null && !created_at.isEmpty()) {
            try {
                return LocalDateTime.parse(created_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            } catch (Exception e) {
                // Fallback to current time if parsing fails
                return LocalDateTime.now();
            }
        }
        return LocalDateTime.now();
    }
    
    public LocalDateTime getUpdatedAt() {
        if (updated_at != null && !updated_at.isEmpty()) {
            try {
                return LocalDateTime.parse(updated_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            } catch (Exception e) {
                return getCreatedAt();
            }
        }
        return getCreatedAt();
    }
}
