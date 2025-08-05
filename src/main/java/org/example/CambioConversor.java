package org.example;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CambioConversor {
    private static final String API_KEY = "0b0781ece8bd9b69ef74a687";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    private static final Scanner scanner = new Scanner(System.in);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("=== CONVERSOR DE MOEDAS ===");
        System.out.println("Bem-vindo ao seu conversor de moedas em tempo real!");
        System.out.println();

        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            if (opcao == 7) {
                System.out.println("Obrigado por usar o Conversor de Moedas! Até logo!");
                break;
            }

            processarOpcao(opcao);
            System.out.println();
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("Escolha uma opção de conversão:");
        System.out.println("1) Dólar (USD) → Real (BRL)");
        System.out.println("2) Real (BRL) → Dólar (USD)");
        System.out.println("3) Euro (EUR) → Real (BRL)");
        System.out.println("4) Real (BRL) → Euro (EUR)");
        System.out.println("5) Libra Esterlina (GBP) → Real (BRL)");
        System.out.println("6) Real (BRL) → Libra Esterlina (GBP)");
        System.out.println("7) Sair");
        System.out.print("Digite sua opção: ");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void processarOpcao(int opcao) {
        String moedaOrigem, moedaDestino;

        switch (opcao) {
            case 1:
                moedaOrigem = "USD";
                moedaDestino = "BRL";
                break;
            case 2:
                moedaOrigem = "BRL";
                moedaDestino = "USD";
                break;
            case 3:
                moedaOrigem = "EUR";
                moedaDestino = "BRL";
                break;
            case 4:
                moedaOrigem = "BRL";
                moedaDestino = "EUR";
                break;
            case 5:
                moedaOrigem = "GBP";
                moedaDestino = "BRL";
                break;
            case 6:
                moedaOrigem = "BRL";
                moedaDestino = "GBP";
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                return;
        }

        realizarConversao(moedaOrigem, moedaDestino);
    }

    private static void realizarConversao(String moedaOrigem, String moedaDestino) {
        System.out.printf("Digite o valor em %s: ", moedaOrigem);

        try {
            double valor = Double.parseDouble(scanner.nextLine());

            if (valor <= 0) {
                System.out.println("Por favor, digite um valor positivo.");
                return;
            }

            System.out.println("Consultando taxa de câmbio...");

            double taxa = obterTaxaCambio(moedaOrigem, moedaDestino);

            if (taxa > 0) {
                double valorConvertido = valor * taxa;

                System.out.println("=== RESULTADO DA CONVERSÃO ===");
                System.out.printf("%.2f %s = %.2f %s%n", valor, moedaOrigem, valorConvertido, moedaDestino);
                System.out.printf("Taxa de câmbio: 1 %s = %.4f %s%n", moedaOrigem, taxa, moedaDestino);
            } else {
                System.out.println("Erro ao obter a taxa de câmbio. Tente novamente.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Valor inválido! Digite apenas números.");
        }
    }

    private static double obterTaxaCambio(String moedaOrigem, String moedaDestino) {
        try {
            String url = BASE_URL + moedaOrigem + "/" + moedaDestino;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                if (jsonResponse.get("result").getAsString().equals("success")) {
                    return jsonResponse.get("conversion_rate").getAsDouble();
                } else {
                    System.out.println("Erro na resposta da API: " +
                            jsonResponse.get("error-type").getAsString());
                }
            } else {
                System.out.println("Erro HTTP: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        return -1;
    }
}
