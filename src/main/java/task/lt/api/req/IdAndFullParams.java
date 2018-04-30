package task.lt.api.req;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("RSReferenceInspection")
@ParametersAreNonnullByDefault
public class IdAndFullParams {

    @PathParam("id")
    @NotNull
    @NotEmpty
    private String id;

    @QueryParam("full")
    private boolean full;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }
}
