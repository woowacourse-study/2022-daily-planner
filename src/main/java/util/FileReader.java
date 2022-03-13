package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class FileReader {

    private static final String FILE_INPUT_ERROR_MESSAGE = "파일 입력이 에러가 발생했습니다.";
    private static final String READ_ME_FILE_PATH = "./README.md";
    private static final String USER_FORDER_FILE_PATH = "./";

    private FileReader() {
        throw new AssertionError();
    }

    public static List<String> inputReadme() {
        try {
            final File file = new File(READ_ME_FILE_PATH);
            final Scanner scanner = new Scanner(file);
            final List<String> crews = new ArrayList<>();
            while (scanner.hasNextLine()) {
                crews.add(scanner.nextLine());
            }
            return crews;
        } catch (FileNotFoundException e) {
            System.out.println(FILE_INPUT_ERROR_MESSAGE);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<File> userDailyForder(final String name) {
        final String userForderPath = String.format("%s%s", USER_FORDER_FILE_PATH, name);
        try {
            final File[] files = new File(userForderPath).listFiles();
            return Arrays.asList(Objects.requireNonNull(files));
        } catch (NullPointerException e) {
            System.out.println(FILE_INPUT_ERROR_MESSAGE);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String convertFileSource(final File file) {
        try {
            final StringBuilder sb = new StringBuilder();
            final Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            System.out.println(FILE_INPUT_ERROR_MESSAGE);
            throw new RuntimeException(e.getMessage());
        }
    }
}
