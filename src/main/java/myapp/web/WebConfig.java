package myapp.web;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("--- addResourceHandlers");
		registry.addResourceHandler("/webjars/**")//
				.addResourceLocations("/webjars/");
	}

    @Override
// Pour activer les variables matrix
    public void configurePathMatch(PathMatchConfigurer configurer) {
        var urlPathHelper = new UrlPathHelper();
        // Nous gardons le contenu après le point virgule
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        var r = new ReloadableResourceBundleMessageSource();
        r.setBasenames("classpath:product", "classpath:messages");
        return r;
    }

    @Override
    public LocalValidatorFactoryBean getValidator() {
        var factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource());
        return factory;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor());
    }
}
