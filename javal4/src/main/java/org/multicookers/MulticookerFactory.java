package org.multicookers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.multicookers.utils.ConcreteMulticooker;

public class MulticookerFactory {
    private static DatabaseExporter argument=DatabaseExporter.getInstance();
    private static final String ALGORITHM = "AES"; 
    private static final String KEY_FILE = "secretKey.key"; 
    private static MulticookerCollection multicookerCollection = new MulticookerList();
    private static MulticookerCollection multicookerCollection2 = new MulticookerList();
    static SecretKey key;
    
    public static void saveKeyToFile(SecretKey secretKey) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(secretKey.getEncoded());
        }
    }

    // Загрузка ключа из файла
    public static SecretKey loadKeyFromFile() throws IOException {
        byte[] keyBytes = new byte[32]; 
        FileInputStream fis = new FileInputStream(KEY_FILE);
        fis.read(keyBytes);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
    

    public static void main(String[] args) throws IOException {
        try {
            key = loadKeyFromFile();
            System.out.println("Ключ загружен из файла.");
        } catch (IOException e) {
            System.out.println("Ключ не найден, генерируем новый.");    
            key = EncryptionUtils.generateKey();
            System.out.println("Новый ключ сгенерирован и сохранен.");
        }
        Multicooker simpleOne = new ConcreteMulticooker(1, "simple", 10, 4000, 120);
        Multicooker upgradedOne = new UpgradeDecorator(simpleOne, 3, 1000, "knife");
        System.out.println("Original Multicooker Capacity and Wattage: " + simpleOne.getCapacity()+" "+ simpleOne.getWattage());
        System.out.println("Upgraded Multicooker Capacity and Wattage: " + upgradedOne.getCapacity()+" "+ upgradedOne.getWattage());
        System.out.println(upgradedOne.toString());
        System.out.println(key);
        Scanner scanner = new Scanner(System.in);
        String txtFilename = "multicookers.txt";
        String xmlFilename = "multicookers.xml";
        String jsonFilename = "multicookers.json";
        String encryptedFilename = "encrypted.txt";

        Set<Multicooker> multicookerSet = new HashSet<>();
        multicookerSet.addAll(FileManager.readFromTxt(txtFilename));
        multicookerSet.addAll(FileManager.readFromXML(xmlFilename));
        multicookerSet.addAll(FileManager.readFromJSON(jsonFilename));
        multicookerCollection.getAllMulticookers().addAll(multicookerSet);

        while (true) {
            System.out.println("1. Добавить мультиварку");
            System.out.println("2. Показать все мультиварки");
            System.out.println("3. Сохранить и зашифровать данные");
            System.out.println("4. Прочитать и расшифровать данные");
            System.out.println("5. Сохранить данные и создать архив (ZIP)");
            System.out.println("6. Удалить мультиварку");
            System.out.println("7. Обновить мультиварку");
            System.out.println("8. Сортировать по цене");
            System.out.println("9. Сортировать по объему");
            System.out.println("10. Сохранить мультиварки в файл");
            System.out.println("11. Прочитать мультиварки из файла");
            System.out.println("12. Экспортировать в базу данных");
            System.out.println("13. Импортировать из базы данных");
            System.out.println("14. Удалить все мультиварки");
            System.out.println("15. Выход");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addMulticooker(scanner);
                    break;
                case 2:
                    printAllMulticooker();
                    break;
                case 3:
                    saveAndEncryptData();
                    break;
                case 4:
                    readAndDecryptData();
                    break;
                case 5:
                    saveDataAndCreateZip();
                    break;
                case 6:
                    removeMulticooker(scanner);
                    break;
                case 7:
                    updateMulticooker(scanner);
                    break;
                case 8:
                    sortMulticookersByPrice();
                    break;
                case 9:
                    sortMulticookersByCapacity();
                    break;
                case 10:
                    saveToFile(txtFilename, xmlFilename, jsonFilename);
                    break;
                case 11:
                    readFromFile(txtFilename, xmlFilename, jsonFilename);
                    break;
                case 12:
                    List<Multicooker> exlmulticookers = multicookerCollection.getAllMulticookers();
                    if (exlmulticookers == null || exlmulticookers.isEmpty()) {
                        System.out.println("Список мультиварок пуст. Экспорт невозможно выполнить.");
                    } else {
                        argument.exportToDatabase(exlmulticookers);
                    }
                    break;
                case 13:
                    List<Multicooker> boh= argument.importFromExcel("multicookers.xlsx");
                    multicookerSet.addAll(boh);
                    multicookerCollection.getAllMulticookers().addAll(multicookerSet);
                    printAllMulticooker();
                    break;
                case 14:
                    removeAllMulticookers();
                case 15:
                    System.out.println("Выход из программы.");
                    saveKeyToFile(key);
                    return;
                default:
                    System.out.println("Неверный ввод. Пожалуйста, попробуйте снова.");
            }
        }
    }

    // Добавление новой мультиварки
    private static void addMulticooker(Scanner scanner) {
        int id = 0;
        int capacity = 0;
        int wattage = 0;
        double price = 0.0;
        String type;
    
        System.out.println("Введите id мультиварки:");
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите целое число для id.");
            scanner.next(); // Пропустить некорректный ввод
        }
        id = scanner.nextInt();
    
        System.out.println("Введите тип мультиварки:");
        type = scanner.next();
    
        System.out.println("Введите объем мультиварки:");
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите целое число для объема.");
            scanner.next(); // Пропустить некорректный ввод
        }
        capacity = scanner.nextInt();
    
        System.out.println("Введите мощность мультиварки:");
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите целое число для мощности.");
            scanner.next(); // Пропустить некорректный ввод
        }
        wattage = scanner.nextInt();
    
        System.out.println("Введите цену мультиварки:");
        while (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: введите число для цены (с десятичной точкой, если необходимо).");
            scanner.next(); // Пропустить некорректный ввод
        }
        price = scanner.nextDouble();
    
        Multicooker newMulticooker = new ConcreteMulticooker(id, type, capacity, wattage, price);
        multicookerCollection.addMulticooker(newMulticooker);
        System.out.println("Мультиварка успешно добавлена.");
    }
    

    // Удаление мультиварки
    private static void removeMulticooker(Scanner scanner) {
        System.out.println("Введите id мультиварки для удаления:");
        int id = scanner.nextInt();
        multicookerCollection.removeMulticooker(id);
    }

    // Обновление мультивакри
    private static void updateMulticooker(Scanner scanner) {
        System.out.println("Введите id мультиварки для обновления:");
        int id = scanner.nextInt();
        Multicooker house = multicookerCollection.getMulticookerById(id);

        if (house != null) {
            System.out.println("Введите новый тип мультиварки:");
            String type = scanner.next();
            System.out.println("Введите новый объем мультиварки:");
            int capacity = scanner.nextInt();
            System.out.println("Введите новую мощность мультиварки:");
            int wattage = scanner.nextInt();
            System.out.println("Введите новую цену мультиварки:");
            double price = scanner.nextDouble();

            house.setType(type);
            house.setCapacity(capacity);
            house.setWattage(wattage);
            house.setPrice(price);
        } else {
            System.out.println("Мультиварка с таким id не найден.");
        }
    }

    // Показать все мультиварки
    private static void printAllMulticooker() {
        List<Multicooker> multicookers = multicookerCollection.getAllMulticookers();
        if (multicookers.isEmpty()) {
            System.out.println("Мультиварки отсутствуют.");
        } else {
            for (Multicooker multicooker : multicookers) {
                System.out.println(multicooker);
            }
        }
    }
    private static void printAllMulticookers2() {
        List<Multicooker> multicookers = multicookerCollection2.getAllMulticookers();
        if (multicookers.isEmpty()) {
            System.out.println("Мультиварки отсутствуют.");
        } else {
            for (Multicooker multicooker : multicookers) {
                System.out.println(multicooker);
            }
        }
    }

    // Сортировка по цене
    private static void sortMulticookersByPrice() {
        List<Multicooker> multicookers = multicookerCollection.getAllMulticookers();
        multicookers.sort(Comparator.comparingDouble(Multicooker::getPrice));
        System.out.println("Мультиварки отсортированы по цене:");
        printAllMulticooker();
    }

    // Сортировка по объему
    private static void sortMulticookersByCapacity() {
        List<Multicooker> multicookers = multicookerCollection.getAllMulticookers();
        multicookers.sort(Comparator.comparingInt(Multicooker::getCapacity));
        System.out.println("Мультиварки отсортированы по площади:");
        printAllMulticooker();
    }

    // Сохранение и шифрование данных
    private static void saveAndEncryptData() {
            FileManager.writeEncryptedToTxt("encrypted.txt", multicookerCollection.getAllMulticookers(), key);
            System.out.println("Данные успешно зашифрованы и сохранены.");
    }

    // Чтение и расшифровка данных
    private static void readAndDecryptData() {
            List<Multicooker> decryptedHouses = FileManager.readDecryptedFromTxt("encrypted.txt", key);
            System.out.println("Дешифрованные данные:");
            multicookerCollection2.getAllMulticookers().addAll(decryptedHouses);
            printAllMulticookers2();
    }

    // Сохранение данных и создание ZIP архива
    private static void saveDataAndCreateZip() {
            FileManager.saveDataWithEncryptionAndZip(multicookerCollection.getAllMulticookers());
    }

    // Сохранение данных в файлы
    private static void saveToFile(String txtFilename, String xmlFilename, String jsonFilename) {
        List<Multicooker> multicookers = multicookerCollection.getAllMulticookers();
        FileManager.writeToTxt(txtFilename, multicookers);
        FileManager.writeToXML(xmlFilename, multicookers);
        FileManager.writeToJSON(jsonFilename, multicookers);
        System.out.println("Данные успешно сохранены в файлы.");
    }

    // Чтение данных из файлов
    private static void readFromFile(String txtFilename, String xmlFilename, String jsonFilename) {
        Set<Multicooker> multicookerSet = new HashSet<>();
        multicookerSet.addAll(FileManager.readFromTxt(txtFilename));
        multicookerSet.addAll(FileManager.readFromXML(xmlFilename));
        multicookerSet.addAll(FileManager.readFromJSON(jsonFilename));
        multicookerCollection.getAllMulticookers().clear();
        multicookerCollection.getAllMulticookers().addAll(multicookerSet);
        System.out.println("Данные успешно прочитаны из файлов.");
        printAllMulticooker();
    }

    public static void removeAllMulticookers() {
        // Очищаем коллекцию от всех элементов
        multicookerCollection.getAllMulticookers().clear();
        System.out.println("Все мультиварки были удалены из коллекции.");
    }

}
