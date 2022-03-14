package domain.table;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeekTable {

    private static final Pattern WEEKLY_FILE_TITLE_PATTERN = Pattern.compile("^[0-9]*월[0-9]*일-[0-9]*월[0-9].*$");
    private static final Pattern WEEKLY_FILE_TITLE_NUMBER_PATTERN = Pattern.compile("\\d{1,2}");

    private static final String WAVE_MESSAGE = "~";
    private static final String HYPHEN_MESSAGE = "-";

    private static final int THIS_YEAR = 2022;

    private final LocalDate startAt;
    private final LocalDate endAt;
    private final String title;
    private final List<DailyPlan> dailyPlans;

    public WeekTable(final LocalDate startAt, final LocalDate endAt, final String title, final List<DailyPlan> dailyPlans) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.title = title;
        this.dailyPlans = dailyPlans;
    }

    public static WeekTable from(final String title, final String todos) {
        final String newTitle = formatingTitle(title);
        if (!WEEKLY_FILE_TITLE_PATTERN.matcher(newTitle).find()) {
            throw new IllegalArgumentException("포맷 이슈");
        }
        final Matcher matcher = WEEKLY_FILE_TITLE_NUMBER_PATTERN.matcher(newTitle);
        return new WeekTable(findDate(matcher), findDate(matcher), title, toDailPlans(todos));
    }

    private static List<DailyPlan> toDailPlans(final String todos) {
        return Stream.of(removeDuplicate(todos).split("#"))
                .filter(value -> value.contains("- [ ]") || value.contains("- [x]") || value.contains("- [X]"))
                .map(DailyPlan::from)
                .collect(Collectors.toList());
    }

    private static String removeDuplicate(final String input) {
        return input.replace("##", "#")
                .replace("###", "#")
                .replace("####", "#");
    }

    private static LocalDate findDate(final Matcher matcher) {
        matcher.find();
        int month = Integer.parseInt(matcher.group());
        matcher.find();
        int day = Integer.parseInt(matcher.group());
        return LocalDate.of(THIS_YEAR, month, day);
    }

    public static boolean isWeekTable(final String title) {
        return WEEKLY_FILE_TITLE_PATTERN.matcher(formatingTitle(title)).find();
    }

    private static String formatingTitle(final String title) {
        return title.replace(WAVE_MESSAGE, HYPHEN_MESSAGE)
                .replace(" ", "");
    }

    public boolean isContainPlan(final LocalDate localDate) {
        return startAt.isBefore(localDate.plusDays(1)) && endAt.isAfter(localDate.minusDays(1));
    }

    public boolean isDonePlan(final LocalDate localDate) {
        return dailyPlans.stream()
                .filter(dailyPlan -> dailyPlan.isNowDailyDate(localDate))
                .map(DailyPlan::isAllPlanDone)
                .findFirst()
                .orElse(false);
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public LocalDate getEndAt() {
        return endAt;
    }

    public String getTitle() {
        return title;
    }

    public List<DailyPlan> getDailyPlans() {
        return dailyPlans;
    }

    @Override
    public String toString() {
        return "WeekTable{" +
                "startAt=" + startAt +
                ", endAt=" + endAt +
                ", title='" + title + '\'' +
                ", dailyPlans=" + dailyPlans +
                '}';
    }
}
