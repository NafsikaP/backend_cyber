package gr.hua.dit.aimodotes.demo.config;

import gr.hua.dit.aimodotes.demo.entity.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Aimodotis.class);
        config.exposeIdsFor(AppForm.class);
        config.exposeIdsFor(BloodTest.class);
        config.exposeIdsFor(DonationRequest.class);
        config.exposeIdsFor(Secretary.class);

    }
}
