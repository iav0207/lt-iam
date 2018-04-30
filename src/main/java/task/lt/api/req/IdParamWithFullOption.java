package task.lt.api.req;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import io.dropwizard.jersey.params.BooleanParam;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("RSReferenceInspection")
@ParametersAreNonnullByDefault
public class IdParamWithFullOption {

    @SuppressWarnings("RSReferenceInspection")
    @PathParam("id")
    @NotNull
    @NotEmpty
    private String id;

    @QueryParam("full")
    private BooleanParam full;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BooleanParam getFull() {
        return full;
    }

    public void setFull(BooleanParam full) {
        this.full = full;
    }
}
