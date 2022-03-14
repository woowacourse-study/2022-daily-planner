package dto;

import java.util.Objects;

public class UserResponse {

    private final String name;
    private final int tableIndex;

    private UserResponse(final String name, final int tableIndex) {
        this.name = name;
        this.tableIndex = tableIndex;
    }

    public static UserResponse of(final String name, final int tableIndex) {
        return new UserResponse(
                name,
                tableIndex
        );
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
        final UserResponse that = (UserResponse) o;
        return tableIndex == that.tableIndex && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tableIndex);
    }
}
