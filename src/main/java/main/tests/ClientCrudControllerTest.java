package main.tests;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;


class ClientCrudControllerTest {



    @Test
    void getClientTest() {

        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("http://localhost:8080/get-all-clients")
                    .asString();
            System.out.println(response.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }
}