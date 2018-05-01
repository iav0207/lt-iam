package task.lt.resources;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.core.Response;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import task.lt.api.model.User;
import task.lt.api.model.UserWithPassword;

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static task.lt.api.model.User.Gender.FEMALE;
import static task.lt.api.model.User.Gender.MALE;

@ParametersAreNonnullByDefault
public class UsersResourceIT {

    private static final String RESOURCE_BASE_PATH = "/users";

    private final ResourceTestingSupport rule = new ResourceTestingSupport("users client");
    private final AtomicInteger increment = new AtomicInteger();

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
        Response response = callAdd(generateUser());

        softly.assertThat(response.getStatus()).isEqualTo(201);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON);
        softly.assertThat(response.hasEntity()).isTrue();
        softly.assertAll();

        assertThat(response.readEntity(User.class)).isNotNull();
    }

    @Test
    public void addExistentUserReturnsError() {
        UserWithPassword user = generateUser();
        assumeThat(callAdd(user).getStatus()).isEqualTo(201);
        final Response response = callAdd(user);

        softly.assertThat(response.getStatus()).isEqualTo(409);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(TEXT_PLAIN);
        softly.assertThat(response.hasEntity()).isTrue();
        softly.assertAll();

        assertThat(response.readEntity(String.class)).isEqualTo("User with this email already exists");
    }

    private Response callAdd(UserWithPassword user) {
        return rule.post(RESOURCE_BASE_PATH, user);
    }

    private Response callGet(String path) {
        return rule.get(RESOURCE_BASE_PATH + path);
    }

    private Response callDelete(String path) {
        return rule.delete(RESOURCE_BASE_PATH + path);
    }

    private Response callUpdate(String path, UserWithPassword update) {
        return rule.post(RESOURCE_BASE_PATH + path, update);
    }

    private UserWithPassword generateUser() {
        final int i = increment.getAndIncrement();
        return new UserWithPassword("password-" + i,
                User.builder()
                        .withEmail(String.format("john.doe-%d@example.com", i))
                        .withGender(i % 2 == 0 ? MALE : FEMALE)
                        .withFirstName("John" + i)
                        .withLastName("Doe" + i));
    }

}
