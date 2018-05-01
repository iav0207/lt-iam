package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import task.lt.api.model.Organization;

import static task.lt.db.OrganizationsDao.FieldNames.ID;
import static task.lt.db.OrganizationsDao.FieldNames.NAME;
import static task.lt.db.OrganizationsDao.FieldNames.TYPE;

@SuppressWarnings("squid:S1214")    // constants interface usage: acceptable as they're not public
@ParametersAreNonnullByDefault
public interface OrganizationsDao {

    @SqlUpdate(SQL.CREATE_TABLE_IF_NOT_EXISTS)
    void createTableIfNotExists();

    @SqlUpdate(SQL.INSERT)
    @GetGeneratedKeys
    long add(@Bind(NAME) String name, @Bind(TYPE) String type);

    @SqlQuery(SQL.NAME_EXISTS)
    boolean exists(@Bind(NAME) String name);

    @SqlQuery(SQL.ID_EXISTS)
    boolean exists(@Bind(ID) long id);

    @SqlQuery(SQL.ID_EXISTS_AND_ACTIVE)
    boolean existsAndActive(@Bind(ID) long id);

    @SqlQuery(SQL.GET_BY_ID)
    @Mapper(OrganizationMapper.class)
    Organization getById(@Bind(ID) long id);

    @SqlQuery(SQL.GET_DELETED_BY_ID)
    @Mapper(OrganizationMapper.class)
    Organization getDeletedById(@Bind(ID) long id);

    @SqlQuery(SQL.GET_NAME_BY_ID)
    String getName(@Bind(ID) long id);

    // TODO list: search, type, limit, offset

    @SqlUpdate(SQL.DELETE)
    int delete(@Bind(ID) long id);

    @SqlUpdate(SQL.RESTORE)
    int restore(@Bind(ID) long id);

    @SqlUpdate(SQL.UPDATE_NAME)
    int update(@Bind(ID) long id, @Bind(NAME) String name);

    interface FieldNames {
        String ID = "id";
        String NAME = "name";
        String TYPE = "type";
        String STATUS = "status";
        String TIMESTAMP = "timestamp";
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
        String ID_EXISTS_AND_ACTIVE = "select count(*) > 0 from organizations"
                + " where id = :id and status = 'active';";
        String INSERT = "insert into organizations (name, type) values"
                + " (:name, (select id from org_types where name = :type))";
        String GET_BY_ID = "select o.id as id, t.name as type, o.name as name"
                + " from organizations o left join org_types t on o.type = t.id"
                + " where o.id = :id and status = 'active';";
        String GET_DELETED_BY_ID = "select o.id as id, t.name as type, o.name as name"
                + " from organizations o left join org_types t on o.type = t.id"
                + " where o.id = :id and status = 'deleted';";
        String DELETE = "update organizations set status = 'deleted' where id = :id;";
        String RESTORE = "update organizations set status = 'active' where id = :id;";
        String UPDATE_NAME = "update organizations set status = 'active', name = :name"
                + " where id = :id;";
        String GET_NAME_BY_ID = "select name from organizations where id = :id;";
    }
}
