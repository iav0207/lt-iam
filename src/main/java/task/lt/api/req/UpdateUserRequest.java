package task.lt.api.req;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import task.lt.api.model.User;

@ParametersAreNonnullByDefault
public class UpdateUserRequest {

    @JsonProperty
    @Length(min = 1, max = 30)
    private String firstName;

    @JsonProperty
    @Length(min = 1, max = 30)
    private String lastName;

    @JsonProperty
    private User.Gender gender;

    @JsonProperty
    @Length(min = 8, max = 30)
    private String password;

    public static Builder builder() {
        return new Builder();
    }

    public UpdateUserRequest() {
    }

    private UpdateUserRequest(Builder b) {
        this.firstName = b.firstName;
        this.lastName = b.lastName;
        this.gender = b.gender;
        this.password = b.password;
    }

    @JsonIgnore
    public Boolean isEmpty() {
        return firstName == null && lastName == null && gender == null && password == null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public User.Gender getGender() {
        return gender;
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
        UpdateUserRequest that = (UpdateUserRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                gender == that.gender &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender, password);
    }


    public static final class Builder {
        private String firstName;
        private String lastName;
        private User.Gender gender;
        private String password;

        private Builder() {
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withGender(User.Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UpdateUserRequest build() {
            return new UpdateUserRequest(this);
        }
    }
}
