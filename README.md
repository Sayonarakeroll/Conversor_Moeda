# Conversor de Moedas em Java

##  Descrição do Projeto

Este é um conversor de moedas simples, desenvolvido em Java, para ser executado no console. O programa permite ao usuário converter valores entre moedas predefinidas ou personalizadas, visualizando a taxa de câmbio atual. O sistema utiliza uma API para obter as taxas de conversão em tempo real.

##  Tecnologias Utilizadas

* **Java 24**
* **Maven** para gerenciar as dependências do projeto.
* **Gson** : biblioteca para facilitar o processamento de respostas JSON vindas da API.
* **API ExchangeRate-API**: serviço de terceiros para obter taxas de câmbio atualizadas.

##  Como Executar o Projeto

1.  **Clone o Repositório**
    * No terminal, execute o comando para clonar o repositório do Git para a sua máquina local:
        `https://github.com/Sayonarakeroll/Conversor_Moeda.git`

2.  **Obtenha sua Chave de API**
    * Crie uma conta no site da [ExchangeRate-API](https://www.exchangerate-api.com/) para obter sua chave de API gratuita.

3.  **Configure o Código**
    * Abra o arquivo `src/main/java/org/example/ConversorPrincipal.java`.
    * Substitua o valor da constante `API_KEY` pela chave que você obteve. Por exemplo: `private static final String API_KEY = "SUA_CHAVE_AQUI";`.

4.  **Instale as Dependências, Compile e Execute**
    * Navegue até a pasta raiz do projeto (`Conversor_Moedas`).
    * Instale as dependências e compile o projeto usando o Maven com o comando:
        `mvn package`
  
