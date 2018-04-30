package task.lt;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class IamAppApplication extends Application<IamAppConfiguration> {

    public static void main(final String[] args) throws Exception {
        new IamAppApplication().run(args);
    }

    @Override
    public String getName() {
        return "IamApp";
    }

    @Override
    public void initialize(final Bootstrap<IamAppConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final IamAppConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
