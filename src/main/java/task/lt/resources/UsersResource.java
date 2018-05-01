package task.lt.resources;

import java.net.URI;

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

import task.lt.api.err.ApiErrors;
import task.lt.api.model.User;
import task.lt.api.model.UserWithPassword;
import task.lt.api.req.IdParamWithFullOption;
import task.lt.api.resp.ApiResponse;
import task.lt.core.id.UsersHashIds;
import task.lt.core.pass.PasswordDigest;
import task.lt.db.UsersDao;

import static task.lt.api.err.ApiErrors.userWithSuchEmailAlreadyExists;
import static task.lt.api.resp.ApiResponse.created;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class UsersResource {

    private final UsersHashIds hashIds = new UsersHashIds();
    private final UsersDao dao;

    public UsersResource(UsersDao dao) {
        this.dao = dao;
    }

    @POST
    public Response add(@Valid final UserWithPassword user) {
        if (dao.exists(user.getEmail())) {
            return userWithSuchEmailAlreadyExists();
        }
        long id = dao.add(user, PasswordDigest.hash(user.getPassword()));
        User createdUser = dao.getById(id);
        return created(uri(createdUser.getId()), createdUser);
    }

    @GET
    @Path("{id}")
    public Response get(@Valid @BeanParam IdParamWithFullOption params) {
        return hashIds.decode(params.getId())
                .filter(dao::exists)
                .map(dao::getById)
                .map(ApiResponse::ok)
                .orElseGet(ApiErrors::notFound);
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

    private static URI uri(String path) {
        return UriBuilder.fromResource(UsersResource.class).path(path).build();
    }
}
