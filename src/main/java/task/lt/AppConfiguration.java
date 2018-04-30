package task.lt;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

class AppConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private final DataSourceFactory database = new DataSourceFactory();

    DataSourceFactory getDataSourceFactory() {
        return database;
    }
}