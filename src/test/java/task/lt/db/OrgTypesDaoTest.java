package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.DBI;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@ParametersAreNonnullByDefault
public class OrgTypesDaoTest {

    private OrgTypesDao orgTypes;

    @BeforeClass
    public void init() {
        DBI dbi = TestDbInitializer.getDbi();
        orgTypes = dbi.onDemand(OrgTypesDao.class);
    }

    @Test
    public void shouldReturnRecords() {
        assertThat(orgTypes.getAll()).isNotEmpty();
    }

    @Test
    public void exists_positive() {
        String existentTypeName = orgTypes.getAll().iterator().next();
        assertThat(orgTypes.exists(existentTypeName)).isTrue();
    }

    @Test
    public void exists_negative() {
        String nonExistentTypeName = "test-shouldDetermineExistence";
        assertThat(orgTypes.exists(nonExistentTypeName)).isFalse();
    }

    @Test
    public void shouldAddRecords() {
        String name = "test-shouldAddRecords";
        assumeThat(orgTypes.exists(name)).isFalse();
        orgTypes.add(name);
        assertThat(orgTypes.exists(name)).isTrue();
    }

}
