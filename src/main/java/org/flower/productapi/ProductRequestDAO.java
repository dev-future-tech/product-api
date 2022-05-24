package org.flower.productapi;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "product_requests", schema = "public")
public class ProductRequestDAO {

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "brand")
    private String productBrand;

    @Column(name = "size")
    private String productSize;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "requested_by")
    private String requestedBy;

    @Column(name = "requested_on")
    private Timestamp requestedOn;


    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Timestamp getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(Timestamp requestedOn) {
        this.requestedOn = requestedOn;
    }
}
