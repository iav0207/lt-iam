package task.lt.db;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

@SuppressWarnings("squid:S1214")    // constants interface usage: acceptable as they're not public
@ParametersAreNonnullByDefault
public interface OrgTypesDao {

    @SqlUpdate(SQL.CREATE_TABLE_IF_NOT_EXISTS)
    void createTableIfNotExists();

    @SqlQuery(SQL.GET_ALL)
    List<String> getAll();

    @SqlQuery(SQL.EXISTS)
    boolean exists(@Bind(FieldNames.NAME) String name);

    @SqlUpdate(SQL.INSERT)
    @GetGeneratedKeys
    int add(@Bind(FieldNames.NAME) String name);

    interface FieldNames {
        String ID = "id";
        String NAME = "name";
    }

    interface SQL {
        String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists org_types"
                + " (id int primary key auto_increment, name varchar(64) not null unique);"
                + " create index if not exists org_type_name on org_types(name);";
        String GET_ALL = "select name from org_types order by name;";
        String EXISTS = "select count(*) > 0 from org_types where name = :name;";
        String INSERT = "insert into org_types (id, name) values (default, :name);";
    }
}
