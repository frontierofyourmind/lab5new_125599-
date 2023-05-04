package src;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Реализует исполнение команд, вводимых пользователем (с помощью консоли или скрипта)
 */
public class CommandInterpreter {
    private static TreeSet<Vehicle> vehicles = new TreeSet<>();
    private static LocalDateTime initDate;
    private static String filename;

    public static void setFilename(String filenameInput){
        filename = filenameInput;
    }
    public static String getFilename(){
        return filename;
    }
    /**
     * @param command Команда для обработки
     * @param stream Поток для считывания данных (консоль или файл)
     *               Используется для обработки команд и перенаправления в соответствующие методы
     */
    public static void commandHandler(String command, BufferedReader stream){
        String[] tokens = command.split("\\s+");
        try {
            switch (tokens[0]) {
                case "help":
                    help();
                    break;
                case "info":
                    info();
                    break;
                case "show":
                    show();
                    break;
                case "add":
                    add(stream);
                    break;
                case "update":
                    update(tokens[1], stream);
                    break;
                case "remove_by_id":
                    removeById(tokens[1]);
                    break;
                case "clear":
                    clear();
                    break;
                case "save":
                    save(getFilename());
                    break;
                case "execute_script":
                    executeScript(tokens[1]);
                    break;
                case "exit":
                    exit();
                    break;
                case "add_if_max":
                    addIfMax(stream);
                    break;
                case "add_if_min":
                    addIfMin(stream);
                    break;
                case "remove_lower":
                    removeLower(stream);
                    break;
                case "group_counting_by_engine_power":
                    groupCountingByEnginePower();
                    break;
                case "filter_by_number_of_wheels":
                    filterByNumberOfWheels(tokens[1]);
                    break;
                case "print_field_ascending_number_of_wheels":
                    printFieldAscendingNumberOfWheels();
                    break;
                default:
                    System.out.println("Неизвестная команда. Наберите help чтобы получить список доступных команд");
            }
        }
        catch (Exception e) {
            handleError(e);
        }
    }

    /**
     * Выводит справку по доступным командам
     */
    private static void help() {
        System.out.println("Доступные команды:");
        System.out.println("help - показать доступные команды");
        System.out.println("info - показать информацию о коллекции");
        System.out.println("show - показать все элементы");
        System.out.println("add {элемент} - добавить элемент в коллекцию");
        System.out.println("update id {элемент} - обновить элемент с заданным id");
        System.out.println("remove_by_id id - удалить элемент с заданным id");
        System.out.println("clear - удалить все элементы из коллекции");
        System.out.println("save - сохранить коллекцию в файл");
        System.out.println("execute_script file_name - выполнить команды из файла");
        System.out.println("exit - выйти из программы");
        System.out.println("add_if_max {элемент} - добавить элемент, если его значение больше максимального значения в коллекции");
        System.out.println("add_if_min {элемент} - добавить элемент, если его значение меньше минимального значения в коллекции");
        System.out.println("remove_lower {элемент} - удалить все элементы, которые меньше заданного элемента");
        System.out.println("group_counting_by_engine_power - сгруппировать элементы по мощности двигателя и показать их количество");
        System.out.println("filter_by_number_of_wheels numberOfWheels - показать элементы с заданным количеством колес");
        System.out.println("print_field_ascending_number_of_wheels - показать значения поля 'количество колес' в порядке возрастания");
    }

    /**
     * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементом
     */
    private static void info() {
        System.out.println("Collection type: " + vehicles.getClass().getName());
        System.out.println("Initialization date: " + initDate.toString());
        System.out.println("Number of elements: " + vehicles.size());
    }

    /**
     * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
     */
    private static void show() {
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    /**
     * @param stream Поток для считывания данных
     *               Добавляет новый элемент в коллекцию
     */
    public static void add(BufferedReader stream) {
        vehicles.add(Vehicle.fromUser(stream));
    }

    /**
     * @param idString id элемента, который надо обновить
     * @param stream Поток для считывания данных
     *               Обновляет значения элемента с данным id
     */
    public static void update(String idString, BufferedReader stream) {
        int id = Integer.parseInt(idString);
        for (Vehicle v : vehicles) {
            if (v.getId() == id) {
                vehicles.remove(v);
                vehicles.add(Vehicle.modifyFromUser(v, stream));
                break;
            }
        }
    }

    /**
     * @param idString id элемента, который надо удалить
     *                 Удаляет элемент с соответствующим id
     */
    private static void removeById(String idString) {
        long id = Long.parseLong(idString);
        Vehicle vehicle = vehicles.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
        if (vehicle == null) {
            throw new IllegalArgumentException("Element with given id not found.");
        }
        vehicles.remove(vehicle);
        System.out.println("Element removed.");
    }

    /**
     * Очищает коллекцию
     */
    private static void clear() {
        vehicles.clear();
        System.out.println("Collection cleared.");
    }

    /**
     * Возвращает коллекцию, загруженную из CSV-файла
     */
    public static void load(){
        String fileName = getFilename();
        TreeSet<Vehicle> vehicleCollection = new TreeSet<>();

        String currentLine;
        try (BufferedReader scanner = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))){
            try {
                initDate = LocalDateTime.parse(scanner.readLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            }
            catch (NullPointerException e){
                initDate = LocalDateTime.now();
            }
            while ((currentLine = scanner.readLine()) != null) {
                vehicleCollection.add(Vehicle.fromCSV(currentLine));
            }
        } catch (IOException e) {
            handleError(e);
        }
        for(Vehicle vehicle: vehicleCollection) {
            Vehicle.updateId(vehicle.getId());
        }
        vehicles = vehicleCollection;
    }

    /**
     * @param fileName Имя файла для CSV-дампа коллекции
     *                 Сохраняет текущее состояние коллекции в CSV-файл
     */
    private static void save(String fileName) {
        try (BufferedWriter printer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))){
            printer.write(initDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))+'\n');
            for (Vehicle vehicle: vehicles) {
                printer.write(vehicle.toCSV() + '\n');
            }
        }
        catch (FileNotFoundException e){
            handleError(e);
        }
        catch (IOException e){
            handleError(e);
        }
    }

    /**
     * @param fileName Имя файла со скриптом
     *                 Выполняет команды из файла как если бы они вводились в консоль
     */
    private static void executeScript(String fileName) {
        String currentLine;
        try {
            InputStream stream = new FileInputStream(fileName);
            BufferedReader scanner = new BufferedReader(new InputStreamReader(stream));
            try (scanner){
                while ((currentLine = scanner.readLine()) != null){
                    commandHandler(currentLine, scanner);
                }
            }
            catch (IOException e){
                handleError(e);
            }
        }
        catch (FileNotFoundException e){
            handleError(e);
        }
    }

    /**
     * Завершает работу программы
     */
    private static void exit() {
        System.exit(0);
    }

    /**
     * @param stream Поток для считывания данных
     *               Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
     */
    public static void addIfMax(BufferedReader stream) {
        Vehicle vehicle = Vehicle.fromUser(stream);
        if (vehicles.isEmpty() || vehicles.last().compareTo(vehicle) < 0) {
            vehicles.add(vehicle);
            System.out.println("Элемент добавлен в коллекцию.");
        } else {
            System.out.println("Элемент не добавлен в коллекцию.");
        }
    }

    /**
     * @param stream Поток для считывания данных
     *               Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
     */
    public static void addIfMin(BufferedReader stream) {
        Vehicle vehicle = Vehicle.fromUser(stream);

        if (vehicles.isEmpty() || vehicles.first().compareTo(vehicle) > 0) {
            vehicles.add(vehicle);
            System.out.println("Элемент добавлен в коллекцию.");
        } else {
            System.out.println("Элемент не добавлен в коллекцию.");
        }
    }

    /**
     * @param stream Поток для считывания данных
     *               Удаляет из коллекции все элементы, меньшие, чем заданный
     */
    public static void removeLower(BufferedReader stream) {
        Vehicle vehicle = Vehicle.fromUser(stream);
        vehicles.removeIf(v -> v.compareTo(vehicle) < 0);
    }

    /**
     * Группирует элементы коллекции по значению поля enginePower, выводит количество элементов в каждой группе
     */
    private static void groupCountingByEnginePower() {
        vehicles.stream()
                .collect(Collectors.groupingBy(Vehicle::getEnginePower, Collectors.counting()))
                .forEach((power, count) -> System.out.println("Engine power: " + power + ", count: " + count));
    }

    /**
     * @param numberOfWheelsString
     * Выводит элементы, значения поля numberOfWheels в который равно заданному
     */
    private static void filterByNumberOfWheels(String numberOfWheelsString) {
        int numberOfWheels = Integer.parseInt(numberOfWheelsString);
        vehicles.stream()
                .filter(v -> v.getNumberOfWheels() == numberOfWheels)
                .forEach(System.out::println);
    }

    /**
     * Выводит значения поля numberOfWheels всех элементов в порядке возрастания
     */
    private static void printFieldAscendingNumberOfWheels() {
        vehicles.stream()
                .map(Vehicle::getNumberOfWheels)
                .sorted()
                .forEach(System.out::println);
    }
    public static void handleError(Exception e){
        System.out.println("Произошла ошибка: " + e.getMessage());
        System.out.println("Вы можете повторите ввод команды, или завершить выполнение программы командой exit");
    }
}
