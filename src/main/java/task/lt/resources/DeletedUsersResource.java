package task.lt.resources;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dropwizard.jersey.params.BooleanParam;
import org.hibernate.validator.constraints.NotEmpty;
import task.lt.api.model.User;

@Path("/users.deleted")
@Produces(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class DeletedUsersResource {

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") @NotEmpty String id)
    {
        return Response.ok(new User()).build();
    }

    @POST
    @Path("{id}")
    public Response restore(
            @PathParam("id") @NotEmpty String id,
            @QueryParam("full") @Nullable BooleanParam full)
    {
        return Response.ok(new User()).build();
    }
}
