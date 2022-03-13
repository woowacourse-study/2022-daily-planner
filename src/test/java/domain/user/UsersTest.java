package domain.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class UsersTest {

    @Test
    void 유저_리스트는_null이_들어올_수_없다() {
        assertThatThrownBy(() -> new Users(null))
                .isInstanceOf(NullPointerException.class);
    }
}
