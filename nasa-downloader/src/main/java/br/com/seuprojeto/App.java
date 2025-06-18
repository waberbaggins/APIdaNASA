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
    // A chave da API para acessar os dados da NASA
    String apiKey = "DEMO_KEY"; // Usando a chave de demonstração
    String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;

    // Cria um cliente HTTP para fazer a requisição
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .build();

    try {
        // 1. FAZ A REQUISIÇÃO PARA A API DA NASA
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        // 2. EXTRAI OS DADOS DO JSON
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String title = jsonObject.get("title").getAsString();
        String explanation = jsonObject.get("explanation").getAsString();
        
        // Esta é a URL real da imagem que a NASA nos enviou!
        String imageUrl = jsonObject.get("url").getAsString();

        // Imprime as informações no console
        System.out.println("Título: " + title);
        System.out.println("Descrição: " + explanation);
        System.out.println("URL da Imagem: " + imageUrl);
        
        // --- INÍCIO DA LÓGICA DE DOWNLOAD ---
        // Agora que temos a URL real, iniciamos o download.
        // O bloco de download DEVE ficar AQUI DENTRO.
        
        System.out.println("\nIniciando download da imagem...");
        try {
            // Usamos a variável 'imageUrl' que acabamos de pegar da NASA
            URL urlDaImagemParaBaixar = new URL(imageUrl);
            
            // Define o nome do arquivo que será salvo
            Path destino = Paths.get("imagem_do_dia.jpg");

            // Abre a conexão e copia o arquivo
            try (InputStream in = urlDaImagemParaBaixar.openStream()) {
                Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("Download concluído com sucesso! Imagem salva como: " + destino.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Falha ao baixar ou salvar a imagem: " + e.getMessage());
        }
        // --- FIM DA LÓGICA DE DOWNLOAD ---

    } catch (IOException | InterruptedException e) {
        // Este 'catch' trata erros da conexão com a API da NASA
        System.err.println("Falha ao se comunicar com a API da NASA: " + e.getMessage());
        e.printStackTrace();
    }
}
}
