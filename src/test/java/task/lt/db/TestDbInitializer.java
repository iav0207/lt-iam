package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

@ParametersAreNonnullByDefault
public class TestDbInitializer {

    private static final String DRIVER = "org.h2.Driver";
    private static final String URL = "jdbc:h2:mem:testDb";

    private static final Environment env = new Environment("test-env", Jackson.newObjectMapper(),
            null, new MetricRegistry(), null);

    private static final DataSourceFactory dataSourceFactory = createDataSourceFactory();

    private static final DBI dbi = new DBIFactory().build(env, dataSourceFactory, "test");

    static {
        OrgTypesDao orgTypesDao = dbi.onDemand(OrgTypesDao.class);
        OrganizationsDao organizationsDao = dbi.onDemand(OrganizationsDao.class);
        UsersDao usersDao = dbi.onDemand(UsersDao.class);
        EmploymentDao employmentDao = dbi.onDemand(EmploymentDao.class);
        SessionsDao sessionsDao = dbi.onDemand(SessionsDao.class);

        orgTypesDao.createTableIfNotExists();
        organizationsDao.createTableIfNotExists();
        usersDao.createTableIfNotExists();
        employmentDao.createTableIfNotExists();
        sessionsDao.createTableIfNotExists();

        new OrgTypesInitializer(orgTypesDao).populateOrgTypes();

        dbi.registerMapper(new OrganizationMapper());
        dbi.registerMapper(new UserMapper());
    }

    private static DataSourceFactory createDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass(DRIVER);
        dataSourceFactory.setUrl(URL);
        return dataSourceFactory;
    }

    public static DBI getDbi() {
        return dbi;
    }

    private TestDbInitializer() {
        // no instantiation
    }
}
