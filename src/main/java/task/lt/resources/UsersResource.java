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

import io.dropwizard.jersey.params.BooleanParam;
import org.hibernate.validator.constraints.NotEmpty;
import task.lt.api.resp.DeleteUserResponse;
import task.lt.api.resp.GetUserResponse;
import task.lt.api.resp.UpdateUserResponse;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class UsersResource {

    @GET
    @Path("{id}")
    public GetUserResponse get(
            @PathParam("id") @NotEmpty String id,
            @QueryParam("full") @Nullable BooleanParam full)
    {
        return new GetUserResponse();
    }

    @POST
    @Path("{id}")
    public UpdateUserResponse update(
            @PathParam("id") @NotEmpty String id,
            @QueryParam("full") @Nullable BooleanParam full)
    {
        return new UpdateUserResponse();
    }

    @DELETE
    @Path("{id}")
    public DeleteUserResponse delete(
            @PathParam("id") @NotEmpty String id,
            @QueryParam("full") @Nullable BooleanParam full)
    {
        return new DeleteUserResponse();
    }
}
