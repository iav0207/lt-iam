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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static task.lt.db.UsersDao.FieldNames.EMAIL;
import static task.lt.db.UsersDao.FieldNames.FIRST_NAME;
import static task.lt.db.UsersDao.FieldNames.GENDER;
import static task.lt.db.UsersDao.FieldNames.ID;
import static task.lt.db.UsersDao.FieldNames.LAST_NAME;

@ParametersAreNonnullByDefault
public class UserMapper implements ResultSetMapper<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    private final UsersHashIds hashIds = new UsersHashIds();
    private final String fieldsPrefix;

    public UserMapper() {
        this("");
    }

    public UserMapper(String fieldsPrefix) {
        this.fieldsPrefix = fieldsPrefix;
    }

    @Override
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        User user = User.builder()
                .withId(hashIds.encode(r.getLong(field(ID))))
                .withEmail(r.getString(field(EMAIL)))
                .withFirstName(r.getString(field(FIRST_NAME)))
                .withLastName(r.getString(field(LAST_NAME)))
                .withGender(User.Gender.valueOf(r.getString(field(GENDER))))
                .buildFull();
        if (logger.isDebugEnabled()) {
            logger.debug("Mapped user: {}", user);
        }
        return user;
    }

    private String field(String fieldName) {
        return isBlank(fieldsPrefix) ? fieldName : fieldsPrefix + "." + fieldName;
    }
}
