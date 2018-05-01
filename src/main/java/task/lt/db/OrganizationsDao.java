package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import task.lt.api.model.Organization;

@SuppressWarnings("squid:S1214")    // constants interface usage: acceptable as they're not public
@ParametersAreNonnullByDefault
public interface OrganizationsDao {

    @SqlUpdate(SQL.CREATE_TABLE_IF_NOT_EXISTS)
    void createTableIfNotExists();

    @SqlUpdate(SQL.INSERT)
    @GetGeneratedKeys
    long add(@Bind(FieldNames.NAME) String name, @Bind(FieldNames.TYPE) String type);

    @SqlQuery(SQL.NAME_EXISTS)
    boolean exists(@Bind(FieldNames.NAME) String name);

    @SqlQuery(SQL.ID_EXISTS)
    boolean exists(@Bind(FieldNames.ID) long id);

    @SqlQuery(SQL.GET_BY_ID)
    @Mapper(OrganizationMapper.class)
    Organization getById(@Bind(FieldNames.ID) long id);

    // TODO list: search, type, limit, offset

    interface FieldNames {
        String ID = "id";
        String NAME = "name";
        String TYPE = "type";
    }

    interface SQL {
        String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists organizations"
                + " (id bigint primary key auto_increment,"
                + " name varchar(64) not null,"
                + " type int not null, foreign key (type) references org_types(id),"
                + " status varchar(30) not null default 'active',"
                + " check status in ('active', 'deleted'),"
                + " created timestamp not null default now());"
                + " create index if not exists org_name on organizations(name);"
                + " create index if not exists org_created on organizations(created);";
        String NAME_EXISTS = "select count(*) > 0 from organizations where name = :name;";
        String ID_EXISTS = "select count(*) > 0 from organizations where id = :id;";
        String INSERT = "insert into organizations (name, type) values"
                + " (:name, (select id from org_types where name = :type))";
        String GET_BY_ID = "select o.id as id, t.name as type, o.name as name"
                + " from organizations o left join org_types t on o.type = t.id"
                + " where o.id = :id;";
    }
}
