package task.lt.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.lt.api.model.Organization;
import task.lt.core.id.OrgHashIds;

import static task.lt.db.OrganizationsDao.FieldNames.ID;
import static task.lt.db.OrganizationsDao.FieldNames.NAME;
import static task.lt.db.OrganizationsDao.FieldNames.TYPE;

@ParametersAreNonnullByDefault
public class OrganizationMapper implements ResultSetMapper<Organization> {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationMapper.class);

    private final OrgHashIds hashIds = new OrgHashIds();

    @Override
    public Organization map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        Organization org = Organization.builder()
                .withId(hashIds.encode(r.getLong(ID)))
                .withName(r.getString(NAME))
                .withType(Organization.Type.fromString(r.getString(TYPE)))
                .buildFull();
        if (logger.isDebugEnabled()) {
            logger.debug("Mapped organization: {}", org);
        }
        return org;
    }
}
