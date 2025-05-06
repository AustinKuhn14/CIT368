import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.io.FileInputStream;

public class WeatherApp {

    public static String getUserZipCode() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Input your zip code here:");
        String zip = keyboard.nextLine();

        while (!Valid.zip(zip)) {
            System.out.println("Invalid zip, try something like 17701");
            zip = keyboard.nextLine();
        }
        return zip;

    }

    //Gathers key from another file, that isn't pushed to the public
    public static String loadApiKey() {
        Properties props = new Properties();
        try {
         FileInputStream in = new FileInputStream("config.properties");
            props.load(in);
            in.close();
            return props.getProperty("apiKey");
        }catch (IOException e) {
            System.out.println("Failed to load API key.");
            return "";
    }
}


    public static String buildApiUrl(String zip, String apiKey) {
        return "https://api.openweathermap.org/data/2.5/forecast?zip=" + zip + "&appid=" + apiKey + "&units=imperial";
    }

    // Function to make the API request and get the raw JSON response
    public static String getWeatherData(String apiUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    public static String parseAndDisplayWeatherData(String jsonResponse) {
        StringBuilder output = new StringBuilder();
    
        if (jsonResponse.contains("\"cod\":\"200\"")) {
            output.append("Weather forecast for the next 3 days:");
    
            for (int i = 0; i < 17; i++) {
                int startIdx = jsonResponse.indexOf("\"dt_txt\":\"", jsonResponse.indexOf("\"list\":") + 7) + 10;
                int endIdx = jsonResponse.indexOf("\"", startIdx);
                String dateTime = jsonResponse.substring(startIdx, endIdx);
    
                startIdx = jsonResponse.indexOf("\"temp\":", jsonResponse.indexOf("\"main\":") + 6) + 7;
                endIdx = jsonResponse.indexOf(",", startIdx);
                double temperature = Double.parseDouble(jsonResponse.substring(startIdx, endIdx));
    
                startIdx = jsonResponse.indexOf("\"humidity\":", jsonResponse.indexOf("\"main\":") + 6) + 11;
                endIdx = jsonResponse.indexOf(",", startIdx);
                int humidity = Integer.parseInt(jsonResponse.substring(startIdx, endIdx));
    
                startIdx = jsonResponse.indexOf("\"description\":\"", jsonResponse.indexOf("\"weather\":") + 9) + 15;
                endIdx = jsonResponse.indexOf("\"", startIdx);
                String weatherDescription = jsonResponse.substring(startIdx, endIdx);
    
                //Only prints the three seperate days instead of all 3-hour periods
                if (i == 0 || i == 8 || i == 16) {
                    output.append(String.format(
                        "%n%nDay %d (%s):%nTemperature: %.1f°F%nHumidity: %d%%%nCondition: %s",
                        (i == 0 ? 1 : (i == 8 ? 2 : 3)), dateTime, temperature, humidity, weatherDescription
                    ));
                }
    
                jsonResponse = jsonResponse.substring(jsonResponse.indexOf("\"dt_txt\":\"", startIdx) + 1);
            }
        } else {
            output.append("Error: Could not retrieve weather data. Please check the zip code or try again later.");
        }
    
        return output.toString();
    }
    

    public static void logRequest(String zipCode, String response) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.println("[" + timestamp + "] ZIP Code: " + zipCode);
            writer.println("Response: " + response);
            writer.println("--------------------------------------------------");
        } catch (IOException e) {
            System.out.println("Warning: Failed to write to log file.");
            //e.printStackTrace();
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        Boolean DEBUG = false;
        String zip = getUserZipCode();
        String apiKey = loadApiKey();
        String apiUrl = buildApiUrl(zip, apiKey);
    
        try {
            String jsonResponse = getWeatherData(apiUrl);
            String output = parseAndDisplayWeatherData(jsonResponse);
            
            System.out.println(output);
            logRequest(zip, output);
        } catch (IOException e) {
            String errorMsg = "ERROR: " + e.getMessage();
            logRequest(zip, errorMsg);
            if (DEBUG) {
                e.printStackTrace();
            } else {
                System.out.println("Something happened, and the program didn't work.");
            }
        }
    }
    
    
}
