package task.lt.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.lt.api.model.EmploymentItem;
import task.lt.api.model.Organization;
import task.lt.core.id.OrgHashIds;

@ParametersAreNonnullByDefault
public class EmploymentMapper implements ResultSetMapper<EmploymentItem> {

    private static final Logger logger = LoggerFactory.getLogger(EmploymentMapper.class);

    private final OrgHashIds orgHashIds = new OrgHashIds();

    @Override
    public EmploymentItem map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        EmploymentItem employmentItem = new EmploymentItem(Organization.builder()
                .withId(orgHashIds.encode(r.getLong("org_id")))
                .withName(r.getString("org_name"))
                .withType(Organization.Type.fromString(r.getString("org_type")))
                .build(),
                r.getString(EmploymentDao.FieldNames.JOB_TITLE));
        if (logger.isDebugEnabled()) {
            logger.debug("Mapped employment item: {}", employmentItem);
        }
        return employmentItem;
    }
}
