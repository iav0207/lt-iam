package task.lt.api.model;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

@ParametersAreNonnullByDefault
public class Session {

    @JsonProperty
    private String id;

    @JsonProperty
    private User user;

    public Session() {
    }

    public Session(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return Objects.equals(id, session.id) &&
                Objects.equals(user, session.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
