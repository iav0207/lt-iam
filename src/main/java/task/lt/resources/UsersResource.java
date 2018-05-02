package task.lt.resources;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import io.dropwizard.jersey.params.BooleanParam;
import org.apache.commons.lang3.BooleanUtils;
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
        long orgId = Optional.ofNullable(organizations.getIdByName(orgName))
                .orElseGet(() -> organizations.add(orgName, emp.getOrganization().getType().toString()));
        employmentDao.add(userId, orgId, emp.getJobTitle());
    }

    @GET
    @Path("{id}")
    public Response get(@Valid @BeanParam IdParamWithFullOption params) {
        return usersIds.decode(params.getId())
                .filter(dao::existsAndActive)
                .map(dao::getById)
                .map(enhanceToFullIfNeeded(params.getFull()))
                .map(ApiResponse::ok)
                .orElseGet(ApiErrors::notFound);
    }

    @POST
    @Path("{id}")
    public Response update(
            @Valid @BeanParam IdParamWithFullOption params,
            UpdateUserRequest update)
    {
        Optional<Long> id = usersIds.decode(params.getId()).filter(dao::exists);
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
        return Optional.of(dao.getById(id.get()))
                .map(enhanceToFullIfNeeded(params.getFull()))
                .map(ApiResponse::ok)
                .orElseThrow(IllegalStateException::new);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@Valid @BeanParam IdParamWithFullOption params) {
        Optional<Long> id = usersIds.decode(params.getId()).filter(dao::existsAndActive);
        if (!id.isPresent()) {
            return notFound();
        }
        Optional<User> user = Optional.ofNullable(params.getFull())
                .filter(UsersResource::needFull)
                .map(f -> dao.getById(id.get()))
                .map(enhanceToFullIfNeeded(params.getFull()));
        dao.delete(id.get());
        return user.map(ApiResponse::ok).orElseGet(ApiResponse::noContent);
    }

    /**
     * Enrich user entity with employment info if {@code full} param is set to {@code true}.
     */
    private Function<User, User> enhanceToFullIfNeeded(@Nullable BooleanParam full) {
        return !needFull(full) ? Function.identity() : user ->
                usersIds.decode(user.getId())
                        .map(employmentDao::getByUser)
                        .map(employment -> user.patch().withEmployment(employment).buildFull())
                        .orElseThrow(IllegalStateException::new);
    }

    private static boolean needFull(@Nullable BooleanParam full) {
        return full != null && BooleanUtils.isTrue(full.get());
    }

    private static URI uri(String path) {
        return UriBuilder.fromResource(UsersResource.class).path(path).build();
    }
}
