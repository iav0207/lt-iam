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

    private static final String RESOURCE_BASE_PATH = "/organizations";

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
        response = callGet(orgId);

        softly.assertThat(response.getStatus()).isEqualTo(200);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON);
        softly.assertAll();

        assumeThat(response.hasEntity()).isTrue();
        assertThat(response.readEntity(Organization.class).getName()).isEqualTo(orgName);
    }

    @Test
    public void getOrganizationNotFound() {
        Response response = callGet("nonExistentId");
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void shouldReturnNotFoundAfterDelete() {
        final Response addResponse = callAdd("test-shouldReturnNotFoundAfterDelete");
        assumeThat(addResponse.getStatus()).isEqualTo(201);

        final String id = addResponse.readEntity(Organization.class).getId();

        assumeThat(callDelete(id).getStatus()).isEqualTo(204);

        assertThat(callGet(id).getStatus()).isEqualTo(404);
    }

    @Test
    public void updatePositive() {
        final String nameBefore = "test-update_positive-0";
        final String nameAfter = "test-update_positive-1";

        final Response addResponse = callAdd(nameBefore);
        assumeThat(addResponse.getStatus()).isEqualTo(201);

        final String id = addResponse.readEntity(Organization.class).getId();

        Organization orgUpdate = Organization.builder().withName(nameAfter).build();
        final Response updateResponse = callUpdate(id, orgUpdate);

        softly.assertThat(updateResponse.getStatus()).isEqualTo(200);
        softly.assertThat(updateResponse.getHeaderString(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON);
        softly.assertAll();

        assumeThat(updateResponse.hasEntity()).isTrue();

        Organization returnedEntity = updateResponse.readEntity(Organization.class);

        softly.assertThat(returnedEntity.getName()).isEqualTo(nameAfter);
        softly.assertThat(returnedEntity.getId()).isEqualTo(id);
        softly.assertAll();

        assertThat(callGet(id).readEntity(Organization.class).getName()).isEqualTo(nameAfter);
    }

    @Test
    public void updateNonExistentEntityShouldReturnNotFound() {
        Response response = callUpdate("nonExistent", Organization.builder().withName("newName").build());

        softly.assertThat(response.getStatus()).isEqualTo(404);
        softly.assertThat(response.hasEntity()).isFalse();
        softly.assertAll();
    }

    @Test
    public void updateWithNameThatAlreadyExistsReturnsError() {
        String nameOne = "test-updateWithNameThatAlreadyExists-1";
        String nameTwo = "test-updateWithNameThatAlreadyExists-2";
        assumeThat(callAdd(nameOne).getStatus()).isEqualTo(201);
        String id = callAdd(nameTwo).readEntity(Organization.class).getId();

        Response response = callUpdate(id, Organization.builder().withName(nameOne).build());

        softly.assertThat(response.getStatus()).isEqualTo(409);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(TEXT_PLAIN);
        softly.assertThat(response.readEntity(String.class)).isEqualTo("Organization with this name already exists");
        softly.assertAll();
    }

    @Test
    public void updateIgnoresIdAndTypeFields() {
        final Response addResponse = callAdd("test-updateIgnoresIdAndTypeFields");
        assumeThat(addResponse.getStatus()).isEqualTo(201);
        final Organization organization = addResponse.readEntity(Organization.class);
        assumeThat(organization.getType()).isEqualTo(Organization.Type.CLIENT);

        Organization update = Organization.builder()
                .withId(organization.getId() + "a")
                .withType(Organization.Type.AFFILIATE)
                .withName(organization.getName())
                .buildFull();

        Response updateResponse = callUpdate(organization.getId(), update);

        assumeThat(updateResponse.getStatus()).isEqualTo(200);

        Organization returnedEntity = updateResponse.readEntity(Organization.class);
        softly.assertThat(returnedEntity.getId()).isEqualTo(organization.getId());
        softly.assertThat(returnedEntity.getType()).isEqualTo(organization.getType());
        softly.assertAll();
    }

    @Test
    public void updateCallShouldRestoreDeletedObject() {
        final String name = "test-updateCallShouldRestoreDeletedObject";
        final Response addResponse = callAdd(name);
        assumeThat(addResponse.getStatus()).isEqualTo(201);

        final String id = addResponse.readEntity(Organization.class).getId();
        assumeThat(callDelete(id).getStatus()).isEqualTo(204);
        assumeThat(callGet(id).getStatus()).isEqualTo(404);

        Organization update = Organization.builder().withName(name).build();
        assumeThat(callUpdate(id, update).getStatus()).isEqualTo(200);

        assertThat(callGet(id).getStatus()).isEqualTo(200);
    }

    private Response callAdd(String orgName) {
        return rule.post(RESOURCE_BASE_PATH,
                Organization.builder()
                        .withType(Organization.Type.CLIENT)
                        .withName(orgName)
                        .build());
    }

    private Response callGet(String id) {
        return rule.get(RESOURCE_BASE_PATH + "/" + id);
    }

    private Response callDelete(String id) {
        return rule.delete(RESOURCE_BASE_PATH + "/" + id);
    }

    private Response callUpdate(String id, Organization update) {
        return rule.post(RESOURCE_BASE_PATH + "/" + id, update);
    }
}
