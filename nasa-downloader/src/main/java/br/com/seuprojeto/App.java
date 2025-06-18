package com.nasaimages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App {
    public static void main(String[] args) {
    String apiKey = "DEMO_KEY";
    String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .build();

    try {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String title = jsonObject.get("title").getAsString();
        String explanation = jsonObject.get("explanation").getAsString();
        
        String imageUrl = jsonObject.get("url").getAsString();

        System.out.println("Título: " + title);
        System.out.println("Descrição: " + explanation);
        System.out.println("URL da Imagem: " + imageUrl);
        
        System.out.println("\nIniciando download da imagem...");
        try {
            URL urlDaImagemParaBaixar = new URL(imageUrl);
            
            Path destino = Paths.get("imagem_do_dia.jpg");

            try (InputStream in = urlDaImagemParaBaixar.openStream()) {
                Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("Download concluído com sucesso! Imagem salva como: " + destino.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Falha ao baixar ou salvar a imagem: " + e.getMessage());
        }

    } catch (IOException | InterruptedException e) {
        System.err.println("Falha ao se comunicar com a API da NASA: " + e.getMessage());
        e.printStackTrace();
    }
}
}
