package com.kenzie.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class Main {
    static String TEST_FRUIT_INPUT = "{\n" +
            "    \"genus\": \"Malus\",\n" +
            "    \"name\": \"Apple\",\n" +
            "    \"family\": \"Rosaceae\",\n" +
            "    \"order\": \"Rosales\",\n" +
            "    \"nutritions\": {\n" +
            "        \"carbohydrates\": 11.4,\n" +
            "        \"protein\": 0.3,\n" +
            "        \"fat\": 0.4,\n" +
            "        \"calories\": 52,\n" +
            "        \"sugar\": 10.3\n" +
            "    }\n" +
            "}";


    public static void main(String[] args) throws JsonProcessingException {

        //TODO: Demo sendGET and sendPUT with FruityVice
        String getURL = "https://www.fruityvice.com/api/fruit/all";
        String putURL = "https://www.fruityvice.com/api/fruit";

        /* Prints out results of GET and PUT call for FruityVice API */

        //System.out.println(  MyHttpClient.sendGET(getURL)  );
        //System.out.println(  MyHttpClient.sendPUT(putURL,TEST_FRUIT_INPUT )  );
        try {
            //TODO: Search for a list of TV shows based on a name
            String searchURL = "https://api.tvmaze.com/singlesearch/shows?q=the%20witcher";

            /* Prints out results of HTTP GET call */
            //System.out.println(MyHttpClient.sendGET(searchURL));

            String jsonString = MyHttpClient.sendGET(searchURL);

            // TODO: Deserialize the JSON: Display the id number, show name, and description
            // use ObjectMapper - instance ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            //read value -- pass in JSON string and reference to a class
            TVShowDTO myShow = objectMapper.readValue(jsonString, TVShowDTO.class);

            System.out.println(myShow.getId());
            System.out.println(myShow.getName());
            System.out.println(myShow.getSummary());

        }
        catch (Exception e) {
            System.out.println("Unexpected exception.");
            System.out.println(e.getMessage());
        }

    }

    static class MyHttpClient {

        //TODO: Write sendGET method that takes URL and returns response
        public static String sendGET(String URLString) {

            //** Start of GET request algorithm
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create(URLString);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            try {

                HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                int statusCode = httpResponse.statusCode();
                if (statusCode == 200) {
                    return httpResponse.body();
                }
                else {
                    // String.format is fun! Worth a Google if you're interested
                    return String.format("GET request failed: %d status code received", statusCode);
                }
            } catch (IOException | InterruptedException e) {
                return e.getMessage();
            }

        }

        //TODO: Write sendPUT method that takes URL and JSON Body String and returns response
        public static String sendPUT(String URLString, String requestBody){
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create(URLString);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            try {
                HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                int statusCode = httpResponse.statusCode();
                if (statusCode == 201 || statusCode == 202) {
                    return httpResponse.body();
                }
                else if(statusCode == 422 || statusCode == 417){
                    return "Server reached. Unable to add fruit.";
                }
                else {
                    return String.format("PUT request failed: %d status code received", statusCode);
                }
            } catch (IOException | InterruptedException e) {
                return e.getMessage();
            }
        }
    }
}
