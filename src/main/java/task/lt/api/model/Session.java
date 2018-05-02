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

    private Session(Builder b) {
        this.id = b.id;
        this.user = b.user;
    }

    public static Builder builder() {
        return new Builder();
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


    public static final class Builder {
        private String id;
        private User user;

        private Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
