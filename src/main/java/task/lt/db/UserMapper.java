package task.lt.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.lt.api.model.User;
import task.lt.core.id.UsersHashIds;

import static task.lt.db.UsersDao.FieldNames.EMAIL;
import static task.lt.db.UsersDao.FieldNames.FIRST_NAME;
import static task.lt.db.UsersDao.FieldNames.GENDER;
import static task.lt.db.UsersDao.FieldNames.ID;
import static task.lt.db.UsersDao.FieldNames.LAST_NAME;

@ParametersAreNonnullByDefault
public class UserMapper implements ResultSetMapper<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    private final UsersHashIds hashIds = new UsersHashIds();

    @Override
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        User user = User.builder()
                .withId(hashIds.encode(r.getLong(ID)))
                .withEmail(r.getString(EMAIL))
                .withFirstName(r.getString(FIRST_NAME))
                .withLastName(r.getString(LAST_NAME))
                .withGender(User.Gender.valueOf(r.getString(GENDER)))
                .buildFull();
        if (logger.isDebugEnabled()) {
            logger.debug("Mapped user: {}", user);
        }
        return user;
    }
}
