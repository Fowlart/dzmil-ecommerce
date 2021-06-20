package main;

import com.stripe.Stripe;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Launcher {

    static public String publishableKey = "";
    static public String secretKey = "";
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Launcher.class);
        app.run(args);
        Dotenv dotenv = Dotenv.load();
        logger.info("Launcher was started.");
        logger.info("Stripe API Version: " + Stripe.API_VERSION);
        publishableKey = dotenv.get("STRIPE_PUBLISHABLE_KEY");
        secretKey = dotenv.get("STRIPE_SECRET_KEY");
    }
}