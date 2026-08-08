import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Client for Gemini's generateContent API. No third-party library is needed. */
public final class GeminiService {
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String MODEL = "gemini-2.5-flash";
    private static final Pattern JSON_STRING_FIELD = Pattern.compile(
            "\\\"%s\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"\\\\])*)\\\"");

    private GeminiService() {
    }

    public static boolean isConfigured() {
        return API_KEY != null && !API_KEY.isBlank();
    }

    public static String askGemini(String prompt) {
        if (!isConfigured()) {
            return "Error: GEMINI_API_KEY is not set. Set it before starting the application.";
        }

        HttpURLConnection connection = null;
        try {
            URI endpoint = URI.create("https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL + ":generateContent?key=" + API_KEY);
            connection = (HttpURLConnection) endpoint.toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setConnectTimeout(15_000);
            connection.setReadTimeout(45_000);
            connection.setDoOutput(true);

            String request = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapeJson(prompt) + "\"}]}]}";
            try (OutputStream output = connection.getOutputStream()) {
                output.write(request.getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();
            String response = readAll(status >= 200 && status < 300
                    ? connection.getInputStream() : connection.getErrorStream());
            if (status < 200 || status >= 300) {
                return "Error: Gemini returned HTTP " + status + ". " + jsonField(response, "message");
            }

            String answer = jsonField(response, "text");
            return answer.isBlank() ? "Error: Gemini returned no text response." : answer;
        } catch (Exception exception) {
            return "Error: Unable to contact Gemini: " + exception.getMessage();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private static String readAll(InputStream stream) throws Exception {
        if (stream == null) return "";
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
        }
        return response.toString();
    }

    private static String jsonField(String json, String fieldName) {
        Matcher matcher = Pattern.compile(String.format(JSON_STRING_FIELD.pattern(), Pattern.quote(fieldName))).matcher(json);
        return matcher.find() ? unescapeJson(matcher.group(1)) : "No details returned.";
    }

    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private static String unescapeJson(String value) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (current != '\\' || index + 1 >= value.length()) {
                result.append(current);
                continue;
            }
            char escaped = value.charAt(++index);
            switch (escaped) {
                case 'n': result.append('\n'); break;
                case 'r': result.append('\r'); break;
                case 't': result.append('\t'); break;
                case 'b': result.append('\b'); break;
                case 'f': result.append('\f'); break;
                case 'u':
                    if (index + 4 < value.length()) {
                        try {
                            result.append((char) Integer.parseInt(value.substring(index + 1, index + 5), 16));
                            index += 4;
                        } catch (NumberFormatException ignored) { result.append("\\u"); }
                    } else result.append("\\u");
                    break;
                default: result.append(escaped);
            }
        }
        return result.toString();
    }
}
