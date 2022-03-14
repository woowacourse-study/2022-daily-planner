package domain.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void 이름은_null이_올_수_없다() {
        assertThatThrownBy(() -> new User(null, 2, new ArrayList<>()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 이름은_공백이_들어올_수_없다() {
        assertThatThrownBy(() -> new User("u s e r", 2, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주간_일정은_null이_올_수_없다() {
        assertThatThrownBy(() -> new User("ori", 2, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 인덱스는_1이상_값이_와야한다() {
        assertThatThrownBy(() -> new User("user", 0, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
