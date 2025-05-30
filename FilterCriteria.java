package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;
import java.util.Date;

public class FilterCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date startDate;
    private Date endDate;
    private Double minAmount;
    private Integer productId;

    public FilterCriteria(Date startDate, Date endDate, Double minAmount, Integer productId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.minAmount = minAmount;
        this.productId = productId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public Integer getProductId() {
        return productId;
    }
}