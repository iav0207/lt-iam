package task.lt.resources;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import task.lt.api.model.Organization;
import task.lt.api.req.IdAndFullParams;

@Path("/organizations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class OrganizationsResource {

    @POST
    public Response add(@Valid Organization organization) {
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response get(@Valid @BeanParam IdAndFullParams params) {
        return Response.ok().build();
    }

    @POST
    @Path("{id}")
    public Response update(@Valid @BeanParam IdAndFullParams params) {
        return Response.ok().build();
    }

}
