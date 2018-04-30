package task.lt.api.model;

import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

@ParametersAreNonnullByDefault
public class EmploymentItem {

    @JsonProperty
    private Organization organization;

    @JsonProperty
    private String jobTitle;
}
