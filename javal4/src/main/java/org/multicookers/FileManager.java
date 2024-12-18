package org.multicookers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.multicookers.utils.ConcreteMulticooker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileManager {

    // Чтение данных из TXT файла
    public static List<Multicooker> readFromTxt(String filename) {
        List<Multicooker> multicookers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                // Проверка на корректность данных
                if (parts.length < 5) {
                    System.out.println("Строка пропущена из-за недостаточных данных: " + line);
                    continue; // Пропускаем строку, если данных недостаточно
                }
    
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    int capacity = Integer.parseInt(parts[2].trim());
                    int wattage = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
    
                    // Добавляем объект, если все данные корректны
                    multicookers.add(new ConcreteMulticooker(id, type, capacity, wattage, price));
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка формата данных в строке: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multicookers;
    }

    // Запись в TXT файл
    public static void writeToTxt(String filename, List<Multicooker> multicookers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Multicooker multicooker : multicookers) {
                writer.write(multicooker.getId() + "," + multicooker.getType() + "," + multicooker.getCapacity() + "," +
                        multicooker.getWattage() + "," + multicooker.getPrice() + "\n");
            }
            System.out.println("Данные успешно записаны в TXT файл.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Чтение из XML файла
    public static List<Multicooker> readFromXML(String filename) {
        List<Multicooker> multicookers = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));

            NodeList multicookerNodes = document.getElementsByTagName("multicooker");

            for (int i = 0; i < multicookerNodes.getLength(); i++) {
                Node node = multicookerNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    int capacity = Integer.parseInt(element.getElementsByTagName("capacity").item(0).getTextContent());
                    int wattage = Integer.parseInt(element.getElementsByTagName("wattage").item(0).getTextContent());
                    double price = Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent());

                    multicookers.add(new ConcreteMulticooker(id, type, capacity, wattage, price));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multicookers;
    }

    // Запись в XML файл
    public static void writeToXML(String filename, List<Multicooker> multicookers) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("multicookers");
            document.appendChild(root);

            for (Multicooker multicooker : multicookers) {
                Element multicookerElement = document.createElement("multicooker");

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(multicooker.getId())));
                multicookerElement.appendChild(id);

                Element type = document.createElement("type");
                type.appendChild(document.createTextNode(multicooker.getType()));
                multicookerElement.appendChild(type);

                Element capacity = document.createElement("capacity");
                capacity.appendChild(document.createTextNode(String.valueOf(multicooker.getCapacity())));
                multicookerElement.appendChild(capacity);

                Element wattage = document.createElement("wattage");
                wattage.appendChild(document.createTextNode(String.valueOf(multicooker.getWattage())));
                multicookerElement.appendChild(wattage);

                Element price = document.createElement("price");
                price.appendChild(document.createTextNode(String.valueOf(multicooker.getPrice())));
                multicookerElement.appendChild(price);

                root.appendChild(multicookerElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filename));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

            System.out.println("Данные успешно записаны в XML файл.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Чтение из JSON файла
    public static List<Multicooker> readFromJSON(String filename) {
        List<Multicooker> multicookers = new ArrayList<>();
        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type multicookerListType = new TypeToken<ArrayList<ConcreteMulticooker>>() {}.getType();
            multicookers = gson.fromJson(reader, multicookerListType);
        } catch (Exception e) {
        }
        return multicookers;
    }

    // Запись в JSON файл
    public static void writeToJSON(String filename, List<Multicooker> multicookers) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(multicookers, writer);
            System.out.println("Данные успешно записаны в JSON файл.");
        } catch (Exception e) {
        }
    }

    // Шифрование и запись данных
    public static void writeEncryptedToTxt(String filename, List<Multicooker> multicookers, SecretKey secretKey) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder data = new StringBuilder();
            
            // Собираем все данные о мультиварках в строку
            for (Multicooker multicooker : multicookers) {
                String multicookerData = multicooker.getId() + "," + multicooker.getType() + "," + multicooker.getCapacity() + "," +
                                   multicooker.getWattage() + "," + multicooker.getPrice() + "\n";
                data.append(multicookerData);
            }

            // Шифруем собранные данные
            String encryptedData = EncryptionUtils.encrypt(data.toString(), secretKey);

            // Записываем зашифрованные данные в файл
            writer.write(encryptedData);
            System.out.println("Данные успешно зашифрованы и записаны в файл " + filename);
        } catch (IOException e) {
        }
    }

    // Чтение и расшифровка данных
    public static List<Multicooker> readDecryptedFromTxt(String filename, SecretKey secretKey) {
        List<Multicooker> multicookers = new ArrayList<>();
    
        if (secretKey == null) {
            System.out.println("Ошибка: secretKey не может быть null.");
            return multicookers; // Возврат пустого списка
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder encryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                encryptedData.append(line);
            }
    
            if (encryptedData.length() == 0) {
                System.out.println("Файл пуст: " + filename);
                return multicookers;
            }
    
            String decryptedData = EncryptionUtils.decrypt(encryptedData.toString(), secretKey);
            String[] multicookerLines = decryptedData.split("\n");
    
            for (String multicookerLine : multicookerLines) {
                String[] parts = multicookerLine.split(",");
                
                if (parts.length < 5) {
                    System.out.println("Пропущена неполная строка: " + multicookerLine);
                    continue;
                }
    
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    int capacity = Integer.parseInt(parts[2].trim());
                    int wattage = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
    
                    multicookers.add(new ConcreteMulticooker(id, type, capacity, wattage, price));
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка формата данных в строке: " + multicookerLine);
                }
            }
    
            System.out.println("Данные успешно расшифрованы и прочитаны из файла " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    
        return multicookers;
    }
    
    // Сохранение данных и создание ZIP архива
    public static void saveDataWithEncryptionAndZip(List<Multicooker> multicookers) {
        String encryptedFileName = "multicookers.txt";

        try (FileOutputStream fos = new FileOutputStream("multicookers.zip");
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            File fileToZip = new File(encryptedFileName);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.closeEntry();
            fis.close();

            System.out.println("Данные заархивированы в ZIP файл.");
        } catch (IOException e) {

        }
    }
    SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // Размер ключа AES - 128 бит
            return keyGen.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
