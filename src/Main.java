package src;

import src.Vehicle;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static src.CommandInterpreter.*;

/**
 * Основной класс программы
 */
public class Main {
    private static String filename;
    /**
     * @param args Аргументы командной строки
     *             Основной цикл программы
     */
    public static void main(String[] args) {
        String filename;
        if (args != null){
            Map<String, String> env = System.getenv();
            filename = System.getenv("FILENAME");
        }
        else {
            System.out.println("Отсутствует имя файла, введите вручную");
            Scanner scanner = new Scanner(System.in);
            filename = scanner.nextLine().trim();
        }
        setFilename(filename);
        load();
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        try(scanner) {
            while (true) {
                System.out.println("Введите команду:");
                try {
                    String command = scanner.readLine().trim();
                    commandHandler(command, scanner);
                } catch (Exception e) {
                    handleError(e);
                }
            }
        }
        catch (Exception e) {
            handleError(e);
        }
    }

}