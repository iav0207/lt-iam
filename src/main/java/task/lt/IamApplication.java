package task.lt;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

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
    }

}
