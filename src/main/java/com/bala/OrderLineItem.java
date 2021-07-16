package com.bala;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * OrderLineItem
 */
@Entity
@Table(name = "order_lineitem")
public class OrderLineItem extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "order_line_seq", sequenceName = "order_line_seq", allocationSize = 1)
    @GeneratedValue(generator = "order_line_seq", strategy = GenerationType.AUTO)
    private Long id;
    public String item;
    public BigDecimal price;
    public BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
    private OrderData parent;
}