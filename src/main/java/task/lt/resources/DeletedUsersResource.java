package task.lt.resources;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotEmpty;
import task.lt.api.model.User;
import task.lt.api.req.IdParamWithFullOption;

@Path("/users.deleted")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class DeletedUsersResource {

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") @NotNull @NotEmpty String id) {
        return Response.ok(new User()).build();
    }

    @POST
    @Path("{id}")
    public Response restore(@Valid @BeanParam IdParamWithFullOption params) {
        return Response.ok(new User()).build();
    }
}