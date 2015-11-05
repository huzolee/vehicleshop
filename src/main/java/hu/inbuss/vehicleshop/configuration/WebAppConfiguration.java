package hu.inbuss.vehicleshop.configuration;

import hu.inbuss.vehicleshop.BasePackageScan;
import hu.inbuss.vehicleshop.interceptor.ThymeleafLayoutInterceptor;
import hu.inbuss.vehicleshop.util.CustomerFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = BasePackageScan.class)
public class WebAppConfiguration extends WebMvcConfigurerAdapter {

    private static final String ENCODING = "UTF-8";
    private static final String EXTENSION = ".html";
    private static final String TEMPLATE_MODE = "HTML5";
    private static final String LANGUAGE_PARAMETER = "lang";
    private static final String RESOURCE_HANDLER = "/resources/**";
    private static final String VIEWS_LOCATION = "/WEB-INF/views/";
    private static final String RESOURCE_LOCATION = "/WEB-INF/resources/";
    private static final String MESSAGE_SOURCE_LOCATION = "/WEB-INF/i18n/messages";

    @Autowired
    private CustomerFormatter customerFormatter;

    @Bean
    public ServletContextTemplateResolver viewTemplateResolver() {
        final ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix(VIEWS_LOCATION);
        templateResolver.setSuffix(EXTENSION);
        templateResolver.setTemplateMode(TEMPLATE_MODE);
        templateResolver.setCharacterEncoding(ENCODING);
        return templateResolver;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding(ENCODING);
        return viewResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(viewTemplateResolver());
        return engine;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(ENCODING);
        messageSource.setBasename(MESSAGE_SOURCE_LOCATION);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        final SessionLocaleResolver slr = new SessionLocaleResolver();
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        final LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName(LANGUAGE_PARAMETER);
        return lci;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new ThymeleafLayoutInterceptor());
    }

    @Override
    public Validator getValidator() {
        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCE_HANDLER).addResourceLocations(RESOURCE_LOCATION);
    }

    @Override
    public void addFormatters(final FormatterRegistry formatterRegistry) {
        formatterRegistry.addFormatter(customerFormatter);
    }
}
