package task.lt.api.model;

import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;

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
    private String id;

    @JsonProperty
    @NotNull
    @Length(min = 1, max = 30)
    private String firstName;

    @JsonProperty
    @NotNull
    @Length(min = 1, max = 30)
    private String lastName;

    @JsonProperty
    @NotNull
    private Gender gender;

    @JsonProperty
    @NotEmpty
    @Length(min = 3, max = 30)
    private String email;

    @JsonProperty
    private List<EmploymentItem> employment = emptyList();

    public static Builder builder() {
        return new Builder();
    }

    public User() {
    }

    User(Builder b) {
        this.id = b.id;
        this.firstName = b.firstName;
        this.lastName = b.lastName;
        this.gender = b.gender;
        this.email = b.email;
        this.employment = b.employment == null ? emptyList() : b.employment;
    }

    @JsonIgnore
    public Builder patch() {
        return new Builder(this);
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


    public static class Builder {
        private String id;
        private String firstName;
        private String lastName;
        private Gender gender;
        private String email;
        private List<EmploymentItem> employment;

        Builder() {
        }

        private Builder(User u) {
            this.id = u.id;
            this.firstName = u.firstName;
            this.lastName = u.lastName;
            this.gender = u.gender;
            this.email = u.email;
            this.employment = u.employment;
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

        public User buildFull() {
            checkNotNull(id);
            checkNotNull(firstName);
            checkNotNull(lastName);
            checkNotNull(gender);
            return build();
        }

        public User build() {
            return new User(this);
        }
    }
}
