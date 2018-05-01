package task.lt.core.pass;

import java.nio.charset.StandardCharsets;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.hash.Hashing;

/**
 * Passwords should never be stored in open form, so here is a simple hashing provider.
 * <p>
 * Algorithm: SHA-256.
 */
@ParametersAreNonnullByDefault
public class PasswordDigest {

    private PasswordDigest() {
        // no instantiation
    }

    /**
     * Hash password.
     *
     * @param pass password in open form
     * @return SHA-256 digest.
     */
    public static String hash(String pass) {
        return Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString();
    }

}
