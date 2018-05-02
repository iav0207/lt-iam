package task.lt.api.req;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import task.lt.api.model.CredentialsProvider;

@ParametersAreNonnullByDefault
public class Credentials implements CredentialsProvider {

    @JsonProperty
    @Length(min = 3, max = 30)
    private String email;

    @JsonProperty
    @Length(min = 8, max = 30)
    private String password;

    public Credentials() {
    }

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private Credentials(Builder b) {
        this.email = b.email;
        this.password = b.password;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Credentials that = (Credentials) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    public static final class Builder {
        private String email;
        private String password;

        private Builder() {
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Credentials build() {
            return new Credentials(this);
        }
    }
}
