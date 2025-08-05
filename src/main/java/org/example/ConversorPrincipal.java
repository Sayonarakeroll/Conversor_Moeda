package org.example;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConversorPrincipal {
    private static final String API_KEY = "0b0781ece8bd9b69ef74a687";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    private static final Scanner scanner = new Scanner(System.in);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static final List<String> historicoConversoes = new ArrayList<>();


    private static final Map<String, String> nomesMoedas = Map.of(
            "USD", "Dólar Americano",
            "BRL", "Real Brasileiro",
            "EUR", "Euro",
            "GBP", "Libra Esterlina",
            "JPY", "Iene Japonês",
            "CAD", "Dólar Canadense",
            "AUD", "Dólar Australiano",
            "CHF", "Franco Suíço"
    );

    public static void main(String[] args) {
        exibirBanner();

        while (true) {
            exibirMenuPrincipal();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    menuConversoes();
                    break;
                case 2:
                    conversaoPersonalizada();
                    break;
                case 3:
                    exibirHistorico();
                    break;
                case 4:
                    exibirMoedasDisponiveis();
                    break;
                case 5:
                    System.out.println("Obrigado por usar o Conversor de Moedas!");
                    return;
                default:
                    System.out.println(" Opção inválida! Tente novamente.");
            }

            pausar();
        }
    }

    private static void exibirBanner() {
        System.out.println(" CONVERSOR DE MOEDAS! ");
        System.out.println("++++++++++++++++++++++");
    }

    private static void exibirMenuPrincipal() {
        System.out.println(" MENU PRINCIPAL");
        System.out.println("1️  Conversões Rápidas");
        System.out.println("2️  Conversão Personalizada");
        System.out.println("3️ Histórico de Conversões");
        System.out.println("4️  Moedas Disponíveis");
        System.out.println("5 Sair");
        System.out.print("➤ Escolha uma opção: ");
    }

    private static void menuConversoes() {
        System.out.println("\n CONVERSÕES RÁPIDAS");
        System.out.println("1) USD → BRL (Dólar para Real)");
        System.out.println("2) BRL → USD (Real para Dólar)");
        System.out.println("3) EUR → BRL (Euro para Real)");
        System.out.println("4) BRL → EUR (Real para Euro)");
        System.out.println("5) GBP → BRL (Libra para Real)");
        System.out.println("6) BRL → GBP (Real para Libra)");
        System.out.println("7) JPY → BRL (Iene para Real)");
        System.out.println("8) BRL → JPY (Real para Iene)");
        System.out.println("0) Voltar ao menu principal");
        System.out.print("➤ Escolha uma conversão: ");

        int opcao = lerOpcao();

        String[][] conversoes = {
                {"USD", "BRL"}, {"BRL", "USD"}, {"EUR", "BRL"}, {"BRL", "EUR"},
                {"GBP", "BRL"}, {"BRL", "GBP"}, {"JPY", "BRL"}, {"BRL", "JPY"}
        };

        if (opcao >= 1 && opcao <= 8) {
            String origem = conversoes[opcao - 1][0];
            String destino = conversoes[opcao - 1][1];
            realizarConversao(origem, destino);
        } else if (opcao != 0) {
            System.out.println(" Opção inválida!");
        }
    }

    private static void conversaoPersonalizada() {
        System.out.println("\n CONVERSÃO PERSONALIZADA");
        System.out.print("Digite a moeda de origem (ex: USD): ");
        String origem = scanner.nextLine().toUpperCase().trim();

        System.out.print("Digite a moeda de destino (ex: BRL): ");
        String destino = scanner.nextLine().toUpperCase().trim();

        if (origem.length() == 3 && destino.length() == 3) {
            realizarConversao(origem, destino);
        } else {
            System.out.println(" Códigos de moeda devem ter 3 letras!");
        }
    }

    private static void realizarConversao(String moedaOrigem, String moedaDestino) {
        String nomeOrigem = nomesMoedas.getOrDefault(moedaOrigem, moedaOrigem);
        String nomeDestino = nomesMoedas.getOrDefault(moedaDestino, moedaDestino);

        System.out.printf("\n Conversão: %s → %s\n", nomeOrigem, nomeDestino);
        System.out.printf("Digite o valor em %s: ", moedaOrigem);

        try {
            double valor = Double.parseDouble(scanner.nextLine());

            if (valor <= 0) {
                System.out.println(" Por favor, digite um valor positivo.");
                return;
            }

            System.out.println(" Consultando taxa de câmbio...");

            double taxa = obterTaxaCambio(moedaOrigem, moedaDestino);

            if (taxa > 0) {
                double valorConvertido = valor * taxa;

                System.out.println("\n RESULTADO DA CONVERSÃO");
                System.out.println("++++++++++++++++++++++++++++++++++");
                System.out.printf(" %.2f %s = %.2f %s\n",
                        valor, moedaOrigem, valorConvertido, moedaDestino);
                System.out.printf(" Taxa: 1 %s = %.6f %s\n",
                        moedaOrigem, taxa, moedaDestino);


                String registro = String.format("[%s] %.2f %s → %.2f %s (Taxa: %.6f)",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        valor, moedaOrigem, valorConvertido, moedaDestino, taxa);
                historicoConversoes.add(registro);

            } else {
                System.out.println(" Erro ao obter a taxa de câmbio. Verifique os códigos das moedas.");
            }

        } catch (NumberFormatException e) {
            System.out.println(" Valor inválido! Digite apenas números.");
        }
    }

    private static double obterTaxaCambio(String moedaOrigem, String moedaDestino) {
        try {
            String url = BASE_URL + moedaOrigem + "/" + moedaDestino;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Currency-Converter/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                if (jsonResponse.get("result").getAsString().equals("success")) {
                    return jsonResponse.get("conversion_rate").getAsDouble();
                } else {
                    System.out.println(" Erro na API: " +
                            jsonResponse.get("error-type").getAsString());
                }
            } else {
                System.out.println(" Erro HTTP: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(" Erro de conexão: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Erro inesperado: " + e.getMessage());
        }

        return -1;
    }

    private static void exibirHistorico() {
        System.out.println("\n HISTÓRICO DE CONVERSÕES");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++");

        if (historicoConversoes.isEmpty()) {
            System.out.println(" Nenhuma conversão realizada ainda.");
        } else {
            for (int i = historicoConversoes.size() - 1; i >= 0 && i >= historicoConversoes.size() - 10; i--) {
                System.out.println((historicoConversoes.size() - i) + ". " + historicoConversoes.get(i));
            }

            if (historicoConversoes.size() > 10) {
                System.out.println("... (mostrando últimas 10 conversões)");
            }
        }
    }

    private static void exibirMoedasDisponiveis() {
        System.out.println("\n MOEDAS DISPONÍVEIS");
        System.out.println("+++++++++++++++++++++++++++++++++++++++");

        nomesMoedas.forEach((codigo, nome) ->
                System.out.printf("%-4s - %s\n", codigo, nome));

        System.out.println("\n Dica: Você pode usar qualquer código de moeda de 3 letras na conversão personalizada!");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void pausar() {
        System.out.println("\n⏸  Pressione Enter para continuar...");
        scanner.nextLine();
        System.out.println();
    }
}