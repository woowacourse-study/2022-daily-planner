package domain.user;

import domain.table.WeekTable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class User {

    private static final String BLANK = " ";

    private final String name;
    private final int tableIndex;
    private final List<WeekTable> weekTables;

    public User(final String name, final int tableIndex, final List<WeekTable> weekTables) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(weekTables);
        checkNameInBlank(name);
        checkTableIndex(tableIndex);
        this.name = name;
        this.tableIndex = tableIndex;
        this.weekTables = weekTables;
    }

    private void checkNameInBlank(final String name) {
        if (name.contains(BLANK)) {
            throw new IllegalArgumentException("이름에 공백이 들어올 수 없다.");
        }
    }

    private void checkTableIndex(final int tableIndex) {
        if (tableIndex <= 0) {
            throw new IllegalArgumentException("인덱스는 1이상의 값이어야 한다.");
        }
    }

    public boolean isMyTableIndex(final int tableIndex) {
        return this.tableIndex == tableIndex;
    }

    public boolean isDonePlan(final LocalDate localDate, final int tableIndex) {
        if (this.tableIndex != tableIndex) {
            return false;
        }
        return weekTables.stream()
                .filter(weekTable -> weekTable.isContainPlan(localDate))
                .map(weekTable -> weekTable.isDonePlan(localDate))
                .findFirst()
                .orElse(false);
    }

    public String getName() {
        return name;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User user = (User) o;
        return tableIndex == user.tableIndex && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tableIndex);
    }
}
