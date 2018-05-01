package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import task.lt.api.model.User;
import task.lt.api.model.UserWithPassword;

@SuppressWarnings("squid:S1214")    // constants interface usage: acceptable as they're not public
@ParametersAreNonnullByDefault
public interface UsersDao {

    @SqlUpdate(SQL.CREATE_TABLE_IF_NOT_EXISTS)
    void createTableIfNotExists();

    @SqlUpdate(SQL.INSERT)
    @GetGeneratedKeys
    long add(@BindBean("u") UserWithPassword user);

    @SqlQuery(SQL.GET_BY_ID)
    @Mapper(UserMapper.class)
    User getById(@Bind(FieldNames.ID) long id);

    @SqlQuery(SQL.CHECK_CREDENTIALS)
    boolean checkCredentials(
            @Bind(FieldNames.EMAIL) String email,
            @Bind(FieldNames.PASSWORD) String password);

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
                + " email varchar(64) unique not null, password varchar(32) not null,"
                + " first_name varchar(30) not null, last_name varchar(30) not null,"
                + " gender varchar(10), check gender in ('MALE', 'FEMALE'),"
                + " status varchar(30) not null default 'active', check status in ('active', 'deleted'),"
                + " created timestamp not null default now());"
                + " create index if not exists usr_email on users(email);"
                + " create index if not exists usr_fn on users(first_name);"
                + " create index if not exists usr_ln on users(last_name);"
                + " create index if not exists usr_created on users(created);";
        String INSERT = "insert into users (email, password, first_name, last_name, gender)"
                + " values (:u.email, :u.password, :u.firstName, :u.lastName, :u.gender);";
        String GET_BY_ID = "select id, email, first_name, last_name, gender from users where id = :id;";
        String CHECK_CREDENTIALS = "select count(*) > 0 from users where email = :email and password = :password;";
    }
}
