package task.lt.core.pass;

import javax.annotation.ParametersAreNonnullByDefault;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ParametersAreNonnullByDefault
public class PasswordDigestTest {

    @Test
    public void resultShouldHaveLength64() {
        assertThat(PasswordDigest.hash("a")).hasSize(64);
    }

}
