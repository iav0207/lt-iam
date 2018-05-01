package task.lt.db;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import task.lt.api.model.EmploymentItem;
import task.lt.api.model.User;
import task.lt.core.pass.PasswordDigest;

import static org.assertj.core.api.Assertions.assertThat;
import static task.lt.api.model.Organization.Type.CLIENT;

@ParametersAreNonnullByDefault
public class EmploymentDaoTest {

    private OrganizationsDao organizationsDao;
    private UsersDao usersDao;
    private EmploymentDao dao;

    @BeforeClass
    public void init() {
        organizationsDao = TestDbInitializer.getDbi().onDemand(OrganizationsDao.class);
        usersDao = TestDbInitializer.getDbi().onDemand(UsersDao.class);
        dao = TestDbInitializer.getDbi().onDemand(EmploymentDao.class);
    }

    @Test
    public void addAndGet() {
        long org = organizationsDao.add("org", CLIENT.toString());
        long user = usersDao.add(User.builder()
                        .withEmail("email@mail.com")
                        .withFirstName("fn")
                        .withLastName("ln")
                        .withGender(User.Gender.FEMALE)
                        .build(),
                PasswordDigest.hash("pass"));

        dao.add(user, org, "CEO");

        List<EmploymentItem> employment = dao.getByUser(user);
        assertThat(employment).hasSize(1);

        EmploymentItem item = employment.get(0);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(item.getJobTitle()).isEqualTo("CEO");
        softly.assertThat(item.getOrganization().getName()).isEqualTo("org");
        softly.assertThat(item.getOrganization().getType()).isEqualTo(CLIENT);
        softly.assertAll();
    }

}
