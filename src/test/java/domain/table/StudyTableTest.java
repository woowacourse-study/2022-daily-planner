package domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import domain.user.User;
import domain.user.Users;
import dto.UserResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class StudyTableTest {

    @Test
    void 테이블_생성_시_null이_들어오면_예외_발생() {
        assertThatThrownBy(() -> new StudyTable(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 정확한_스터디_상황표가_없으면_예외_발생() {
        final List<String> informations = Arrays.asList("테스트", "## \uD83D\uDCBB스터디 상황표1");
        assertThatThrownBy(() -> new StudyTable(informations))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 스터디_상황표_생성() {
        final List<String> informations = Arrays.asList("테스트", "## \uD83D\uDCBB스터디 상황표");
        assertThat(new StudyTable(informations)).isInstanceOf(StudyTable.class);
    }

    @Test
    void 스터디_상황표에서_스터디원을_가져올_수_있다() {
        // given
        final List<String> informations = Arrays.asList("## \uD83D\uDCBB스터디 상황표",
                "||[민초](https://github.com/jswith)|[아서](https://github.com/Hyunta)|[앨버](https://github.com/al-bur)|[에덴](https://github.com/leo0842)|[오리](https://github.com/jinyoungchoi95)|[잉](https://github.com/Yboyu0u)|[티거](https://github.com/daaaayeah)|[코린](https://github.com/hamcheeseburger)|[파랑](https://github.com/summerlunaa)|");
        final StudyTable studyTable = new StudyTable(informations);

        final List<UserResponse> expected = Arrays.asList(UserResponse.of("민초", 2),
                UserResponse.of("아서", 3),
                UserResponse.of("앨버", 4),
                UserResponse.of("에덴", 5),
                UserResponse.of("오리", 6),
                UserResponse.of("잉", 7),
                UserResponse.of("티거", 8),
                UserResponse.of("코린", 9),
                UserResponse.of("파랑", 10)
        );

        // when
        final List<UserResponse> result = studyTable.createUsers();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void 스터디_이름이_잘못_입력되어있으면_예외_발생() {
        final List<String> informations = Arrays.asList("## \uD83D\uDCBB스터디 상황표",
                "||민초(https://github.com/jswith)|[아서](https://github.com/Hyunta)|[앨버](https://github.com/al-bur)|[에덴](https://github.com/leo0842)|[오리](https://github.com/jinyoungchoi95)|[잉](https://github.com/Yboyu0u)|[티거](https://github.com/daaaayeah)|[코린](https://github.com/hamcheeseburger)|[파랑](https://github.com/summerlunaa)|");
        final StudyTable studyTable = new StudyTable(informations);
        assertThatThrownBy(() -> studyTable.createUsers())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이름이 존재하지 않습니다.");
    }

    @Test
    void 스터디_빈_상황표에_유저의_계획완료를_업데이트한다() {
        final LocalDate today = LocalDate.now();
        final LocalDate yesterday = today.minusDays(1);
        final LocalDate firstDay = today.minusDays(3);
        final LocalDate lastDay = today.plusDays(3);
        final List<String> informations = Arrays.asList(
                "## \uD83D\uDCBB스터디 상황표",
                "||민초(https://github.com/jswith)|[아서](https://github.com/Hyunta)",
                "|" + yesterday.getMonthValue() +"/" + yesterday.getDayOfMonth() + "||",
                "|" + today.getMonthValue() +"/" + today.getDayOfMonth() + "(월)||",
                "|3/15||",
                "|3/16||");
        final StudyTable studyTable = new StudyTable(informations);

        final String title = convertToTitle(firstDay, lastDay) + ".md";
        final String todos = "# \uD83D\uDC2F " + convertToTitle(firstDay, lastDay) + "\n"
                + "\n"
                + "## " + yesterday.getMonthValue() +"월 " + yesterday.getDayOfMonth() + "일 (월)\n"
                + "\n"
                + "- [x] 10시 30분 강의\n"
                + "- [x] 유튜브 미션\n"
                + "\n"
                + "## " + today.getMonthValue() +"월 " + today.getDayOfMonth() + "일 (월)\n"
                + "\n"
                + "- [x] 로또 2단계 리뷰 피드백\n"
                + "- [ ] 자동차 경주 미션 2단계 피드백 강의 듣기\n"
                + "\n";
        final User user = new User("민초", 2, Arrays.asList(WeekTable.from(title, todos)));
        studyTable.refreshStudy(new Users(Arrays.asList(user)));

        assertThat(studyTable.getInformations()).contains("|" + yesterday.getMonthValue() +"/" + yesterday.getDayOfMonth() + "|✅|");
        assertThat(studyTable.getInformations()).contains("|" + today.getMonthValue() +"/" + today.getDayOfMonth() + "(월)||");
    }

    private String convertToTitle(final LocalDate firstDay, final LocalDate lastDay) {
        return String.format("%s월%s일~%s월%s일", firstDay.getMonthValue(), firstDay.getDayOfMonth(),
                lastDay.getMonthValue(), lastDay.getDayOfMonth());
    }
}
