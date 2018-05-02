package task.lt.api.model;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface CredentialsProvider {

    String getEmail();

    String getPassword();

}
