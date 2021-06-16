package main.controllers.stripe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(path = "/stripe/webhooks")
public class Webhooks {

    @PostMapping(value = "/all")
    public ResponseEntity getCustomerWithMeta(HttpServletRequest request) throws StripeException, IOException {
        String strCurrentLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((strCurrentLine = request.getReader().readLine()) != null) {
            stringBuilder.append(strCurrentLine);
        }
        System.out.println("> Received request: ");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Event event = gson.fromJson(stringBuilder.toString(), Event.class);
        System.out.println(event.getType());
        EventDataObjectDeserializer eventDataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (eventDataObjectDeserializer.getObject().isPresent()) {
            stripeObject = eventDataObjectDeserializer.getObject().get();
        }

        switch (event.getType()) {
            case "customer.created": {
                Customer customer = (Customer) stripeObject;
                System.out.println(customer);
                break;
            }
            default: {
                System.out.println("> unhandled event type!");
            }
        }

        return ResponseEntity.ok().build();
    }

}
