package task.lt.api.model;

import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API representation of user entity.
 */
@ParametersAreNonnullByDefault
public class User {

    public enum Gender {
        MALE,
        FEMALE;
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @JsonProperty
    @NotEmpty
    private String id;

    @JsonProperty
    @NotNull
    @NotEmpty
    private String firstName;

    @JsonProperty
    @NotNull
    @NotEmpty
    private String lastName;

    @JsonProperty
    @NotNull
    private Gender gender;

    @JsonProperty
    @NotEmpty
    private String email;

    @JsonProperty
    private List<EmploymentItem> employment;

    public static Builder builder() {
        return new Builder();
    }

    public User() {
    }

    private User(Builder b) {
        this.id = checkNotNull(b.id);
        this.firstName = checkNotNull(b.firstName);
        this.lastName = checkNotNull(b.lastName);
        this.gender = checkNotNull(b.gender);
        this.email = b.email;
        this.employment = checkNotNull(b.employment);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public List<EmploymentItem> getEmployment() {
        return employment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                gender == user.gender &&
                Objects.equals(email, user.email) &&
                Objects.equals(employment, user.employment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, gender, email, employment);
    }


    public static final class Builder {
        private String id;
        private String firstName;
        private String lastName;
        private Gender gender;
        private String email;
        private List<EmploymentItem> employment;

        private Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withEmployment(List<EmploymentItem> employment) {
            this.employment = employment;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
