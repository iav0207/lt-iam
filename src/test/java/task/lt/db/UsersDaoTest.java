package task.lt.db;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import task.lt.api.model.User;
import task.lt.api.model.UserWithPassword;

import static org.assertj.core.api.Assertions.assertThat;
import static task.lt.api.model.User.Gender.FEMALE;
import static task.lt.api.model.User.Gender.MALE;

@ParametersAreNonnullByDefault
public class UsersDaoTest {

    private UsersDao dao;

    @BeforeClass
    public void init() {
        dao = TestDbInitializer.getDbi().onDemand(UsersDao.class);
    }

    @Test
    public void testAddAndGetById() {
        long id = dao.add(generateUser("testAdd"));
        assertThat(dao.getById(id)).isNotNull();
        assertThat(dao.getById(id + 100)).isNull();
    }

    @Test
    public void testCheckCredentials() {
        UserWithPassword user = generateUser("testCheckCredentials");
        assertThat(dao.checkCredentials(user.getEmail(), user.getPassword())).isFalse();
        dao.add(user);
        assertThat(dao.checkCredentials(user.getEmail(), user.getPassword())).isTrue();
        assertThat(dao.checkCredentials(user.getEmail() + "a", user.getPassword())).isFalse();
        assertThat(dao.checkCredentials(user.getEmail(), user.getPassword() + "a")).isFalse();
    }

    private static UserWithPassword generateUser(String testCaseName) {
        return new UserWithPassword("pass-" + testCaseName,
                User.builder()
                        .withEmail("email-" + testCaseName)
                        .withGender(RandomUtils.nextBoolean() ? MALE : FEMALE)
                        .withFirstName("first-" + testCaseName)
                        .withLastName("last-" + testCaseName));
    }

}
