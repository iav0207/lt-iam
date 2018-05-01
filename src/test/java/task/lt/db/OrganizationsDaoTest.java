package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.skife.jdbi.v2.DBI;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import task.lt.api.model.Organization;

import static org.assertj.core.api.Assertions.assertThat;

@ParametersAreNonnullByDefault
public class OrganizationsDaoTest {

    private OrganizationsDao organizations;
    private String[] types;

    @BeforeClass
    public void init() {
        DBI dbi = TestDbInitializer.getDbi();
        organizations = dbi.onDemand(OrganizationsDao.class);
        types = dbi.onDemand(OrgTypesDao.class).getAll().toArray(new String[0]);
    }

    @Test
    public void shouldReturnAddedRecordById() {
        String name = "test-shouldReturnAddedRecordById";
        long id = organizations.add(name, types[0]);
        Organization org = organizations.getById(id);

        assertThat(org).isNotNull();
        assertThat(org.getName()).isEqualTo(name);
    }

}
