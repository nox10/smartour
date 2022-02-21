package com.smartour.app;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Configuration
@Theme(value = "smartour")
@PWA(name = "Smartour", shortName = "Smartour", offlineResources = {"images/logo.png"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static class JarTomcatServletWebServerFactory extends TomcatServletWebServerFactory {
        @Override
        protected void postProcessContext(Context context) {
            context.addLifecycleListener(new StaticResourceConfigurer(context));
        }

        private final class StaticResourceConfigurer implements LifecycleListener {

            private final Context context;

            private StaticResourceConfigurer(Context context) {
                this.context = context;
            }

            @Override
            public void lifecycleEvent(LifecycleEvent event) {
                if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                    URL resource = Application.class.getResource("/");
                    String url = resource.toExternalForm();
                    int index = url.indexOf("jar!/");
                    if (index >= 0) {
                        url = url.substring(0, index + 5);
                    }
                    try {
                        resource = new URL(url);
                    } catch (MalformedURLException exception) {
                        throw new RuntimeException(exception);
                    }
                    this.context.getResources().createWebResourceSet(ResourceSetType.RESOURCE_JAR, "/", resource, "/META-INF/resources");
                }
            }

        }

    }

    /*
     * This workaround is used to be able to use static web resources in
     * /META-INF/resources if application is executed as JAR ( `java -jar
     * packaged_web_app.jar`).
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        return new JarTomcatServletWebServerFactory();
    }

}
