package task.lt.resources;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.core.Response;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import task.lt.api.model.CredentialsProvider;
import task.lt.api.model.Session;
import task.lt.api.model.UserWithPassword;
import task.lt.api.req.Credentials;

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@ParametersAreNonnullByDefault
public class SessionsResourceIT {

    private static final String RESOURCE_BASE_PATH = "/sessions";

    private final ResourceTestingSupport rule = new ResourceTestingSupport("sessions client");
    private final Supplier<UserWithPassword> usersGenerator = new TestUsersGenerator()::generateUser;

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
        Response response = callAdd(createUser());
        softly.assertThat(response.getStatus()).isEqualTo(201);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON);
        softly.assertThat(response.getHeaderString(LOCATION)).matches("http://localhost:\\d+/sessions/\\w+");
        softly.assertThat(response.hasEntity()).isTrue();
        softly.assertAll();
        assertThat(response.readEntity(Session.class)).isNotNull();
    }

    @Test(dataProvider = "credentialsDistorter")
    public void addWithInvalidCredentialsReturnsError(Function<CredentialsProvider, Credentials> distorter) {
        CredentialsProvider user = createUser();
        Response response = callAdd(distorter.apply(user));
        softly.assertThat(response.getStatus()).isEqualTo(400);
        softly.assertThat(response.getHeaderString(CONTENT_TYPE)).isEqualTo(TEXT_PLAIN);
        softly.assertThat(response.readEntity(String.class)).isEqualTo("Invalid credentials");
        softly.assertAll();
    }

    @DataProvider(name = "credentialsDistorter")
    public static Object[][] credentialsDistorter() {
        return new Object[][]{
                {distorter(c -> new Credentials(c.getEmail() + "a", c.getPassword()))},
                {distorter(c -> new Credentials(c.getEmail(), c.getPassword() + "a"))},
                {distorter(c -> new Credentials(randomAlphanumeric(10), randomAlphanumeric(10)))},
        };
    }

    private static Function<CredentialsProvider, Credentials> distorter(
            Function<CredentialsProvider, Credentials> lambda)
    {
        return lambda;
    }

    private Response callAdd(CredentialsProvider cred) {
        return rule.post(RESOURCE_BASE_PATH, new Credentials(cred.getEmail(), cred.getPassword()));
    }

    private CredentialsProvider createUser() {
        UserWithPassword user = usersGenerator.get();
        assumeThat(rule.post("/users", user).getStatus()).isEqualTo(201);
        return user;
    }
}
