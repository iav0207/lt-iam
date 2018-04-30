package task.lt.resources;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import task.lt.api.model.User;
import task.lt.api.req.IdParamWithFullOption;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class UsersResource {

    @POST
    public Response add(@Valid @BeanParam IdParamWithFullOption params) {
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response get(@Valid @BeanParam IdParamWithFullOption params) {
        return Response.ok(new User()).build();
    }

    @POST
    @Path("{id}")
    public Response update(@Valid @BeanParam IdParamWithFullOption params) {
        return Response.ok(new User()).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@Valid @BeanParam IdParamWithFullOption params) {
        return Response.ok(new User()).build();
    }
}
