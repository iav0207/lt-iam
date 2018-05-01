package task.lt.core.id;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * ID translator for user entities.
 */
@ParametersAreNonnullByDefault
public final class UsersHashIds extends HashIds {

    @Override
    String salt() {
        return "IAM: Users";
    }

    @Override
    int minLength() {
        return 11;
    }
}
