package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.assertj.core.api.SoftAssertions;
import org.skife.jdbi.v2.DBI;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import task.lt.api.model.Organization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@ParametersAreNonnullByDefault
public class OrganizationsDaoTest {

    private OrganizationsDao dao;
    private String[] types;

    @BeforeClass
    public void init() {
        DBI dbi = TestDbInitializer.getDbi();
        dao = dbi.onDemand(OrganizationsDao.class);
        types = dbi.onDemand(OrgTypesDao.class).getAll().toArray(new String[0]);
    }

    @Test
    public void shouldReturnAddedRecordById() {
        final String name = "test-shouldReturnAddedRecordById";
        assumeThat(dao.exists(name)).isFalse();

        long id = dao.add(name, types[0]);
        Organization org = dao.getById(id);

        assertThat(org).isNotNull();
        assertThat(org.getName()).isEqualTo(name);
        assertThat(dao.exists(name)).isTrue();
    }

    @Test
    public void shouldNotReturnDeletedOrg() {
        final String name = "test-shouldNotReturnDeletedOrg";
        assumeThat(dao.exists(name)).isFalse();

        long id = dao.add(name, types[0]);
        assumeThat(dao.delete(id)).isEqualTo(1);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(dao.exists(id)).isTrue();
        softly.assertThat(dao.existsAndActive(id)).isFalse();
        softly.assertAll();

        assertThat(dao.getById(id)).isNull();
    }

    @Test
    public void shouldReturnRestoredOrg() {
        final String name = "test-shouldReturnRestoredOrg";
        assumeThat(dao.exists(name)).isFalse();

        long id = dao.add(name, types[0]);
        assumeThat(dao.delete(id)).isEqualTo(1);
        assumeThat(dao.restore(id)).isEqualTo(1);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(dao.exists(id)).isTrue();
        softly.assertThat(dao.existsAndActive(id)).isTrue();
        softly.assertAll();

        assertThat(dao.getById(id)).isNotNull();
    }
}
