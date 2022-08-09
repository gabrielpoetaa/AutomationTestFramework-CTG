import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TestaCliente {

    String enderecoApiCliente ="http://localhost:8080/";
    String enderecoCadastro ="cliente";
    String listaDeTodosClientes ="clientes";
    String apagaTodosClientes ="/apagaTodos";
    String listaVazia ="{}";


    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar, entao a lista deve estar vazia")
    public void quandoPegarListaSemCadastrarEntaoListaDeveEstarVazia (){

        deletaTodosClientes();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(enderecoApiCliente)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(new IsEqual<>(listaVazia));

    }

    @Test
    @DisplayName("Quando cadastrar um cliente, entao ele deve estar disponivel no resultado")
    public void cadastraClientes() {

        deletaTodosClientes();

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1001,\n" +
                "  \"idade\": 32,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"1001\":{\"nome\":\"Mickey Mouse\",\"idade\":32,\"id\":1001,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON) // dado que eu tenho um conteudo do tipo JSON para mandar para a API RESTful que aceita JSON
                .body(clienteParaCadastrar) // quando eu mando meu cliente dessa string
                .when()
                .post(enderecoApiCliente+enderecoCadastro) // quando eu criar meu cliente para o meu endpoint
                .then()
                .statusCode(201) // verificar se o codigo vai ser 201
                .assertThat().body(containsString(respostaEsperada)); // verificar se o corpo sera o dessa string

    }


    @Test
    @DisplayName("Quando eu atualizar um cliente, entao a lista sera atualizada")
    public void atualizaCliente(){

        deletaTodosClientes();

        String clienteParaCadastrar ="{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 32,\n" +
                "  \"nome\": \"Minney Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String clienteAtualizado ="{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 55,\n" +
                "  \"nome\": \"Minney Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada ="{\"1002\":{\"nome\":\"Minney Mouse\",\"idade\":55,\"id\":1002,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(enderecoApiCliente+enderecoCadastro)
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(clienteAtualizado)
                .when()
                .put(enderecoApiCliente+enderecoCadastro)// a documentacao diz que o endpoint para PUT eh /cliente
                .then()
                .statusCode(200)
                .assertThat().body(containsString(respostaEsperada));



    }

    @Test
    @DisplayName("Quando eu deletar um cliente, entao um cliente sera deletado da lista")
    public void deletaCliente(){

        String clienteDeletado ="http://localhost:8080/cliente/1002";

        String clienteParaCadastrar ="{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 32,\n" +
                "  \"nome\": \"Minney Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada ="CLIENTE REMOVIDO: { NOME: Minney Mouse, IDADE: 32, ID: 1002 }";

        given()
                .contentType(ContentType.JSON) // dado que eu tenho um conteudo do tipo JSON para mandar para a API RESTful que aceita JSON
                .body(clienteParaCadastrar) // quando eu mando meu cliente dessa string
                .when()
                .post(enderecoApiCliente+enderecoCadastro) // quando eu criar meu cliente para o meu endpoint
                .then()
                .statusCode(201); // verificar se o codigo vai ser 201

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(clienteDeletado)
                .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(respostaEsperada));

    }


    //Metodo de apoio
    public void deletaTodosClientes () {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(enderecoApiCliente+enderecoCadastro+apagaTodosClientes)
                .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(listaVazia));
    }
}