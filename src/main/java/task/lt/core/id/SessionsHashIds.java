package task.lt.core.id;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * ID translator for sessions.
 */
@ParametersAreNonnullByDefault
public final class SessionsHashIds extends HashIds {

    @Override
    String salt() {
        return "IAM: Sessions";
    }

    @Override
    int minLength() {
        return 15;
    }
}
