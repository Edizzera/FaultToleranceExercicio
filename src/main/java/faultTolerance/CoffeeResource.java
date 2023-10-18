package faultTolerance;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/circuit")
public class CoffeeResource {

    private Long counter = 0L;

    @Inject
    CoffeeRepositoryService coffeeRepository;

    Logger LOGGER = Logger.getLogger(CoffeeResource.class.getName());

    @GET
    @Path("/{id}/availability")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response availability(@PathParam("id") int id) {

        final Long invocationNumber = counter++;

        Coffee coffee = coffeeRepository.getCoffeeById(id);

        if (coffee == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            Integer availability = null;
            if (coffee != null) {
                availability = coffeeRepository.getAvailability(coffee);
            }

            if (availability != null) {
                LOGGER.log(Level.INFO, () -> "Sucesso: " + invocationNumber);
                return Response.ok(availability).build();
            } else {
                LOGGER.log(Level.SEVERE, () -> "Falha, coffee nulo:" + invocationNumber);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Coffee is null")
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .build();
            }
        } catch (RuntimeException e) {
            String message = String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage());
            LOGGER.log(Level.SEVERE, () -> "Falha:" + invocationNumber);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
    }

}