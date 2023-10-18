package faultTolerance;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.resteasy.specimpl.ResponseBuilderImpl;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class FaultTolerance {

    private static final String FALL_BACK_MESSAGE = "FallbackMethod: ";

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    @Retry(maxRetries = 3, delay = 1000)
    @Fallback(fallbackMethod = "recover")
    @Timeout(7000)
    public String getName(@PathParam("name") String name) {    

        if (name.equalsIgnoreCase("error")) {
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status(Response.Status.INTERNAL_SERVER_ERROR);
            builder.entity("The requested was an error");
            Response response = builder.build();
            throw new WebApplicationException(response);
        }

        return name;
    }


    public String recover(String name) {
        return FALL_BACK_MESSAGE + name;
    }

}
