package task.lt.db;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import task.lt.api.model.EmploymentItem;

import static task.lt.db.EmploymentDao.FieldNames.JOB_TITLE;
import static task.lt.db.EmploymentDao.FieldNames.ORG;
import static task.lt.db.EmploymentDao.FieldNames.USER;

@SuppressWarnings("squid:S1214")    // constants interface usage: acceptable as they're not public
@ParametersAreNonnullByDefault
public interface EmploymentDao {

    @SqlUpdate(SQL.CREATE_TABLE_IF_NOT_EXISTS)
    void createTableIfNotExists();

    @SqlUpdate(SQL.INSERT)
    void add(@Bind(USER) long userId,
            @Bind(ORG) long orgId,
            @Bind(JOB_TITLE) String jobTitle);

    @SqlQuery(SQL.GET_BY_USER)
    @Mapper(EmploymentMapper.class)
    List<EmploymentItem> getByUser(@Bind(USER) long userId);

    interface FieldNames {
        String USER = "user";
        String ORG = "org";
        String JOB_TITLE = "job_title";
    }

    interface SQL {
        String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists employment"
                + " (user bigint not null, foreign key (user) references users(id),"
                + " org bigint not null, foreign key (org) references organizations(id),"
                + " job_title varchar(30) not null,"
                + " primary key (user, org));";
        String INSERT = "insert into employment (user, org, job_title) values (:user, :org, :job_title);";
        String GET_BY_USER = "select o.id as org_id, o.name as org_name,"
                + " (select name from org_types where id = o.type) as org_type, job_title"
                + " from employment e join organizations o on e.org = o.id"
                + " where e.user = :user;";
    }
}
