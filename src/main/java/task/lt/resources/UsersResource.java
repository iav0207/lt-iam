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
import task.lt.api.model.EmploymentItem;
import task.lt.api.model.User;
import task.lt.api.model.UserWithPassword;
import task.lt.api.req.IdParamWithFullOption;
import task.lt.api.req.UpdateUserRequest;
import task.lt.api.resp.ApiResponse;
import task.lt.core.id.UsersHashIds;
import task.lt.core.pass.PasswordDigest;
import task.lt.db.EmploymentDao;
import task.lt.db.OrganizationsDao;
import task.lt.db.UsersDao;

import static task.lt.api.err.ApiErrors.notFound;
import static task.lt.api.err.ApiErrors.userWithSuchEmailAlreadyExists;
import static task.lt.api.resp.ApiResponse.created;
import static task.lt.api.resp.ApiResponse.noContent;
import static task.lt.api.resp.ApiResponse.ok;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class UsersResource {

    private final UsersHashIds usersIds = new UsersHashIds();
    private final OrganizationsDao organizations;
    private final EmploymentDao employmentDao;
    private final UsersDao dao;

    public UsersResource(UsersDao dao, OrganizationsDao organizations, EmploymentDao employmentDao) {
        this.dao = dao;
        this.organizations = organizations;
        this.employmentDao = employmentDao;
    }

    @POST
    public Response add(@Valid final UserWithPassword user) {
        if (dao.exists(user.getEmail())) {
            return userWithSuchEmailAlreadyExists();
        }
        long id = dao.add(user, PasswordDigest.hash(user.getPassword()));
        user.getEmployment().forEach(e -> addEmployment(id, e));
        User createdUser = dao.getById(id);
        return created(uri(createdUser.getId()), createdUser);
    }

    private void addEmployment(long userId, EmploymentItem emp) {
        String orgName = emp.getOrganization().getName();
        long orgId = organizations.getIdByName(orgName)
                .orElseGet(() -> organizations.add(orgName, emp.getOrganization().getType().toString()));
        employmentDao.add(userId, orgId, emp.getJobTitle());
    }

    @GET
    @Path("{id}")
    public Response get(@Valid @BeanParam IdParamWithFullOption params) {
        return usersIds.decode(params.getId())
                .filter(dao::existsAndActive)
                .map(dao::getById)
                // TODO full?
                .map(ApiResponse::ok)
                .orElseGet(ApiErrors::notFound);
    }

    @POST
    @Path("{id}")
    public Response update(
            @PathParam("id") @NotNull @NotEmpty String hashId,
            UpdateUserRequest update)
    {
        Optional<Long> id = usersIds.decode(hashId).filter(dao::exists);
        if (!id.isPresent()) {
            return notFound();
        }
        if (!update.isEmpty()) {
            dao.update(id.get(),
                    User.builder()
                            .withFirstName(update.getFirstName())
                            .withLastName(update.getLastName())
                            .withGender(update.getGender())
                            .build(),
                    Optional.ofNullable(update.getPassword())
                            .map(PasswordDigest::hash)
                            .orElse(null));
        }
        return ok(dao.getById(id.get()));
    }

    @DELETE
    @Path("{id}")
    public Response delete(@Valid @BeanParam IdParamWithFullOption params) {
        return usersIds.decode(params.getId())
                .filter(dao::existsAndActive)
                .map(dao::delete)
                .map(upd -> noContent())
                .orElseGet(ApiErrors::notFound);
    }

    private static URI uri(String path) {
        return UriBuilder.fromResource(UsersResource.class).path(path).build();
    }
}
