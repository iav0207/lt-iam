package task.lt.resources;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dropwizard.jersey.params.BooleanParam;
import io.dropwizard.validation.Validated;
import org.hibernate.validator.constraints.NotEmpty;
import task.lt.api.model.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class UsersResource {

    @POST
    public Response add(@Validated User user) {
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response get(
            @PathParam("id") @NotEmpty String id,
            @QueryParam("full") @Nullable BooleanParam full)
    {
        return Response.ok(new User()).build();
    }

    @POST
    @Path("{id}")
    public Response update(
            @PathParam("id") @NotEmpty String id,
            @QueryParam("full") @Nullable BooleanParam full)
    {
        return Response.ok(new User()).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(
            @PathParam("id") @NotEmpty String id,
            @QueryParam("full") @Nullable BooleanParam full)
    {
        return Response.ok(new User()).build();
    }
}
