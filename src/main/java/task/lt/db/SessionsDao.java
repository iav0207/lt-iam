package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import task.lt.api.model.User;

import static task.lt.db.SessionsDao.FieldNames.ID;
import static task.lt.db.SessionsDao.FieldNames.USER;

@SuppressWarnings("squid:S1214")    // constants interface usage: acceptable as they're not public
@ParametersAreNonnullByDefault
public interface SessionsDao {

    @SqlUpdate(SQL.CREATE_TABLE_IF_NOT_EXISTS)
    void createTableIfNotExists();

    @SqlUpdate(SQL.INSERT)
    @GetGeneratedKeys
    long add(@Bind(USER) long userId);

    @SqlQuery(SQL.GET_BY_ID)
    @Mapper(UserMapper.class)
    User getById(@Bind(ID) long id);

    @SqlUpdate(SQL.DELETE)
    int delete(@Bind(ID) long id);

    interface FieldNames {
        String ID = "id";
        String USER = "user";
        String STATUS = "status";
        String TIMESTAMP = "timestamp";
    }

    interface SQL {
        String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists sessions"
                + " (id bigint primary key auto_increment,"
                + " user bigint not null, foreign key (user) references users(id),"
                + " status varchar(30) not null default 'active', check status in ('active', 'deleted'),"
                + " created timestamp not null default now());"
                + " create index if not exists ses_usr on sessions(user);"
                + " create index if not exists ses_created on sessions(created);";
        String INSERT = "insert into sessions (user) values (:user);";
        String DELETE = "update sessions set status = 'deleted' where id = :id;";
        String GET_BY_ID = "select id, u.id, u.email, u.first_name, u.last_name, u.gender"
                + " from sessions s join users u on s.user = u.id where id = :id and status = 'active';";
    }
}
