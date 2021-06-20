package main.controllers.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import main.Launcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/stripe/metadata")
public class MetaData {


    @GetMapping(value = "/get-customer-with-meta")
    public ResponseEntity<String> getCustomerWithMeta() throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("some key", "some value");

        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                .setMetadata(metadata)
                .setName("Artur")
                .build();

        Customer artur = Customer.create(customerCreateParams, requestOptions);

        return ResponseEntity.ok().body(artur.toJson());
    }

    @GetMapping(value = "/update-customer-with-meta")
    public ResponseEntity<String> updateCustomerWithMeta(String customerId) throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("some new key", "some value");
        CustomerUpdateParams customerUpdateParams = CustomerUpdateParams.builder().putMetadata("new key new", "new val new").build();
        Customer artur = Customer.retrieve(customerId, requestOptions);
        artur = artur.update(customerUpdateParams,requestOptions);
        return ResponseEntity.ok().body(artur.toJson());
    }




}
