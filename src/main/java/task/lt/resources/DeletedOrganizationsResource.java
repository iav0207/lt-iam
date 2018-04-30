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
import task.lt.api.req.IdParamWithFullOption;

@Path("/organizations.deleted")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class DeletedOrganizationsResource {

    @GET
    public Response getAll() {
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") @NotNull @NotEmpty String id) {
        return Response.ok().build();
    }

    @POST
    @Path("{id}")
    public Response restore(@Valid @BeanParam IdParamWithFullOption params) {
        return Response.ok().build();
    }

}
