package main.controllers.stripe;


import com.stripe.exception.*;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.*;
import main.Launcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/stripe")
public class StripeLab {

    @GetMapping(value = "/get-stripe-customer-list")
    public ResponseEntity<List<Customer>> getCustomerList() {

        /** Set api key globally **/
        //  Stripe.apiKey = secretKey;

        CustomerListParams params = CustomerListParams.builder().setLimit(3l).build();
        /** Set api key per request **/

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        /** Making requests on behalf of connected accounts **/

        /* RequestOptions requestOptions1 = RequestOptions.builder()
                .setStripeAccount("connectedStripeAccount") //connected stripe account id
                .setApiKey(secretKey)
                .build(); */

        try {
            CustomerCollection customers = Customer.list(params, requestOptions);
            System.out.println(customers);
            return ResponseEntity.ok().body(customers.getData());
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/get-stripe-customer-list-filtered")
    public ResponseEntity<List<Customer>> getCustomerListFiltered(@RequestParam String eMail) throws StripeException {

        CustomerListParams params = CustomerListParams.builder().setLimit(3l).setEmail(eMail).build();
        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();
        CustomerCollection customers = Customer.list(params, requestOptions);
        System.out.println(customers);
        return ResponseEntity.ok().body(customers.getData());
    }

    @GetMapping(value = "/errors-handling")
    public ResponseEntity errorsHandling() {
        String secretKey = "sk_test_51J2EO2EoAGESdlxhx4CU0lowJrfaIRqOYJAvddFeuAPiLdfA8P4AyaRhHx70q96vdBppHZknH6mymrbcK8IkHTUj00kZnHW3cj";
        CustomerListParams params = CustomerListParams.builder().build();


        RequestOptions requestOptions1 = RequestOptions.builder()
                .setStripeAccount("connectedStripeAccount") //connected stripe account id
                .setApiKey(secretKey)
                .build();

        try {
            // Use Stripe's library to make requests...
            CustomerCollection customers = Customer.list(params, requestOptions1);
            System.out.println(customers);
        } catch (CardException e) {
            // Since it's a decline, CardException will be caught
            System.out.println("Status is: " + e.getCode());
            System.out.println("Message is: " + e.getMessage());
        } catch (RateLimitException e) {
            // Too many requests made to the API too quickly
            System.out.println(e);
        } catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
            System.out.println(e);
        } catch (AuthenticationException e) {
            // Authentication with Stripe's API failed
            // (maybe you changed API keys recently)
            System.out.println(e);
        } catch (StripeException e) {
            // Display a very generic error to the user, and maybe send
            // yourself an email
            System.out.println(e);
        } catch (Exception e) {
            // Something else happened, completely unrelated to Stripe
            System.out.println(e);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/create-customer")
    public ResponseEntity<Customer> createCustomer() throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();
        Map<String, Object> params = new HashMap<>();
        params.put(
                "description",
                "My First Test Customer (created for API docs)"
        );
        Customer customer = Customer.create(params, requestOptions);
        System.out.println(customer);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/create-customer-2")
    public ResponseEntity<Customer> createCustomer2() throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                .setDescription("customer with only description")
                .build();

        Customer customer = Customer.create(customerCreateParams, requestOptions);

        System.out.println(customer);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/create-customer-3")
    public ResponseEntity<String> createCustomer3() throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                .setDescription("CustomerCreateParams.TaxExempt.EXEMPT")
                .setTaxExempt(CustomerCreateParams.TaxExempt.EXEMPT)
                .build();

        Customer customer = Customer.create(customerCreateParams, requestOptions);

        return ResponseEntity.ok().body(customer.toJson());
    }

    @GetMapping(value = "/create-customer-4")
    public ResponseEntity<String> createCustomer4() throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        /** this will allow customers to make recurring payments in test mode for a billing subscription and other invoices **/
        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                .setDescription("setDefaultPaymentMethod(pm_card_visa)")
                .setPaymentMethod("pm_card_visa")
                .setInvoiceSettings(CustomerCreateParams.InvoiceSettings.builder().setDefaultPaymentMethod("pm_card_visa").build())
                .build();

        Customer customer = Customer.create(customerCreateParams, requestOptions);

        return ResponseEntity.ok().body(customer.toJson());
    }

    @GetMapping(value = "/fetch-customer")
    public ResponseEntity<String> fetchCustomer(@RequestParam String id) throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        Customer customer = Customer.retrieve(id, requestOptions);

        System.out.println(customer);

        return ResponseEntity.ok().body(customer.toJson());
    }

    @GetMapping(value = "/update-customer-eMail")
    public ResponseEntity<String> updateCustomer(@RequestParam String id, String eMail) throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();
        Customer customer = Customer.retrieve(id, requestOptions);

        CustomerUpdateParams customerUpdateParams = CustomerUpdateParams.builder().setEmail(eMail).build();
        Customer updatedCustomer = customer.update(customerUpdateParams, requestOptions);

        return ResponseEntity.ok().body(updatedCustomer.toJson());
    }

    @GetMapping(value = "/update-customer-nested-params")
    public ResponseEntity<String> updateCustomerNestedParams(@RequestParam String id) throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();
        Customer customer = Customer.retrieve(id, requestOptions);

        CustomerUpdateParams customerUpdateParams = CustomerUpdateParams
                .builder()
                .setInvoiceSettings(
                        CustomerUpdateParams.InvoiceSettings.builder()
                                .addCustomField(CustomerUpdateParams.InvoiceSettings.CustomField.builder().setName("VAT").setValue("ABC123").build())
                                .build()).build();
        Customer updatedCustomer = customer.update(customerUpdateParams, requestOptions);

        return ResponseEntity.ok().body(updatedCustomer.toJson());
    }

    @GetMapping(value = "/delete-customer")
    public ResponseEntity<String> deleteCustomer(@RequestParam String id) throws StripeException {
        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();
        Customer customer = Customer.retrieve(id, requestOptions);
        Customer deletedCustomer = customer.delete(requestOptions);
        return ResponseEntity.ok().body(deletedCustomer.toJson());
    }

    @GetMapping(value = "/payment-intent")
    public ResponseEntity<String> paymentIntent() throws StripeException {

        PaymentIntentCreateParams params = PaymentIntentCreateParams
                .builder()
                .setAmount(1000l)
                .setCurrency("Usd")
                .build();

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        PaymentIntent intent = PaymentIntent.create(params, requestOptions);

        PaymentIntentConfirmParams paymentIntentConfirmParams = PaymentIntentConfirmParams
                .builder()
                .setPaymentMethod("pm_card_visa")
                .build();

        System.out.println(intent);

        PaymentIntent confirmedPaymentIntent = intent.confirm(paymentIntentConfirmParams, requestOptions);// separate call

        return ResponseEntity.ok().body(confirmedPaymentIntent.toJson());
    }

    //Todo: !!!!not work!!!!
    @GetMapping(value = "/nested-resource")
    public ResponseEntity<String> nestedResource(String customerCode) throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();

        InvoiceCreateParams invoiceCreateParams = InvoiceCreateParams
                .builder()
                .setDescription("first invoice")
                .setCustomer(customerCode)
                .build();

        Invoice newInvoice = Invoice.create(invoiceCreateParams, requestOptions);
        System.out.println(newInvoice);
        Invoice invoice = Invoice.retrieve(newInvoice.getId(), requestOptions);


        InvoiceLineItemCollectionListParams params = InvoiceLineItemCollectionListParams.builder()
                .setLimit(5l)
                .build();
        InvoiceLineItemCollection lines = invoice.getLines().list(params, requestOptions);
        return ResponseEntity.ok().body(lines.toJson());
    }

    @GetMapping(value = "/on-behalf-of")
    public ResponseEntity<String> onBehalfOf() throws StripeException {

        RequestOptions onBehalfOfRequestOpt = RequestOptions
                .builder()
                .setStripeAccount("lalala")
                .setApiKey(Launcher.secretKey)
                .build();

        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/create-session")
    public ResponseEntity<String> createSession(String customerId) throws StripeException {

        RequestOptions requestOptions = RequestOptions.builder().setApiKey(Launcher.secretKey).build();
        List<Object> paymentMethodTypes =
                new ArrayList<>();
        paymentMethodTypes.add("card");
        List<Object> lineItems = new ArrayList<>();
        Map<String, Object> lineItem1 = new HashMap<>();
        lineItem1.put("currency", "usd");
        lineItem1.put("quantity", 2);
        lineItem1.put("amount", 2);
        lineItem1.put("name","Artur");
        lineItems.add(lineItem1);
        Map<String, Object> params = new HashMap<>();
        params.put(
                "success_url",
                "https://example.com/success"
        );
        params.put(
                "cancel_url",
                "https://example.com/cancel"
        );
        params.put(
                "payment_method_types",
                paymentMethodTypes
        );
        params.put("line_items", lineItems);
        params.put("mode", "payment");
        Session session = Session.create(params, requestOptions);
        session.setCustomer(customerId);

        return ResponseEntity.ok().body(session.toJson());
    }

}

