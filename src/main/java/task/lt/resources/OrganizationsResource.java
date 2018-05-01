package task.lt.resources;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import task.lt.api.model.Organization;
import task.lt.api.req.IdAndFullParams;
import task.lt.core.id.NoSuchIdException;
import task.lt.core.id.OrgHashIds;
import task.lt.db.OrganizationsDao;

import static task.lt.api.err.ApiErrors.notFound;
import static task.lt.api.err.ApiErrors.organizationAlreadyExists;
import static task.lt.api.resp.ApiResponse.created;
import static task.lt.api.resp.ApiResponse.ok;
import static task.lt.api.resp.ApiResponse.uri;

@Path(OrganizationsResource.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class OrganizationsResource {

    static final String PATH = "/organizations";

    private final OrgHashIds hashIds = new OrgHashIds();
    private final OrganizationsDao dao;

    public OrganizationsResource(OrganizationsDao dao) {
        this.dao = dao;
    }

    @POST
    public Response add(@Valid Organization organization) {
        if (dao.exists(organization.getName())) {
            return organizationAlreadyExists();
        }
        long id = dao.add(organization.getName(), organization.getType().toString());
        Organization createdOrg = dao.getById(id);
        return created(uri(PATH + "/" + createdOrg.getId()), createdOrg);
    }

    @GET
    @Path("{id}")
    public Response get(@Valid @BeanParam IdAndFullParams params) {
        final long id;
        try {
            id = hashIds.decode(params.getId());
        } catch (NoSuchIdException ex) {
            return notFound();
        }
        if (!dao.exists(id)) {
            return notFound();
        }
        if (params.isFull()) {
            // TODO full means include employees, teams, relations
        }
        return ok(dao.getById(id));
    }

    @POST
    @Path("{id}")
    public Response update(@Valid @BeanParam IdAndFullParams params) {
        return Response.ok().build();
    }

}
