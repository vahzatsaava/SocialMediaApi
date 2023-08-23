package com.example.socialmediaapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Social Media Application",
                version = "1.1.0",
                description = "This is a Test Media Application." +
                        "To register, send a POST request to /register with a JSON body containing valid username, email, and password.\n" +
                        "Then, use the registered credentials to log in by sending a POST request to /login and receiving a JWT token.\n" +
                        "Add the obtained JWT token to the 'Authorize' button at the top right corner.",
                contact = @Contact(
                        name = "Vazha Tsaava",
                        email = "vazhatsaava@gmail.com"
                )
        )
)
@SecurityScheme(name = "bearerAuth",
                description = "JwtAuth Description",
                scheme = "bearer",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER
)
public class SocialMediaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaApiApplication.class, args);
    }

}
