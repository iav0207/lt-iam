package task.lt.db;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import task.lt.api.model.User;

import static task.lt.db.UsersDao.FieldNames.EMAIL;
import static task.lt.db.UsersDao.FieldNames.ID;
import static task.lt.db.UsersDao.FieldNames.PASSWORD;

@SuppressWarnings("squid:S1214")    // constants interface usage: acceptable as they're not public
@ParametersAreNonnullByDefault
public interface UsersDao {

    @SqlUpdate(SQL.CREATE_TABLE_IF_NOT_EXISTS)
    void createTableIfNotExists();

    @SqlUpdate(SQL.INSERT)
    @GetGeneratedKeys
    long add(@BindBean("u") User user, @Bind(PASSWORD) String passwordHash);

    @SqlQuery(SQL.GET_BY_ID)
    @Mapper(UserMapper.class)
    User getById(@Bind(ID) long id);

    @SqlQuery(SQL.GET_BY_EMAIL)
    @Mapper(UserMapper.class)
    User getByEmail(@Bind(EMAIL) String email);

    @SqlQuery(SQL.EXISTS_EMAIL)
    boolean exists(@Bind(EMAIL) String email);

    @SqlQuery(SQL.EXISTS_ID)
    boolean exists(@Bind(ID) long id);

    @SqlQuery(SQL.EXISTS_AND_ACTIVE_ID)
    boolean existsAndActive(@Bind(ID) long id);

    @SqlQuery(SQL.CHECK_CREDENTIALS)
    boolean checkCredentials(
            @Bind(EMAIL) String email,
            @Bind(PASSWORD) String passwordHash);

    @SqlUpdate(SQL.DELETE)
    int delete(@Bind(ID) long id);

    @SqlUpdate(SQL.UPDATE)
    int update(@Bind(ID) long id,
            @BindBean("u") User update,
            @Bind(PASSWORD) @Nullable String passwordHash);

    interface FieldNames {
        String ID = "id";
        String EMAIL = "email";
        @SuppressWarnings("squid:S2068")
        String PASSWORD = "password";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String GENDER = "gender";
        String STATUS = "status";
        String TIMESTAMP = "timestamp";
    }

    interface SQL {
        String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists users"
                + " (id bigint primary key auto_increment,"
                + " email varchar(64) unique not null, check (length(email) > 3),"
                + " password varchar(64) not null, check (length(password) = 64),"
                + " first_name varchar(30) not null, check (length(first_name) > 0),"
                + " last_name varchar(30) not null, check(length(last_name) > 0),"
                + " gender varchar(10), check gender in ('MALE', 'FEMALE'),"
                + " status varchar(30) not null default 'active', check status in ('active', 'deleted'),"
                + " created timestamp not null default now());"
                + " create index if not exists usr_email on users(email);"
                + " create index if not exists usr_fn on users(first_name);"
                + " create index if not exists usr_ln on users(last_name);"
                + " create index if not exists usr_created on users(created);";
        String INSERT = "insert into users (email, password, first_name, last_name, gender)"
                + " values (:u.email, :password, :u.firstName, :u.lastName, :u.gender);";
        String GET_BY_ID = "select id, email, first_name, last_name, gender from users where id = :id;";
        String GET_BY_EMAIL = "select id, email, first_name, last_name, gender from users where email = :email;";
        String CHECK_CREDENTIALS = "select count(*) > 0 from users"
                + " where email = :email and password = :password and status = 'active';";
        String EXISTS_EMAIL = "select count(*) > 0 from users where email = :email;";
        String EXISTS_ID = "select count(*) > 0 from users where id = :id;";
        String EXISTS_AND_ACTIVE_ID = "select count(*) > 0 from users where id = :id and status = 'active';";
        String DELETE = "update users set status = 'deleted' where id = :id;";
        String UPDATE = "update users set"
                + " first_name = ifnull(:u.firstName, first_name),"
                + " last_name = ifnull(:u.lastName, last_name),"
                + " gender = ifnull(:u.gender, gender),"
                + " password = ifnull(:password, password),"
                + " status = 'active'"
                + " where id = :id";
    }
}
