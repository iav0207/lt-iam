package task.lt.resources;

import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.core.Response;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import task.lt.api.model.Organization;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpHeaders.LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;


@ParametersAreNonnullByDefault
public class OrganizationsResourceIT {

    private final ResourceTestingSupport rule = new ResourceTestingSupport("org client");

    private SoftAssertions softly;

    @BeforeClass
    public void beforeClass() {
        rule.before();
    }

    @AfterClass
    public void afterClass() {
        rule.after();
    }

    @BeforeMethod
    public void reset() {
        softly = new SoftAssertions();
    }

    @Test
    public void addPositive() {
        Response response = callAdd("Foo Unlimited");

        softly.assertThat(response.getStatus()).isEqualTo(201);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON);
        softly.assertThat(response.getHeaderString(LOCATION))
                .matches("http://localhost:\\d+/organizations/\\w+");
        softly.assertAll();

        assumeThat(response.hasEntity()).isTrue();
        assertThat(response.readEntity(Organization.class)).isNotNull();
    }

    @Test
    public void addOrganizationAlreadyExists() {
        Supplier<Response> post = () -> callAdd("Repeated");

        assumeThat(post.get().getStatus()).isEqualTo(201);  // first call: success

        Response response = post.get();

        softly.assertThat(response.getStatus()).isEqualTo(409);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(TEXT_PLAIN);
        softly.assertThat(response.readEntity(String.class)).isEqualTo("Organization with this name already exists");
        softly.assertAll();
    }

    @Test
    public void getPositive() {
        final String orgName = "test-get_positive";
        Response response = callAdd(orgName);
        assumeThat(response.getStatus()).isEqualTo(201);

        String orgId = response.readEntity(Organization.class).getId();
        response = callGet("/" + orgId);

        softly.assertThat(response.getStatus()).isEqualTo(200);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON);
        softly.assertAll();

        assumeThat(response.hasEntity()).isTrue();
        assertThat(response.readEntity(Organization.class).getName()).isEqualTo(orgName);
    }

    @Test
    public void getOrganizationNotFound() {
        Response response = callGet("/nonExistentId");
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void shouldReturnNotFoundAfterDelete() {
        final Response addResponse = callAdd("test-shouldReturnNotFoundAfterDelete");
        assumeThat(addResponse.getStatus()).isEqualTo(201);

        final String id = addResponse.readEntity(Organization.class).getId();

        assumeThat(callDelete("/" + id).getStatus()).isEqualTo(204);

        assertThat(callGet(id).getStatus()).isEqualTo(404);
    }

    private Response callAdd(String orgName) {
        return rule.post("/organizations",
                Organization.builder()
                        .withType(Organization.Type.CLIENT)
                        .withName(orgName)
                        .build());
    }

    private Response callGet(String path) {
        return rule.get("/organizations" + path);
    }

    private Response callDelete(String path) {
        return rule.delete("/organizations" + path);
    }
}
