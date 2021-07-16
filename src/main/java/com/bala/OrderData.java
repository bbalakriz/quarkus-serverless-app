package com.bala;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * OrderData
 */
@Entity
@Table(name = "order_data")
public class OrderData extends PanacheEntity {

    public BigDecimal orderAmount;
    public Date createdTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    public List<OrderLineItem> orderLines;
}