package task.lt.resources;

import java.net.URI;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.validator.constraints.NotEmpty;
import task.lt.api.err.ApiErrors;
import task.lt.api.model.Organization;
import task.lt.api.req.IdAndFullParams;
import task.lt.api.resp.ApiResponse;
import task.lt.core.id.OrgHashIds;
import task.lt.db.OrganizationsDao;

import static task.lt.api.err.ApiErrors.notFound;
import static task.lt.api.err.ApiErrors.suchOrganizationAlreadyExists;
import static task.lt.api.resp.ApiResponse.created;
import static task.lt.api.resp.ApiResponse.noContent;
import static task.lt.api.resp.ApiResponse.ok;

@Path("/organizations")
@Consumes(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class OrganizationsResource {

    private final OrgHashIds hashIds = new OrgHashIds();
    private final OrganizationsDao dao;

    public OrganizationsResource(OrganizationsDao dao) {
        this.dao = dao;
    }

    @POST
    public Response add(@Valid Organization organization) {
        if (dao.exists(organization.getName())) {
            return suchOrganizationAlreadyExists();
        }
        long id = dao.add(organization.getName(), organization.getType().toString());
        Organization createdOrg = dao.getById(id);
        return created(uri(createdOrg.getId()), createdOrg);
    }

    @GET
    @Path("{id}")
    public Response get(@Valid @BeanParam IdAndFullParams params) {
        return hashIds.decode(params.getId())
                .filter(dao::existsAndActive)
                .map(dao::getById)
                // TODO full means include employees, teams, relations
                .map(ApiResponse::ok)
                .orElseGet(ApiErrors::notFound);
    }

    @POST
    @Path("{id}")
    public Response update(
            @PathParam("id") @NotNull @NotEmpty String hashId,
            Organization update)
    {
        Optional<Long> id = hashIds.decode(hashId).filter(dao::exists);
        if (!id.isPresent()) {
            return notFound();
        }
        String nameUpdate = update.getName();
        if (nameUpdate != null) {
            String oldName = dao.getName(id.get());
            if (!nameUpdate.equals(oldName) && dao.exists(update.getName())) {
                return suchOrganizationAlreadyExists();
            }
            dao.update(id.get(), update.getName());
        }
        return ok(dao.getById(id.get()));
    }

    @DELETE
    @Path("{id}")
    public Response delete(@Valid @BeanParam IdAndFullParams params) {
        return hashIds.decode(params.getId())
                .filter(dao::existsAndActive)
                .map(dao::delete)
                .map(upd -> noContent())
                .orElseGet(ApiErrors::notFound);
    }

    private static URI uri(String path) {
        return UriBuilder.fromResource(OrganizationsResource.class).path(path).build();
    }

}
