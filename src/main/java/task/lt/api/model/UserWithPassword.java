package task.lt.api.model;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class UserWithPassword extends User {

    @JsonProperty
    @NotNull
    @NotEmpty
    private String password;

    public static Builder builder() {
        return new Builder();
    }

    public UserWithPassword() {
    }

    public UserWithPassword(String password, User.Builder b) {
        super(b);
        this.password = checkNotNull(password);
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
        if (!super.equals(o)) {
            return false;
        }
        UserWithPassword that = (UserWithPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), password);
    }
}
