package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseProductStoreAssociationRepository;
import com.fulfilment.application.monolith.fulfillment.domain.usecases.AssociateWarehouseToProductInStoreUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;
import org.jboss.logging.Logger;

@Path("fulfillment")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FulfillmentResourceImpl {

  @Inject private AssociateWarehouseToProductInStoreUseCase associateUseCase;
  @Inject private WarehouseProductStoreAssociationRepository associationRepository;

  private static final Logger LOGGER = Logger.getLogger(FulfillmentResourceImpl.class.getName());

  @POST
  @Path("warehouse/{warehouseCode}/product/{productId}/store/{storeId}")
  @Transactional
  public Response associate(
      @PathParam("warehouseCode") String warehouseCode,
      @PathParam("productId") Long productId,
      @PathParam("storeId") Long storeId) {

    var association = associateUseCase.associate(warehouseCode, productId, storeId);

    return Response.status(201).entity(association).build();
  }

  @GET
  @Path("warehouse/{warehouseCode}")
  public List<Map<String, Object>> getAssociationsByWarehouse(
      @PathParam("warehouseCode") String warehouseCode) {
    return associationRepository.findByWarehouse(warehouseCode).stream()
        .map(
            a ->
                (Map<String, Object>) Map.of(
                    "id", a.id,
                    "warehouseBusinessUnitCode", a.warehouseBusinessUnitCode,
                    "productId", a.productId,
                    "storeId", a.storeId,
                    "createdAt", a.createdAt))
        .toList();
  }

  @GET
  @Path("store/{storeId}")
  public List<Map<String, Object>> getAssociationsByStore(@PathParam("storeId") Long storeId) {
    return associationRepository.findByStore(storeId).stream()
        .map(
            a ->
                (Map<String, Object>) Map.of(
                    "id", a.id,
                    "warehouseBusinessUnitCode", a.warehouseBusinessUnitCode,
                    "productId", a.productId,
                    "storeId", a.storeId,
                    "createdAt", a.createdAt))
        .toList();
  }

  @GET
  @Path("product/{productId}/store/{storeId}")
  public List<Map<String, Object>> getAssociationsByProductStore(
      @PathParam("productId") Long productId, @PathParam("storeId") Long storeId) {
    return associationRepository.findByProductAndStore(productId, storeId).stream()
        .map(
            a ->
                (Map<String, Object>) Map.of(
                    "id", a.id,
                    "warehouseBusinessUnitCode", a.warehouseBusinessUnitCode,
                    "productId", a.productId,
                    "storeId", a.storeId,
                    "createdAt", a.createdAt))
        .toList();
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Inject ObjectMapper objectMapper;

    @Override
    public Response toResponse(Exception exception) {
      LOGGER.error("Failed to handle request", exception);

      int code = 500;
      if (exception instanceof WebApplicationException) {
        code = ((WebApplicationException) exception).getResponse().getStatus();
      }

      ObjectNode exceptionJson = objectMapper.createObjectNode();
      exceptionJson.put("exceptionType", exception.getClass().getName());
      exceptionJson.put("code", code);

      if (exception.getMessage() != null) {
        exceptionJson.put("error", exception.getMessage());
      }

      return Response.status(code).entity(exceptionJson).build();
    }
  }
}
