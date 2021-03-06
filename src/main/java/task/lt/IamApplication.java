package task.lt;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import task.lt.db.EmploymentDao;
import task.lt.db.OrgTypesDao;
import task.lt.db.OrgTypesInitializer;
import task.lt.db.OrganizationsDao;
import task.lt.db.SessionsDao;
import task.lt.db.UsersDao;
import task.lt.resources.OrganizationsResource;
import task.lt.resources.SessionsResource;
import task.lt.resources.UsersResource;

public class IamApplication extends Application<AppConfiguration> {

    public static void main(String... args) throws Exception {
        new IamApplication().run(args);
    }

    @Override
    public String getName() {
        return "Identity and Access Management";
    }

    @Override
    public void run(AppConfiguration config, Environment env) {
        DBI dbi = new DBIFactory().build(env, config.getDataSourceFactory(), "h2");

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

        if (orgTypesDao.getAll().isEmpty()) {
            new OrgTypesInitializer(orgTypesDao).populateOrgTypes();
        }

        env.jersey().register(new OrganizationsResource(organizationsDao));
        env.jersey().register(new UsersResource(usersDao, organizationsDao, employmentDao));
        env.jersey().register(new SessionsResource(usersDao, sessionsDao));
    }

}
