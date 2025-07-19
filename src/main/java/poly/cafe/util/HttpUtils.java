package poly.cafe.util;

public class HttpUtils {

    public static String sendGet(String url, String apiKey) {
        try {
            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("apikey", apiKey);
            con.setRequestProperty("Accept", "application/json");

            java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(con.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public static String sendPost(String url, String json, String apiKey) {
        try {
            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("apikey", apiKey);
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Prefer", "resolution=merge-duplicates");
            con.setDoOutput(true);
            try (java.io.OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = con.getResponseCode();
            System.out.println("[DEBUG] POST Response Code: " + responseCode);
            
            if (responseCode >= 200 && responseCode < 300) {
                // Success
                try {
                    java.io.BufferedReader in = new java.io.BufferedReader(
                            new java.io.InputStreamReader(con.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    System.out.println("[DEBUG] POST Response: " + response.toString());
                    return response.toString();
                } catch (Exception e) {
                    System.out.println("[DEBUG] POST Success but no response body");
                    return "";
                }
            } else {
                // Error
                java.io.BufferedReader in = new java.io.BufferedReader(
                        new java.io.InputStreamReader(con.getErrorStream(), java.nio.charset.StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("[DEBUG] POST Error Response: " + response.toString());
                return "";
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] POST Exception: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

public static String sendPatch(String url, String json, String apiKey) {
    try {
        // In ra URL đầy đủ để debug
        System.out.println("[DEBUG] Full PATCH URL: " + url);

        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

        // Thử PATCH trước, nếu không được thì dùng PUT
        con.setRequestMethod("PATCH");
        con.setRequestProperty("apikey", apiKey);
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Prefer", "return=minimal");
        con.setDoOutput(true);

        try (java.io.OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("[DEBUG] PATCH Response Code: " + responseCode);

        if (responseCode >= 200 && responseCode < 300) {
            // Success
            try {
                java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(con.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("[DEBUG] PATCH Response: " + response.toString());
                return response.toString();
            } catch (Exception e) {
                System.out.println("[DEBUG] PATCH Success but no response body");
                return "";
            }
        } else {
            // Error - thử lại với PUT
            System.out.println("[DEBUG] PATCH failed, trying PUT...");
            return sendPut(url, json, apiKey);
        }
    } catch (Exception e) {
        System.out.println("[DEBUG] PATCH Exception: " + e.getMessage());
        e.printStackTrace();
        return "";
    }
}

public static String sendPut(String url, String json, String apiKey) {
    try {
        System.out.println("[DEBUG] Full PUT URL: " + url);

        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

        con.setRequestMethod("PUT");
        con.setRequestProperty("apikey", apiKey);
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Prefer", "return=minimal");
        con.setDoOutput(true);

        try (java.io.OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("[DEBUG] PUT Response Code: " + responseCode);

        if (responseCode >= 200 && responseCode < 300) {
            // Success
            try {
                java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(con.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("[DEBUG] PUT Response: " + response.toString());
                return response.toString();
            } catch (Exception e) {
                System.out.println("[DEBUG] PUT Success but no response body");
                return "";
            }
        } else {
            // Error
            java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(con.getErrorStream(), java.nio.charset.StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("[DEBUG] PUT Error Response: " + response.toString());
            return "";
        }
    } catch (Exception e) {
        System.out.println("[DEBUG] PUT Exception: " + e.getMessage());
        e.printStackTrace();
        return "";
    }
}

    public static String sendDelete(String url, String apiKey) {
        try {
            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("apikey", apiKey);
            con.setRequestProperty("Accept", "application/json");
            int responseCode = con.getResponseCode();
            return String.valueOf(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
