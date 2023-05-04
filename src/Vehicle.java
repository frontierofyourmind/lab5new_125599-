package src;

import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Set;


/**
 * Класс, экземпляры которого хранятся в коллекции
 */
public class Vehicle implements Comparable<Vehicle> {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long enginePower; //Поле может быть null, Значение поля должно быть больше 0
    private int numberOfWheels; //Значение поля должно быть больше 0
    private VehicleType type; //Поле может быть null
    private FuelType fuelType; //Поле может быть null


    private static Set<Long> usedIds = new HashSet<Long>(); // хранилище уже использованных id

    /**
     * @param id id, которое нужно добавить в множество использованных
     *           Добавляет id в список использованных
     */
    public static void updateId(Long id){
        usedIds.add(id);
    }

    // Конструктор с параметрами
    public Vehicle(String name, Coordinates coordinates, Long enginePower, int numberOfWheels, VehicleType type, FuelType fuelType) {
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
        this.numberOfWheels = numberOfWheels;
        this.type = type;
        this.fuelType = fuelType;

        // Установка id и даты создания
        this.id = generateId();
        this.creationDate = LocalDateTime.now();
    }

    public Vehicle(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, Long enginePower, int numberOfWheels, VehicleType type, FuelType fuelType) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.enginePower = enginePower;
        this.numberOfWheels = numberOfWheels;
        this.type = type;
        this.fuelType = fuelType;
    }

    public Vehicle() {

    }

    /**
     * Генератор новых уникальных id
     */
    // Метод для генерации уникального id
    private Long generateId() {
//        Long newId = null;
//        do {
//            newId = (long) (Math.random() * Long.MAX_VALUE);
//        } while (newId <= 0 || usedIds.contains(newId));
        Long newId = 0L;
        while (usedIds.contains(newId)){
            newId += 1L;
        }
        usedIds.add(newId);
        return newId;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    public void setId(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(Long enginePower) {
        if (enginePower != null && enginePower <= 0) {
            throw new IllegalArgumentException("Engine power must be greater than 0");
        }
        this.enginePower = enginePower;
    }

    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    public void setNumberOfWheels(int numberOfWheels) {
        if (numberOfWheels <= 0) {
            throw new IllegalArgumentException("Number of wheels must be greater than 0");
        }
        this.numberOfWheels = numberOfWheels;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }


    /**
     * Реализует сравнение элементов для возможности сортировки
     * Сравнение происходит по полю enginePower, при его совпадении - по дате создания
     */
    @Override
    public int compareTo(Vehicle o) {
        int result = this.getEnginePower().compareTo(o.getEnginePower());
        if (result == 0){
            result = this.getCreationDate().compareTo(o.getCreationDate());
        }
        return result;
    }

    /**
     * Строковое представление экземпляра класса
     */
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", enginePower=" + enginePower +
                ", numberOfWheels=" + numberOfWheels +
                ", type=" + type +
                ", fuelType=" + fuelType +
                '}';
    }

    /**
     * Конвертирует поля класса в строку в формате CSV
     */
    public String toCSV() {
        return id + "," + name + "," + coordinates.getX() + "," + coordinates.getY() + "," +
                creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "," +
                enginePower + "," + numberOfWheels + "," +
                (type == null ? "" : type) + "," +
                (fuelType == null ? "" : fuelType);
    }

    /**
     * Преобразует строку в формате CSV в экземпляр класса
     */
    public static Vehicle fromCSV(String csv) {
        String[] values = csv.split(",");
        Long id = Long.parseLong(values[0]);
        String name = values[1];
        Coordinates coordinates = new Coordinates(Integer.parseInt(values[2]), Integer.parseInt(values[3]));
        LocalDateTime creationDate = LocalDateTime.parse(values[4], DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        Long enginePower = Long.parseLong(values[5]);
        int numberOfWheels = Integer.parseInt(values[6]);
        VehicleType type = values[7].equals("") ? null : VehicleType.valueOf(values[7]);
        FuelType fuelType = values[8].equals("") ? null : FuelType.valueOf(values[8]);

        return new Vehicle(id, name, coordinates, creationDate, enginePower, numberOfWheels, type, fuelType);
    }

    /**
     * Изменяет значения полей класса, сохраняя генерируемые автоматически
     */
    public static Vehicle modifyFromUser(Vehicle vehicle, BufferedReader scanner){
        try {
            System.out.println("Введите имя: ");
            String name = scanner.readLine().trim();
            while (name == "") {
                System.out.println("Введите корректное значение. Поле не может быть пустым.");
                name = scanner.readLine().trim();
            }


            Integer coordX = null;
            System.out.println("Введите координату X, X <= 970: ");
            try {
                coordX = Integer.parseInt(scanner.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (coordX == null || coordX > 970) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите координату X, X <= 970: ");
                try {
                    coordX = Integer.parseInt(scanner.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }
            }


            Integer coordY = null;
            System.out.println("Введите координату Y, Y > -988: ");
            try {
                coordY = Integer.parseInt(scanner.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (coordY == null || coordY <= -988) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите координату Y, Y > -988: ");
                try {
                    coordY = Integer.parseInt(scanner.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }
            }

            Coordinates coordinates = new Coordinates(coordX, coordY);

            Long enginePower = null;
            System.out.println("Введите мощность двигателя: ");
            try {
                String input = scanner.readLine();
                enginePower = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (enginePower <= 0 || enginePower == null) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите мощность двигателя: ");
                try {
                    String input = scanner.readLine();
                    if (input == "") break;
                    enginePower = Long.parseLong(input);
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }
            }

            Integer numberOfWheels = null;
            System.out.println("Введите количество колёс: ");
            try {
                numberOfWheels = Integer.parseInt(scanner.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (numberOfWheels == null || numberOfWheels <= 0) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите количество колёс: ");
                try {
                    numberOfWheels = Integer.parseInt(scanner.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }

            }

            VehicleType vehicleType = null;
            while (true) {
                System.out.println("Возможные виды транспорта:");
                for (VehicleType VT : VehicleType.values()){
                    System.out.println(VT.name() + " ");
                }
                System.out.println();
                System.out.println("Введите вид транспорта: ");
                try {
                    String input = scanner.readLine();
                    if (input == "") break;
                    vehicleType = VehicleType.valueOf(input.toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка! Попробуйте еще раз.");
                }
            }

            FuelType fuelType = null;
            while (true) {
                System.out.println("Возможные варианты топлива:");
                for (FuelType FT : FuelType.values()){
                    System.out.println(FT.name() + " ");
                }
                System.out.println();
                System.out.println("Введите тип топлива: ");
                try {
                    String input = scanner.readLine();
                    if (input == "") break;
                    fuelType = FuelType.valueOf(input.toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка! Попробуйте еще раз.");
                }
            }

            return new Vehicle(vehicle.getId(), name, coordinates, vehicle.getCreationDate(), enginePower, numberOfWheels, vehicleType, fuelType);
        }
        catch (IOException e){
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
        return new Vehicle();
    }

    /**
     * Создаёт новый экземпляр класса на основе введённых данных
     */
    public static Vehicle fromUser(BufferedReader scanner){
        try {
            System.out.println("Введите имя: ");
            String name = scanner.readLine().trim();
            while (name == "") {
                System.out.println("Введите корректное значение. Поле не может быть пустым.");
                name = scanner.readLine().trim();
            }


            Integer coordX = null;
            System.out.println("Введите координату X, X <= 970: ");
            try {
                coordX = Integer.parseInt(scanner.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (coordX == null || coordX > 970) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите координату X, X <= 970: ");
                try {
                    coordX = Integer.parseInt(scanner.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }
            }


            Integer coordY = null;
            System.out.println("Введите координату Y, Y > -988: ");
            try {
                coordY = Integer.parseInt(scanner.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (coordY == null || coordY <= -988) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите координату Y, Y > -988: ");
                try {
                    coordY = Integer.parseInt(scanner.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }
            }

            Coordinates coordinates = new Coordinates(coordX, coordY);

            Long enginePower = null;
            System.out.println("Введите мощность двигателя: ");
            try {
                String input = scanner.readLine();
                enginePower = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (enginePower <= 0) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите мощность двигателя: ");
                try {
                    String input = scanner.readLine();
                    if (input == "") break;
                    enginePower = Long.parseLong(input);
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }
            }

            Integer numberOfWheels = null;
            System.out.println("Введите количество колёс: ");
            try {
                numberOfWheels = Integer.parseInt(scanner.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод: " + e.getMessage());
            }
            while (numberOfWheels == null || numberOfWheels <= 0) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                System.out.println("Введите количество колёс: ");
                try {
                    numberOfWheels = Integer.parseInt(scanner.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод: " + e.getMessage());
                }

            }

            VehicleType vehicleType = null;
            while (true) {
                System.out.println("Возможные виды транспорта:");
                for (VehicleType VT : VehicleType.values()){
                    System.out.println(VT.name() + " ");
                }
                System.out.println();
                System.out.println("Введите вид транспорта: ");
                try {
                    String input = scanner.readLine();
                    if (input == "") break;
                    vehicleType = VehicleType.valueOf(input.toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка! Попробуйте еще раз.");
                }
            }

            FuelType fuelType = null;
            while (true) {
                System.out.println("Возможные варианты топлива:");
                for (FuelType FT : FuelType.values()){
                    System.out.println(FT.name() + " ");
                }
                System.out.println();
                System.out.println("Введите тип топлива: ");
                try {
                    String input = scanner.readLine();
                    if (input == "") break;
                    fuelType = FuelType.valueOf(input.toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка! Попробуйте еще раз.");
                }
            }

            return new Vehicle(name, coordinates, enginePower, numberOfWheels, vehicleType, fuelType);
        }
        catch (IOException e){
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
        return new Vehicle();
    }

}