import domain.table.StudyTable;
import domain.table.WeekTable;
import domain.user.User;
import domain.user.Users;
import dto.UserResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import util.FileReader;

public class Application {

    public static void main(String[] args) {
        final StudyTable studyTable = new StudyTable(FileReader.inputReadme());
        final List<UserResponse> userResponses = studyTable.createUsers();
        final Users users = new Users(userResponses
                .stream()
                .map(Application::createUser)
                .collect(Collectors.toList()));
        studyTable.refreshStudy(users);
        try (FileWriter fw = new FileWriter("./README.md")) {
            fw.write(studyTable.informations());
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    private static User createUser(final UserResponse response) {
        return new User(response.getName(), response.getTableIndex(),
                weeekTables(FileReader.userDailyForder(response.getName())));
    }

    private static List<WeekTable> weeekTables(final List<File> files) {
        return files.stream()
                .filter(file -> WeekTable.isWeekTable(file.getName()))
                .map(file -> WeekTable.from(file.getName(), FileReader.convertFileSource(file)))
                .collect(Collectors.toList());
    }
}
