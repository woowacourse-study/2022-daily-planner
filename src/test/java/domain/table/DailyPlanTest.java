package domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DailyPlanTest {

    @Test
    void 계획의_현재_날짜를_저장한다() {
        final String todos = "## 3월 7일\n"
                + "\n"
                + "- [x] 아이템 12, 51, 53 읽기\n"
                + "- [x] 리뷰 염탐하고 정리하기\n";
        final DailyPlan dailyPlan = DailyPlan.from(todos);
        assertAll(
                () -> assertThat(dailyPlan.getNowDate()).isEqualTo(LocalDate.of(2022, 3, 7)),
                () -> assertThat(dailyPlan.getTodos()).isEqualTo(todos.trim().replaceAll("#", ""))
        );
    }

    @Test
    void 잘못된_포맷_투두_예외_처리() {
        assertThatThrownBy(() -> DailyPlan.from("##3월-[x]잠자기"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("데일리 일정 포맷이 지켜지지 않는다.");
    }

    @Test
    void 모든_일정이_완료됨을_확인한다() {
        final String todos = "## 3월 7일\n"
                + "\n"
                + "- [x] 아이템 12, 51, 53 읽기\n"
                + "- [x] 리뷰 염탐하고 정리하기\n";
        final DailyPlan dailyPlan = DailyPlan.from(todos);
        assertThat(dailyPlan.isAllPlanDone()).isTrue();
    }

    @Test
    void 일정이_완료되지_않으면_실패를_반환한다() {
        final String todos = "## 3월 7일\n"
                + "\n"
                + "- [x] 아이템 12, 51, 53 읽기\n"
                + "- [ ] 리뷰 염탐하고 정리하기\n";
        final DailyPlan dailyPlan = DailyPlan.from(todos);
        assertThat(dailyPlan.isAllPlanDone()).isFalse();
    }

    @Test
    void 입력받은_날짜가_데일리날짜와_같은지_확인한다() {
        final String todos = "## 3월 7일\n"
                + "\n"
                + "- [x] 아이템 12, 51, 53 읽기\n"
                + "- [ ] 리뷰 염탐하고 정리하기\n";
        final DailyPlan dailyPlan = DailyPlan.from(todos);
        assertThat(dailyPlan.isNowDailyDate(LocalDate.of(2022, 3, 7))).isTrue();
    }
}
