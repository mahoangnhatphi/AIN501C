package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> result1 = new HashMap<>();
        List<String> dirs = List.of("test", "train");
        for (String dir : dirs) {
            int count = 0;
            File folder = new File("C:/data/" + dir);
            folder.mkdirs();
            // Create an HttpClient instance
            HttpClient httpClient = HttpClients.createDefault();

            // Define the URL of the RESTful service you want to call
            String apiUrl = "https://dog.ceo/api/breeds/list/all";

            // Create an HTTP GET request
            HttpGet httpGet = new HttpGet(apiUrl);


            try {
                // Execute the GET request
                HttpResponse response = httpClient.execute(httpGet);

                // Get the response entity (the content of the response)
                String responseBody = EntityUtils.toString(response.getEntity());

                // Create an ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();
                // Parse the JSON string into a JsonNode
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                JsonNode jsonNode1 = jsonNode.get("message");
                Iterator<Map.Entry<String, JsonNode>> entryIterator = jsonNode1.fields();
                List<String> breeds = new ArrayList<>();
                List<String> childBreeds = new ArrayList<>();
                count = 0;
                while (entryIterator.hasNext()) {
                    Map.Entry<String, JsonNode> stringJsonNodeEntry = entryIterator.next();
                    String breed = stringJsonNodeEntry.getKey();

                    if (!stringJsonNodeEntry.getValue().isEmpty()) {
//                        var arrNode = (ArrayNode) stringJsonNodeEntry.getValue();
//                        var i = arrNode.elements();
//                        while (i.hasNext()) {
//                            if (count < 100) {
//                                count++;
//                                String subBreed = i.next().toString();
//                                subBreed = subBreed.replace("\"", "");
//                                String titleSubBreed = String.valueOf(subBreed.charAt(0)).toUpperCase() + subBreed.substring(1);
////                            String folderPath = "C:/data/"+ dir + "/" + titleSubBreed + " " + String.valueOf(breed.charAt(0)).toUpperCase() + breed.substring(1);
//                                String folderPath = "C:/data/" + dir + "/";
//                                File file = new File(folderPath);
//                                file.mkdir();
//                                String url = "https://dog.ceo/api/breed/" + breed + "/" + subBreed + "/images/random/10";
//                                var imgPaths = getUrls(url);
//                                for (String imgPath : imgPaths) {
//                                    UUID uuid = UUID.randomUUID();
//                                    String imgName = imgPath.substring(imgPath.lastIndexOf("/")).replace("\"", "");
//                                    boolean saved = saveImg(imgPath, folderPath + uuid.toString() + ".jpg");
//                                    if (saved) {
////                                    result.put(uuid.toString(), breed + "_" + subBreed);
//                                        if (dir.equals("test")) {
//                                            result1.put(uuid.toString(), breed + "_" + subBreed);
//                                        } else {
//                                            result.put(uuid.toString(), breed + "_" + subBreed);
//                                        }
//                                    }
//                                }
//                            } else {
//                                break;
//                            }
//                        }
//                        childBreeds.add(stringJsonNodeEntry.getKey() + ", " + stringJsonNodeEntry.getValue().toString());
                    } else {
//                        String folderPath = "C:/data/" + dir + "/" + String.valueOf(breed.charAt(0)).toUpperCase() + breed.substring(1);
                        count++;
                        String folderPath = "C:/data/" + dir + "/";
                        File file = new File(folderPath);
                        file.mkdir();
                        String url = "https://dog.ceo/api/breed/" + breed + "/images/random/30";
                        var imgPaths = getUrls(url);
                        for (String imgPath : imgPaths) {
                            UUID uuid = UUID.randomUUID();
                            String imgName = imgPath.substring(imgPath.lastIndexOf("/")).replace("\"", "");
//                            saveImg(imgPath, folderPath + imgName);
                            boolean saved = saveImg(imgPath, folderPath + uuid.toString() + ".jpg");
                            if (saved) {
                                if (dir.equals("test")) {
                                    result1.put(uuid.toString(), breed);
                                } else {
                                    result.put(uuid.toString(), breed);
                                }
                            }
                        }
                        breeds.add(stringJsonNodeEntry.getKey());
                    }
                }
                String filePath = "C:/data/labels.csv";

                try (FileWriter fileWriter = new FileWriter(filePath);
                     CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {

                    // Write the CSV header (optional)
                    csvPrinter.printRecord("id", "breed");

                    // Write data rows
                    for (Map.Entry<String, String> e : result.entrySet()) {
                        csvPrinter.printRecord(e.getKey(), e.getValue());
                    }

                    // Flush and close the CSV printer
                    csvPrinter.flush();

                    System.out.println("Data has been saved to " + filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                filePath = "C:/data/test_labels.csv";
                try (FileWriter fileWriter = new FileWriter(filePath);
                     CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {

                    // Write the CSV header (optional)
                    csvPrinter.printRecord("id", "breed");

                    // Write data rows
                    for (Map.Entry<String, String> e : result1.entrySet()) {
                        csvPrinter.printRecord(e.getKey(), e.getValue());
                    }

                    // Flush and close the CSV printer
                    csvPrinter.flush();

                    System.out.println("Data has been saved to " + filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                for (var breed : breeds) {
//                    File file = new File("C:/data/" + dir + "/" + String.valueOf(breed.charAt(0)).toUpperCase() + breed.substring(1));
//                    file.mkdir();
//                }

                // Print the response
                System.out.println("Response Status Code: " + response.getStatusLine().getStatusCode());
                System.out.println("Response Body: " + responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> getUrls(String url) {
        final List<String> imgPaths = new ArrayList<>();
        // Create an HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // Create an HTTP GET request
        HttpGet httpGet = new HttpGet(url);

        try {
            // Execute the GET request
            HttpResponse response = httpClient.execute(httpGet);

            // Get the response entity (the content of the response)
            String responseBody = EntityUtils.toString(response.getEntity());

            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            // Parse the JSON string into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            ArrayNode jsonNode1 = (ArrayNode) jsonNode.get("message");
            var i = jsonNode1.elements();
            while (i.hasNext()) {
                imgPaths.add(i.next().toString());
            }

            // Print the response
            System.out.println("Response Status Code: " + response.getStatusLine().getStatusCode());
            System.out.println("Response Body: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgPaths;
    }

    private static boolean saveImg(String imageUrl, String output) {
        try {
            // Create a URL object
            URL url = new URL(imageUrl.replace("\"", ""));

            // Open a connection to the URL
            try (InputStream in = url.openStream()) {
                // Specify where you want to save the image locally
                Path outputPath = Path.of(output); // Replace with your desired local file path

                // Copy the image data from the URL to the local file
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image saved to: " + outputPath);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}