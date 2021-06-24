package main.controllers.stripe.connected_accs;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import main.Launcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DirectChargeController {

    @GetMapping(value = "/direct-charge")
    public ResponseEntity<String> directCharge(@RequestParam String connectedStripeAccId) throws StripeException {
        Stripe.apiKey = Launcher.secretKey;
        ArrayList<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", paymentMethodTypes);
        params.put("amount", 1000);
        params.put("currency", "usd");
        // With Connect, your platform can take an application fee on direct charges.
        params.put("application_fee_amount", 123);
        RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(connectedStripeAccId).build();
        PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);
        return ResponseEntity.ok().body(paymentIntent.toJson());
    }

    @GetMapping(value = "/acc-info")
    public ResponseEntity<String> getConnectedAccInfo(@RequestParam String connectedStripeAccId) throws StripeException {
        Stripe.apiKey = Launcher.secretKey;
        Account account =
                Account.retrieve(connectedStripeAccId);
        return ResponseEntity.ok().body(account.toJson());
    }


    @GetMapping(value = "/create-acc")
    public ResponseEntity<String> createAcc() throws StripeException {
        Stripe.apiKey = Launcher.secretKey;

        Map<String, Object> cardPayments = new HashMap<>();
        cardPayments.put("requested", true);

        Map<String, Object> transfers = new HashMap<>();
        transfers.put("requested", true);

        //  Map<String, Object> capabilities = new HashMap<>();
        // capabilities.put("card_payments", cardPayments);
        // capabilities.put("transfers", transfers);
        //  params.put("capabilities", capabilities);

        Map<String, Object> business_profile = new HashMap<>();
        business_profile.put("name","Arturio Fernando del Cul");

        Map<String, Object> params = new HashMap<>();
        params.put("type", "standard");
        params.put("country", "US");
        params.put("email", "fowlartshevchenka@gmail.com");
        params.put("business_profile", business_profile);

        Account account = Account.create(params);

        Map<String, Object> params1 = new HashMap<>();
        params1.put("account", account.getId());
        params1.put(
                "refresh_url",
                "https://example.com/reauth"
        );
        params1.put(
                "return_url",
                "https://example.com/return"
        );
        params1.put("type", "account_onboarding");

        AccountLink accountLink =
                AccountLink.create(params1);

        return ResponseEntity.ok().body(accountLink.toJson());
    }

}
