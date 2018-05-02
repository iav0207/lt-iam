package task.lt.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.lt.api.model.Session;
import task.lt.core.id.SessionsHashIds;

import static task.lt.db.SessionsDao.FieldNames.ID;

@ParametersAreNonnullByDefault
public class SessionMapper implements ResultSetMapper<Session> {

    private static final Logger logger = LoggerFactory.getLogger(SessionMapper.class);

    private final SessionsHashIds sessionsHashIds = new SessionsHashIds();
    private final UserMapper userMapper = new UserMapper("users");

    @Override
    public Session map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        Session session = new Session(sessionsHashIds.encode(r.getLong(ID)), userMapper.map(index, r, ctx));
        if (logger.isDebugEnabled()) {
            logger.debug("Mapped session: {}", session);
        }
        return session;
    }
}
