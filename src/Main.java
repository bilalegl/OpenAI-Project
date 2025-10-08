import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        try (Scanner inputScanner = new Scanner(System.in)) {
            System.out.println("🤖 ChatBot Ready! Type 'exit' to quit.");
            
            while (true) {
                System.out.print("\nYou: ");
                String userPrompt = inputScanner.nextLine();

                // Exit condition
                if (userPrompt.equalsIgnoreCase("exit")) {
                    System.out.println("👋 Goodbye!");
                    break;
                }

                // Build JSON payload
                String payload = "{\n" +
                        "  \"model\": \"gpt-3.5-turbo\",\n" +
                        "  \"messages\": [\n" +
                        "    {\"role\": \"user\", \"content\": \"" + userPrompt + "\"}\n" +
                        "  ]\n" +
                        "}";

                // Send request
                String response = sendPostRequest(API_URL, payload);

                // Parse JSON
                JSONObject json = new JSONObject(response);
                JSONArray choices = json.getJSONArray("choices");
                String answer = choices.getJSONObject(0)
                                       .getJSONObject("message")
                                       .getString("content");

                // Print AI answer
                System.out.println("AI: " + answer.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String sendPostRequest(String apiUrl, String payload) throws Exception {

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            os.write((payload.getBytes()));
            os.flush();
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while ((scanner.hasNext())) {
                response.append((scanner.nextLine()));
            }
        }

        return response.toString();
    }
}

