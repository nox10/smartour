package com.smartour.app.data.service;

import com.google.common.base.Splitter;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Workaround for spring-boot loading static resources issue
 * https://github.com/spring-projects/spring-boot/issues/13703
 */
//@Slf4j
@Component
public class TomcatStaticResourcesSupportWorkaround implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>, TomcatContextCustomizer {

    private Context context;
    @SuppressWarnings("squid:S1075")
    private static final String INTERNAL_PATH = "/META-INF/resources";

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers(this);
    }

    @Override
    public void customize(Context context) {
        this.context = context;
        Arrays.stream(ManagementFactory.getRuntimeMXBean().getClassPath().split(File.pathSeparator)).map(this::toUrl).filter(this::isPathingJar).flatMap(url -> pathingJarDependencies(url).stream()).filter(this::isNotDirectory) // resolving only jars
                .forEach(this::addByUrl);
    }

    // everything below is street magic, David Blaine
    private void addByUrl(URL url) {
        String path = url.getPath();
        if (path.endsWith(".jar") || path.endsWith(".jar!/")) {
            String jar = url.toString();
            if (!jar.startsWith("jar:")) {
                // A jar file in the file system. Convert to Jar URL.
                jar = "jar:" + jar + "!/";
            }
            addResourceSet(jar);
        } else {
            addResourceSet(url.toString());
        }
    }

    private void addResourceSet(String resource) {
        try {
            if (isInsideNestedJar(resource)) {
                // It's a nested jar but we now don't want the suffix because Tomcat
                // is going to try and locate it as a root URL (not the resource
                // inside it)
                resource = resource.substring(0, resource.length() - 2);
            }
            URL url = new URL(resource);
            this.context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, INTERNAL_PATH);
        } catch (Exception ex) {
            // Ignore
        }
    }

    private boolean isInsideNestedJar(String dir) {
        return dir.indexOf("!/") < dir.lastIndexOf("!/");
    }

    private List<URL> pathingJarDependencies(URL path) {
        List<URL> result = new ArrayList<>();
        try (JarFile jar = getJar(path)) {
            Manifest manifest = jar.getManifest();
            String classPath = manifest.getMainAttributes().getValue("Class-Path");
            Splitter.on(" ").omitEmptyStrings().split(classPath).forEach(classPathEntry -> {
                URL cpEntryUrl = toUrl(classPathEntry);
                result.add(cpEntryUrl);
            });
        } catch (IOException | URISyntaxException e) {
//            log.warn("Skipping pathing jar resource {} due to exception:", path.getPath(), e);
        }
        return result;
    }

    private boolean isNotDirectory(URL url) {
        try {
            return !Paths.get(url.toURI()).toFile().isDirectory();
        } catch (URISyntaxException e) {
            return true;
        }
    }

    private boolean isPathingJar(URL url) {
        try {
            try (JarFile jar = getJar(url)) {
                Manifest manifest = jar.getManifest();
                return manifest != null && manifest.getMainAttributes().getValue("Class-Path") != null;
            }
        } catch (IOException | URISyntaxException ex) {
            return false;
        }
    }

    private JarFile getJar(URL url) throws IOException, URISyntaxException {
        return new JarFile(Paths.get(url.toURI()).toFile());
    }

    private URL toUrl(String classPathEntry) {
        try {
            return resolveUrl(classPathEntry);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("URL could not be created from '" + classPathEntry + "'", ex);
        }
    }

    private URL resolveUrl(String classPathEntry) throws MalformedURLException {
        try {
            return new URL(classPathEntry);
        } catch (MalformedURLException e) {
            return Paths.get(classPathEntry).toFile().toURI().toURL();
        }
    }

}