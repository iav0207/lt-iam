package task.lt.core.id;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * ID translator for organization entities.
 */
@ParametersAreNonnullByDefault
public final class OrgHashIds extends HashIds {

    @Override
    String salt() {
        return "IAM: Organizations";
    }

    @Override
    int minLength() {
        return 8;
    }
}
