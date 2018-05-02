package task.lt.resources;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.ParametersAreNonnullByDefault;

import task.lt.api.model.User;
import task.lt.api.model.UserWithPassword;

import static task.lt.api.model.User.Gender.FEMALE;
import static task.lt.api.model.User.Gender.MALE;

@ParametersAreNonnullByDefault
class TestUsersGenerator {

    private static final AtomicInteger increment = new AtomicInteger();

    UserWithPassword generateUser() {
        final int i = increment.getAndIncrement();
        return new UserWithPassword("password-" + i,
                User.builder()
                        .withEmail(String.format("john.doe-%d@example.com", i))
                        .withGender(i % 2 == 0 ? MALE : FEMALE)
                        .withFirstName("John" + i)
                        .withLastName("Doe" + i));
    }
}
