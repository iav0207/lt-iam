package task.lt.api.model;

import java.util.NoSuchElementException;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;

@ParametersAreNonnullByDefault
public class Organization {

    public enum Type {
        PRIMARY,
        AFFILIATE,
        CLIENT;

        public static Type fromString(String s) {
            return stream(values()).filter(v -> v.name().equalsIgnoreCase(s))
                    .findFirst().orElseThrow(() -> new NoSuchElementException(s));
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @JsonProperty
    private String id;

    @JsonProperty
    @Length(min = 1, max = 60)
    private String name;

    @JsonProperty
    @NotNull
    private Type type;

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("squid:S2637")
    public Organization() {
    }

    private Organization(Builder b) {
        this.id = b.id;
        this.name = b.name;
        this.type = b.type;
    }

    public Builder patch() {
        return new Builder(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organization that = (Organization) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    public static final class Builder {
        private String id;
        private String name;
        private Type type;

        private Builder() {
        }

        private Builder(Organization o) {
            this.id = o.id;
            this.name = o.name;
            this.type = o.type;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public Organization build() {
            return new Organization(this);
        }

        public Organization buildFull() {
            checkNotNull(id);
            checkNotNull(type);
            checkNotNull(name);
            return build();
        }
    }
}
