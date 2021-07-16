package com.bala;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/order")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @GET
    @Path("/getAll")
    public List<OrderData> getAll() {
        return OrderData.findAll().list();
    }

    @POST
    @Transactional
    @Path("/create")
    public Response create(OrderData order) {
        order.persist();
        return Response.ok(order).status(201).build();
    }

    @GET
    @Path("/getByOrderAmount")
    public List<OrderData> getByOrderAmount(@QueryParam("amount") BigDecimal amount) {
        return OrderData.find("orderAmount >= ?1", amount).list();
    }

    @GET
    @Path("/getByOrderLinePrice")
    public List<OrderData> getByOrderLinePrice(@QueryParam("price") BigDecimal price) {
        return OrderData.find("select o.id, ol.price from OrderData o join o.orderLines ol where ol.price >= ?1", price)
                .list();
    }

}