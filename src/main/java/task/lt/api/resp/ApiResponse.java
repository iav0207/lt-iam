package task.lt.api.resp;

import java.net.URI;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ParametersAreNonnullByDefault
public class ApiResponse {

    private ApiResponse () {
        // no instantiation
    }

    public static Response ok(Object entity) {
        return Response.ok(entity).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static Response created(URI uri, Object entity) {
        return Response.created(uri)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(entity)
                .build();
    }

    public static URI uri(String uri) {
        return URI.create(uri);
    }
}
