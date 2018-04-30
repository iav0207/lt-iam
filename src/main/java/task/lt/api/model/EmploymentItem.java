package task.lt.api.model;

import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

@ParametersAreNonnullByDefault
public class EmploymentItem {

    @JsonProperty
    private Organization organization;

    @JsonProperty
    private String jobTitle;

    public EmploymentItem() {
    }

    public EmploymentItem(Organization organization, String jobTitle) {
        this.organization = organization;
        this.jobTitle = jobTitle;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
