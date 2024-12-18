package org.multicookers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.multicookers.utils.ConcreteMulticooker;

public class MulticookerFactoryGUI {
    private static MulticookerCollection multicookerCollection = new MulticookerList();
    private static final String ALGORITHM = "AES";
    private static final String KEY_FILE = "secretKey.key";
    private static SecretKey key;

    public static void main(String[] args) {
        try {
            key = loadKeyFromFile();
            System.out.println("Ключ загружен из файла.");
        } catch (IOException e) {
            System.out.println("Ключ не найден, генерируем новый.");
            key = EncryptionUtils.generateKey();
            try {
                saveKeyToFile(key);
                System.out.println("Новый ключ сгенерирован и сохранен.");
            } catch (IOException ex) {
                System.err.println("Ошибка сохранения ключа: " + ex.getMessage());
            }
        }

        JFrame frame = new JFrame("Multicooker Factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea displayArea = new JTextArea(20, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3));

        addButton(buttonPanel, "Добавить мультиварку", e -> addMulticooker(displayArea));
        addButton(buttonPanel, "Показать все мультиварки", e -> printAllMulticooker(displayArea));
        addButton(buttonPanel, "Удалить мультиварку", e -> removeMulticooker(displayArea));
        addButton(buttonPanel, "Удалить все мультиварки", e -> removeAllMulticookers(displayArea));
        addButton(buttonPanel, "Сохранить и зашифровать данные", e -> saveAndEncryptData(displayArea));
        addButton(buttonPanel, "Прочитать и расшифровать данные", e -> readAndDecryptData(displayArea));
        addButton(buttonPanel, "Сохранить данные в файлы", e -> saveToFile(displayArea));
        addButton(buttonPanel, "Прочитать данные из файлов", e -> readFromFile(displayArea));
        addButton(buttonPanel, "Сортировать по цене", e -> sortMulticookersByPrice(displayArea));
        addButton(buttonPanel, "Сортировать по объему", e -> sortMulticookersByCapacity(displayArea));
        addButton(buttonPanel, "Параллельная запись/чтение", e -> parallelFileOperations(displayArea));
        addButton(buttonPanel, "Выход", e -> System.exit(0));
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);
    }

    private static void addButton(JPanel panel, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private static void addMulticooker(JTextArea displayArea) {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Введите ID мультиварки:"));
            String type = JOptionPane.showInputDialog("Введите тип мультиварки:");
            int capacity = Integer.parseInt(JOptionPane.showInputDialog("Введите объем мультиварки:"));
            int wattage = Integer.parseInt(JOptionPane.showInputDialog("Введите мощность мультиварки:"));
            double price = Double.parseDouble(JOptionPane.showInputDialog("Введите цену мультиварки:"));

            Multicooker newMulticooker = new ConcreteMulticooker(id, type, capacity, wattage, price);
            multicookerCollection.addMulticooker(newMulticooker);
            displayArea.append("Мультиварка добавлена: " + newMulticooker + "\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ошибка ввода: проверьте корректность введенных данных.");
        }
    }

    private static void printAllMulticooker(JTextArea displayArea) {
        List<Multicooker> multicookers = multicookerCollection.getAllMulticookers();
        if (multicookers.isEmpty()) {
            displayArea.append("Мультиварки отсутствуют.\n");
        } else {
            for (Multicooker multicooker : multicookers) {
                displayArea.append(multicooker + "\n");
            }
        }
    }

    private static void removeMulticooker(JTextArea displayArea) {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Введите ID мультиварки для удаления:"));
            multicookerCollection.removeMulticooker(id);
            displayArea.append("Мультиварка с ID " + id + " удалена.\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ошибка ввода: проверьте корректность введенного ID.");
        }
    }

    private static void removeAllMulticookers(JTextArea displayArea) {
        multicookerCollection.getAllMulticookers().clear();
        displayArea.append("Все мультиварки удалены.\n");
    }

    private static void saveAndEncryptData(JTextArea displayArea) {
        try {
            FileManager.writeEncryptedToTxt("encrypted.txt", multicookerCollection.getAllMulticookers(), key);
            displayArea.append("Данные успешно зашифрованы и сохранены.\n");
        } catch (Exception e) {
            displayArea.append("Ошибка при шифровании данных: " + e.getMessage() + "\n");
        }
    }

    private static void readAndDecryptData(JTextArea displayArea) {
        try {
            List<Multicooker> decryptedMulticookers = FileManager.readDecryptedFromTxt("encrypted.txt", key);
            multicookerCollection.getAllMulticookers().clear();
            multicookerCollection.getAllMulticookers().addAll(decryptedMulticookers);
            displayArea.append("Данные успешно расшифрованы:\n");
            printAllMulticooker(displayArea);
        } catch (Exception e) {
            displayArea.append("Ошибка при дешифровании данных: " + e.getMessage() + "\n");
        }
    }

    private static void saveToFile(JTextArea displayArea) {
        try {
            FileManager.writeToTxt("multicookers.txt", multicookerCollection.getAllMulticookers());
            FileManager.writeToXML("multicookers.xml", multicookerCollection.getAllMulticookers());
            FileManager.writeToJSON("multicookers.json", multicookerCollection.getAllMulticookers());
            displayArea.append("Данные успешно сохранены в файлы.\n");
        } catch (Exception e) {
            displayArea.append("Ошибка при сохранении данных: " + e.getMessage() + "\n");
        }
    }

    private static void readFromFile(JTextArea displayArea) {
        try {
            Set<Multicooker> multicookerSet = new HashSet<>();
            multicookerSet.addAll(FileManager.readFromTxt("multicookers.txt"));
            multicookerSet.addAll(FileManager.readFromXML("multicookers.xml"));
            multicookerSet.addAll(FileManager.readFromJSON("multicookers.json"));

            multicookerCollection.getAllMulticookers().clear();
            multicookerCollection.getAllMulticookers().addAll(multicookerSet);
            displayArea.append("Данные успешно прочитаны из файлов:\n");
            printAllMulticooker(displayArea);
        } catch (Exception e) {
            displayArea.append("Ошибка при чтении данных: " + e.getMessage() + "\n");
        }
    }

    private static void sortMulticookersByPrice(JTextArea displayArea) {
        List<Multicooker> multicookers = multicookerCollection.getAllMulticookers();
        multicookers.sort(Comparator.comparingDouble(Multicooker::getPrice));
        displayArea.append("Мультиварки отсортированы по цене:\n");
        printAllMulticooker(displayArea);
    }

    private static void sortMulticookersByCapacity(JTextArea displayArea) {
        List<Multicooker> multicookers = multicookerCollection.getAllMulticookers();
        multicookers.sort(Comparator.comparingInt(Multicooker::getCapacity));
        displayArea.append("Мультиварки отсортированы по объему:\n");
        printAllMulticooker(displayArea);
    }

    private static void parallelFileOperations(JTextArea displayArea) {
        Thread writeThread = new Thread(() -> {
            try {
                FileManager.writeToJSON("multicookers_parallel.json", multicookerCollection.getAllMulticookers());
                displayArea.append("Данные успешно записаны в JSON файл в потоке.\n");
            } catch (Exception e) {
            }
        });

        Thread readThread = new Thread(() -> {
            try {
                List<Multicooker> multicookers = FileManager.readFromJSON("multicookers_parallel.json");
                multicookerCollection.getAllMulticookers().clear();
                multicookerCollection.getAllMulticookers().addAll(multicookers);
                displayArea.append("Данные успешно прочитаны из JSON файла в потоке:\n");
                printAllMulticooker(displayArea);
            } catch (Exception e) {
            }
        });

        writeThread.start();
        readThread.start();
    }

    private static void saveKeyToFile(SecretKey secretKey) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(secretKey.getEncoded());
        }
    }

    private static SecretKey loadKeyFromFile() throws IOException {
        byte[] keyBytes = new byte[32];
        try (FileInputStream fis = new FileInputStream(KEY_FILE)) {
            fis.read(keyBytes);
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}
