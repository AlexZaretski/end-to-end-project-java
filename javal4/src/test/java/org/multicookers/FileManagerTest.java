package org.multicookers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.multicookers.utils.ConcreteMulticooker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileManagerTest {

    @Test
    public void testWriteToFile() throws IOException {
        Path tempFile = Files.createTempFile("multicookers2", ".txt");

        List<Multicooker> multicookerList = new ArrayList<>();
        multicookerList.add(new ConcreteMulticooker(1, "XM-3000", 120, 6000, 3000));
        multicookerList.add(new ConcreteMulticooker(2, "Turbocool-50", 80, 7000, 1500));

        FileManager.writeToTxt(tempFile.toString(), multicookerList);

        List<String> fileContent = Files.readAllLines(tempFile);

        assertEquals(2, fileContent.size());
        assertTrue(fileContent.get(0).contains("XM-3000"));
        assertTrue(fileContent.get(1).contains("Turbocool-50"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testWriteToJsonFile() throws IOException {
    Path tempFile = Files.createTempFile("multicookers2", ".json");

    List<Multicooker> multicookerList = new ArrayList<>();
    multicookerList.add(new ConcreteMulticooker(1, "XM-3000", 120, 6000, 3000));
    multicookerList.add(new ConcreteMulticooker(2, "Turbocool-50", 80, 7000, 1500));

    FileManager.writeToJSON(tempFile.toString(), multicookerList);

    String fileContent = Files.readString(tempFile);
    System.out.println("Содержимое JSON файла: \n" + fileContent); 

    Gson gson = new Gson();
    List<Multicooker> parsedMulticookers = gson.fromJson(fileContent, new TypeToken<List<ConcreteMulticooker>>(){}.getType());

    assertEquals(2, parsedMulticookers.size());
    assertEquals("XM-3000", parsedMulticookers.get(0).getType());
    assertEquals("Turbocool-50", parsedMulticookers.get(1).getType());

    Files.deleteIfExists(tempFile);
}


    @Override
    public String toString() {
        return "FileManagerTest []";
    }

    @Test
    public void testWriteToXmlFile() throws IOException {
        Path tempFile = Files.createTempFile("multicookers2", ".xml");

        List<Multicooker> multicookerList = new ArrayList<>();
        multicookerList.add(new ConcreteMulticooker(1, "XM-3000", 120, 6000, 3000));
        multicookerList.add(new ConcreteMulticooker(2, "Turbocool-50", 80, 7000, 1500));

        FileManager.writeToXML(tempFile.toString(), multicookerList);

        String fileContent = Files.readString(tempFile);

        assertTrue(fileContent.contains("<type>XM-3000</type>"));
        assertTrue(fileContent.contains("<type>Turbocool-50</type>"));

        Files.deleteIfExists(tempFile);
    }
}