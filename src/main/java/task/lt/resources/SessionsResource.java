package task.lt.resources;

import java.net.URI;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import task.lt.api.err.ApiErrors;
import task.lt.api.model.Session;
import task.lt.api.model.User;
import task.lt.api.req.Credentials;
import task.lt.api.resp.ApiResponse;
import task.lt.core.id.SessionsHashIds;
import task.lt.core.id.UsersHashIds;
import task.lt.core.pass.PasswordDigest;
import task.lt.db.SessionsDao;
import task.lt.db.UsersDao;

import static task.lt.api.err.ApiErrors.invalidCredentials;
import static task.lt.api.resp.ApiResponse.created;

@Path("/sessions")
@Consumes(MediaType.APPLICATION_JSON)
@ParametersAreNonnullByDefault
public class SessionsResource {

    private final SessionsHashIds sessionsHashIds = new SessionsHashIds();
    private final UsersHashIds usersHashIds = new UsersHashIds();
    private final UsersDao usersDao;
    private final SessionsDao sessionsDao;

    public SessionsResource(UsersDao usersDao, SessionsDao sessionsDao) {
        this.usersDao = usersDao;
        this.sessionsDao = sessionsDao;
    }

    /**
     * Delegated access is not supported.
     */
    @POST
    public Response start(@Valid Credentials credentials) {
        if (!usersDao.checkCredentials(credentials.getEmail(), PasswordDigest.hash(credentials.getPassword()))) {
            return invalidCredentials();
        }
        User user = usersDao.getByEmail(credentials.getEmail());
        long id = sessionsDao.add(usersHashIds.decode(user.getId()).orElseThrow(IllegalStateException::new));
        Session createdSession = Session.builder()
                .withId(sessionsHashIds.encode(id))
                .withUser(user)
                .build();
        return created(uri(createdSession.getId()), createdSession);
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String sessionHashId) {
        return sessionsHashIds.decode(sessionHashId)
                .map(sessionsDao::getById)
                .map(ApiResponse::ok)
                .orElseGet(ApiErrors::notFound);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String sessionHashId) {
        return sessionsHashIds.decode(sessionHashId)
                .map(sessionsDao::delete)
                .filter(upd -> upd == 1)
                .map(upd -> ApiResponse.noContent())
                .orElseGet(ApiErrors::notFound);
    }

    private static URI uri(String path) {
        return UriBuilder.fromResource(SessionsResource.class).path(path).build();
    }
}
