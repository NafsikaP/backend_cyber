package gr.hua.dit.aimodotes.demo.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Sendgridconfig {

    @Value("${sendgrid.key}")
    private String appKey;

    @Bean
    public SendGrid sendGrid(){
        return new SendGrid(appKey);
    }
}