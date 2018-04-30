package task.lt.api.model;

import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

@ParametersAreNonnullByDefault
public class Organization {

    public enum Type {
        PRIMARY,
        AFFILIATE,
        CLIENT;
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private Type type;


}
