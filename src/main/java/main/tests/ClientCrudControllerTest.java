package main.tests;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


class ClientCrudControllerTest {

    public ClientCrudControllerTest() {
    }

    void getClientTest() {

        Unirest.setTimeouts(0, 0);
        String textVar = "BUBA";

        try {
            HttpResponse<String> response = Unirest.get("http://localhost:8080/get-all-clients")
                    .asString();
            System.out.println(response.getBody()+" : "+textVar);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }
}