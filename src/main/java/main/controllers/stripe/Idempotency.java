package main.controllers.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/stripe/idempotency")
public class Idempotency {

    @GetMapping
    public ResponseEntity idempotencyTest(HttpServletRequest request) throws StripeException {

        Stripe.setMaxNetworkRetries(2);
        Map<String, Object> params = new HashMap<>();
        params.put("email", "foo@bar.com");
        // params.put("id", "tratatatata");
        RequestOptions options = new RequestOptions.RequestOptionsBuilder().setApiKey(StripeLab.secretKey).build();

        Customer customer = Customer.create(params, options);


        return ResponseEntity.ok().build();
    }

}
