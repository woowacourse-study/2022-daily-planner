package domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class WeekTableTest {

    @Test
    void 주간_일정_날짜_포멧_예외_처리() {
        final String title = "2을28일-3월6일.md";
        final String body = "todos";

        assertThatThrownBy(() -> WeekTable.from(title, body))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포맷 이슈");
    }

    @Test
    void 주간_일정을_받아_테이블_생성() {
        final String title = "2월28일-3월6일.md";
        final String body = "todos";

        WeekTable weekTable = WeekTable.from(title, body);
        assertAll(
                () -> assertThat(weekTable.getStartAt()).isEqualTo(LocalDate.of(2022, 2, 28)),
                () -> assertThat(weekTable.getEndAt()).isEqualTo(LocalDate.of(2022, 3, 6))
        );
    }

    @Test
    void 물결_포맷을_기존_포맷으로_변경() {
        final String title = "2월28일~3월6일.md";
        final String body = "todos";
        WeekTable weekTable = WeekTable.from(title, body);

        assertThat(weekTable.getTitle()).isEqualTo("2월28일~3월6일.md");
    }

    @Test
    void 물결_포맷도_검증_가능하다() {
        final String title = "2월28일~3월6일 .md";
        assertThat(WeekTable.isWeekTable(title)).isTrue();
    }

    @Test
    void todo를_받아서_데일리플랜을_저장한다() {
        final String title = "3월7일~3월13일.md";
        final String todos = "# \uD83D\uDC2F 3월 7일 ~ 3월 13일\n"
                + "\n"
                + "## 3월 7일 (월)\n"
                + "\n"
                + "- [x] 로또 2단계 리뷰 피드백\n"
                + "- [x] 자동차 경주 미션 2단계 피드백 강의 듣기\n"
                + "\n"
                + "## 3월 8일 (화)\n"
                + "\n"
                + "- [ ] 10시 30분 강의\n"
                + "- [ ] 유튜브 미션\n"
                + "\n"
                + "## 3월 9일 (수)\n"
                + "\n"
                + "- [X] 10시 30분 강의\n"
                + "- [X] 유튜브 미션\n";

        final WeekTable weekTable = WeekTable.from(title, todos);
        assertAll(
                () -> assertThat(weekTable.getDailyPlans()).hasSize(3),
                () -> assertThat(weekTable.getDailyPlans().get(0).isAllPlanDone()).isTrue(),
                () -> assertThat(weekTable.getDailyPlans().get(1).isAllPlanDone()).isFalse(),
                () -> assertThat(weekTable.getDailyPlans().get(2).isAllPlanDone()).isTrue()
        );
    }

    @Test
    void 입력받은_날짜가_주간테이블에_포함되는지_확인한다() {
        final String title = "3월7일~3월13일.md";
        final String todos = "# \uD83D\uDC2F 3월 7일 ~ 3월 13일\n"
                + "\n"
                + "## 3월 7일 (월)\n"
                + "\n"
                + "- [x] 로또 2단계 리뷰 피드백\n"
                + "- [x] 자동차 경주 미션 2단계 피드백 강의 듣기\n"
                + "\n"
                + "## 3월 8일 (화)\n"
                + "\n"
                + "- [x] 10시 30분 강의\n"
                + "- [ ] 유튜브 미션\n";
        final WeekTable weekTable = WeekTable.from(title, todos);

        final LocalDate compareDate = LocalDate.of(2022, 3, 7);
        assertThat(weekTable.isContainPlan(compareDate)).isTrue();
    }

    @Test
    void 입력받은_날짜가_주간테이블에서_완료되었는지_확인한다() {
        final String title = "3월7일~3월13일.md";
        final String todos = "# \uD83D\uDC2F 3월 7일 ~ 3월 13일\n"
                + "\n"
                + "## 3월 7일 (월)\n"
                + "\n"
                + "- [x] 로또 2단계 리뷰 피드백\n"
                + "- [x] 자동차 경주 미션 2단계 피드백 강의 듣기\n"
                + "\n"
                + "## 3월 8일 (화)\n"
                + "\n"
                + "- [x] 10시 30분 강의\n"
                + "- [ ] 유튜브 미션\n";
        final WeekTable weekTable = WeekTable.from(title, todos);
        assertAll(
                () -> assertThat(weekTable.isDonePlan(LocalDate.of(2022, 3, 7))).isTrue(),
                () -> assertThat(weekTable.isDonePlan(LocalDate.of(2022, 3, 8))).isFalse()
        );
    }
}
