package domain.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Users {

    private final List<User> users;

    public Users(final List<User> users) {
        Objects.requireNonNull(users);
        this.users = new ArrayList<>(users);
    }

    public boolean isDoneUser(final LocalDate localDate, final int tableIndex) {
        return users.stream()
                .filter(user -> user.isMyTableIndex(tableIndex))
                .map(user -> user.isDonePlan(localDate, tableIndex))
                .findFirst()
                .orElse(false);
    }

    public List<User> users() {
        return List.copyOf(users);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Users users1 = (Users) o;
        return Objects.equals(users, users1.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }
}
