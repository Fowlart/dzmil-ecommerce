package main.controllers.stripe.subscribtion;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import main.Launcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/stripe/subscriptions")
public class Subscriptions {

    @GetMapping(value = "/create-all-4-subscriptions")
    public ResponseEntity<Map<String, String>> createAll4Subscriptions() throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        Map<String, Object> productParams = new HashMap<>();
        productParams.put("name", "Basic");
        Product product = Product.create(productParams, requestOptions);

        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", "month");
        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("unit_amount", 2000);
        priceParams.put("currency", "USD");
        priceParams.put("recurring", recurring);
        priceParams.put("product", product.getId());
        priceParams.put("lookup_key", "sample_basic");

        Price price = Price.create(priceParams, requestOptions);
        Map<String, String> customResponse = new HashMap<>();

        customResponse.put("product", product.toJson());
        customResponse.put("price", price.toJson());

        return ResponseEntity.ok().body(customResponse);
    }

    @GetMapping(value = "/create-payment-intent")
    public ResponseEntity<Map<String,String>> createPaymentIntent() throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams
                .builder()
                .setAmount(1L)
                .setCurrency("usd")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams, requestOptions);

        Map<String,String> customResponse = new HashMap<>();
        customResponse.put("client secret 4 PaymentIntent",paymentIntent.getClientSecret());

        return ResponseEntity.ok().body(customResponse);
    }


}
