package task.lt.resources;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import task.lt.AppConfiguration;
import task.lt.IamApplication;

@ParametersAreNonnullByDefault
class ResourceTestingSupport extends DropwizardTestSupport<AppConfiguration> {

    private final String name;
    private Client client;

    ResourceTestingSupport(String clientName) {
        super(IamApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));
        this.name = clientName;
    }

    @Override
    public void before() {
        super.before();
        client = newClient(name);
    }

    Response get(String uri) {
        return client.target(target(uri))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }

    Response post(String uri, Object entity) {
        return client.target(target(uri))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(entity));
    }

    Response delete(String uri) {
        return client.target(target(uri))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
    }

    private String target(String uri) {
        return "http://localhost:" + getLocalPort() + uri;
    }

    private Client newClient(String name) {
        return new JerseyClientBuilder(getEnvironment()).build(name);
    }


}
