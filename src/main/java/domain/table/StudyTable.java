package domain.table;

import domain.user.Users;
import dto.UserResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StudyTable {

    private static final String STUDY_STATUS_TABLE_TITLE = "## \uD83D\uDCBB스터디 상황표";

    private static final Pattern NAME_PATTERN = Pattern.compile("\\[(.*?)\\]");

    private final List<String> informations;
    private final int studyStatusTableIndex;

    public StudyTable(final List<String> informations) {
        Objects.requireNonNull(informations);
        this.informations = new ArrayList<>(informations);
        checkTitleContain(this.informations);
        this.studyStatusTableIndex = getStudyStatusTableIndex(this.informations);
    }

    private void checkTitleContain(final List<String> informations) {
        if (!informations.contains(STUDY_STATUS_TABLE_TITLE)) {
            throw new IllegalArgumentException("스터디 상황표가 존재해야한다.");
        }
    }

    private int getStudyStatusTableIndex(final List<String> informations) {
        for (int i = 0; i < informations.size(); i++) {
            if (informations.get(i).equals(STUDY_STATUS_TABLE_TITLE)) {
                return i;
            }
        }
        throw new IllegalArgumentException("스터디 상황표가 존재해야한다.");
    }

    public List<UserResponse> createUsers() {
        for (int i = 0; i < informations.size(); i++) {
            final String information = informations.get(i);
            if (information.equals(STUDY_STATUS_TABLE_TITLE)) {
                return informationToUsers(i);
            }
        }
        throw new IllegalStateException("유저 정보를 생성할 수 없습니다.");
    }

    private List<UserResponse> informationToUsers(final int i) {
        final AtomicInteger index = new AtomicInteger();
        return Arrays.stream(informations.get(i + 1).split("\\|"))
                .filter(value -> !value.isBlank())
                .map(value -> UserResponse.of(extractName(value), index.getAndIncrement() + 2))
                .collect(Collectors.toList());
    }

    private String extractName(final String nameFormat) {
        final Matcher matcher = NAME_PATTERN.matcher(nameFormat);

        while (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalStateException("이름이 존재하지 않습니다.");
    }

    public void refreshStudy(final Users users) {
        final LocalDate today = LocalDate.now();
        final LocalDate yesterday = today.minusDays(1);

        refreshInformations(users, today);
        refreshInformations(users, yesterday);
    }

    private void refreshInformations(final Users users, final LocalDate today) {
        int index = findDateIndex(today);
        String[] values = informations.get(index).split("\\|", -1);
        for (int i = 2; i < values.length; i++) {
            if (values[i].contains("✅") || !users.isDoneUser(today, i)) {
                continue;
            }
            values[i] = "✅";
            break;
        }
        String result = Arrays.stream(values)
                .collect(Collectors.joining("|"));
        informations.remove(index);
        informations.add(index, result);
    }

    private int findDateIndex(final LocalDate date) {
        final String dateFormat = convertToDateFormat(date);
        for (int i = studyStatusTableIndex; i < informations.size(); i++) {
            if (informations.get(i).contains(dateFormat)) {
                return i;
            }
        }
        throw new IllegalArgumentException("없는 날짜는 입력할 수 없다.");
    }

    private String convertToDateFormat(final LocalDate date) {
        return String.format("%s/%s", date.getMonthValue(), date.getDayOfMonth());
    }

    public String informations() {
        return String.join("\n", informations);
    }

    public void trimAllInformation() {
        for (int i=studyStatusTableIndex+1; i<informations.size(); i++){
            String test = informations.get(i).replace(" ", "");
            informations.remove(i);
            informations.add(i, test);
        }
    }

    public List<String> getInformations() {
        return informations;
    }

    public int getStudyStatusTableIndex() {
        return studyStatusTableIndex;
    }
}
