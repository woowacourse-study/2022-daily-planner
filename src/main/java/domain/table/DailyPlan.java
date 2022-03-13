package domain.table;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyPlan {

    private static final Pattern DAILY_FILE_TITLE_NUMBER_PATTERN = Pattern.compile("\\d{1,2}");
    private static final String NOT_END_PLAN_FORMAT = "- [ ]";

    private final LocalDate nowDate;
    private final String todos;

    private DailyPlan(final LocalDate nowDate, final String todos) {
        this.nowDate = nowDate;
        this.todos = todos;
    }

    public static DailyPlan from(final String dailyTable) {
        final String todos = dailyTable.trim().replaceAll("#", "");

        final Matcher matcher = DAILY_FILE_TITLE_NUMBER_PATTERN.matcher(todos);
        try {
            matcher.find();
            final int month = Integer.parseInt(matcher.group());
            matcher.find();
            final int dayOfMonth = Integer.parseInt(matcher.group());
            return new DailyPlan(LocalDate.of(2022, month, dayOfMonth), todos);
        } catch (RuntimeException e) {
            throw new RuntimeException("데일리 일정 포맷이 지켜지지 않는다.");
        }
    }

    public boolean isAllPlanDone() {
        return !todos.contains(NOT_END_PLAN_FORMAT);
    }

    public boolean isNowDailyDate(final LocalDate localDate) {
        return nowDate.isEqual(localDate);
    }

    public LocalDate getNowDate() {
        return nowDate;
    }

    public String getTodos() {
        return todos;
    }

    @Override
    public String toString() {
        return "DailyPlan{" +
                "nowDate=" + nowDate +
                ", todos='" + todos + '\'' +
                '}';
    }
}
