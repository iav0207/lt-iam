package task.lt.api.err;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ParametersAreNonnullByDefault
public class ApiErrors {

    private ApiErrors() {
        // no instantiation
    }

    public static Response userWithSuchEmailAlreadyExists() {
        return err(409, "User with this email already exists");
    }

    public static Response suchOrganizationAlreadyExists() {
        return err(409, "Organization with this name already exists");
    }

    public static Response notFound() {
        return Response.status(404).build();
    }

    private static Response err(int code, String message) {
        return Response.status(code)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(message)
                .build();
    }
}
