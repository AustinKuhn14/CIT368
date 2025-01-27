import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp{
    public static void main(String[] args){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Input your zip code here:");
        String Zip = keyboard.nextLine();


        //Everything from here down is from CHATGPT until the keyboard.close

         // Your OpenWeatherMap API key
        String apiKey = "2a3025cca65db0a2b708856792830f9d";

        // Build the URL for the API request to get the 3-day forecast (5-day with 3-hour intervals)
        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?zip=" + Zip + "&appid=" + apiKey + "&units=imperial";

        try {
            // Make the API request
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Get the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Now parse the response manually (simple parsing without external libraries)
            String jsonResponse = response.toString();

            // Check if the request was successful (check for "cod" == 200)
            if (jsonResponse.contains("\"cod\":\"200\"")) {
                System.out.println("Weather forecast for the next 3 days:");

                // Process the data to extract the 3-day forecast
                for (int i = 0; i < 3; i++) {
                    // Find the forecast data for the current day (3-hour intervals)
                    int startIdx = jsonResponse.indexOf("\"dt_txt\":\"", jsonResponse.indexOf("\"list\":") + 7) + 10;
                    int endIdx = jsonResponse.indexOf("\"", startIdx);
                    String dateTime = jsonResponse.substring(startIdx, endIdx);

                    // Extract temperature
                    startIdx = jsonResponse.indexOf("\"temp\":", jsonResponse.indexOf("\"main\":") + 6) + 7;
                    endIdx = jsonResponse.indexOf(",", startIdx);
                    double temperature = Double.parseDouble(jsonResponse.substring(startIdx, endIdx));

                    // Extract humidity
                    startIdx = jsonResponse.indexOf("\"humidity\":", jsonResponse.indexOf("\"main\":") + 6) + 11;
                    endIdx = jsonResponse.indexOf(",", startIdx);
                    int humidity = Integer.parseInt(jsonResponse.substring(startIdx, endIdx));

                    // Extract weather description
                    startIdx = jsonResponse.indexOf("\"description\":\"", jsonResponse.indexOf("\"weather\":") + 9) + 15;
                    endIdx = jsonResponse.indexOf("\"", startIdx);
                    String weatherDescription = jsonResponse.substring(startIdx, endIdx);

                    // Print the weather for the day
                    System.out.println("\nDay " + (i + 1) + " (" + dateTime + "):");
                    System.out.println("Temperature: " + temperature + "°F");
                    System.out.println("Humidity: " + humidity + "%");
                    System.out.println("Condition: " + weatherDescription);

                    // Move the index to the next forecast (3-hour interval)
                    jsonResponse = jsonResponse.substring(jsonResponse.indexOf("\"dt_txt\":\"", startIdx) + 1);
                }
            } else {
                System.out.println("Error: Could not retrieve weather data. Please check the zip code or try again later.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        keyboard.close();
    }

    
}