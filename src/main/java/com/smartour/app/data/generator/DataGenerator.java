package com.smartour.app.data.generator;

import com.smartour.app.data.Role;
import com.smartour.app.data.entity.User;
import com.smartour.app.data.entity.map.Placemark;
import com.smartour.app.data.service.PlacemarkRepository;
import com.smartour.app.data.service.SampleAddressRepository;
import com.smartour.app.data.service.UserRepository;
import com.smartour.app.util.PlacemarkConverter;
import com.smartour.app.util.xml.entity.XmlPlacemark;
import com.smartour.app.util.xml.parser.KmlParserHandler;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
@Slf4j
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, SampleAddressRepository sampleAddressRepository, UserRepository userRepository, PlacemarkRepository placemarkRepository) {
        return args -> {
            if (placemarkRepository.count() != 0L) {
                log.info("Using existing database");
                return;
            }

            placemarkRepository.saveAll(getPlacemarks());
            log.info("Placemarks loaded");

            // Generate everything else

            int seed = 123;

            log.info("Generating demo data");

//            logger.info("... generating 100 Sample Address entities...");
//            ExampleDataGenerator<SampleAddress> sampleAddressRepositoryGenerator = new ExampleDataGenerator<>(SampleAddress.class, LocalDateTime.of(2022, 1, 25, 0, 0, 0));
//            sampleAddressRepositoryGenerator.setData(SampleAddress::setId, DataType.ID);
//            sampleAddressRepositoryGenerator.setData(SampleAddress::setStreet, DataType.ADDRESS);
//            sampleAddressRepositoryGenerator.setData(SampleAddress::setPostalCode, DataType.ZIP_CODE);
//            sampleAddressRepositoryGenerator.setData(SampleAddress::setCity, DataType.CITY);
//            sampleAddressRepositoryGenerator.setData(SampleAddress::setState, DataType.STATE);
//            sampleAddressRepositoryGenerator.setData(SampleAddress::setCountry, DataType.COUNTRY);
//            sampleAddressRepository.saveAll(sampleAddressRepositoryGenerator.create(100, seed));

            log.info("... generating 2 User entities...");
            User user = new User();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);
            User admin = new User();
            admin.setName("Emma Powerful");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl("https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            admin.setRoles(Stream.of(Role.USER, Role.ADMIN).collect(Collectors.toSet()));
            userRepository.save(admin);

            log.info("Generated demo data");
        };


    }

    private List<Placemark> getPlacemarks() throws IOException {
        List<XmlPlacemark> xmlPlacemarks = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("/sampleplacemarks/**.kml");
        for (Resource resource : resources) {
            log.info("resource = " + resource);

            try (InputStream is = resource.getInputStream()) {
                SAXParser saxParser = factory.newSAXParser();

                // parse XML and map to object, it works, but not recommend, try JAXB
                KmlParserHandler handler = new KmlParserHandler();

                saxParser.parse(is, handler);

                List<XmlPlacemark> placemarks = handler.getResult();

//                List<XmlPlacemark> badPlacemarks = placemarks.stream().filter(p -> p.getPoint() == null).toList();
//                if (!badPlacemarks.isEmpty()) {
//                    System.out.println();
//                }

                List<XmlPlacemark> resultPlacemarks = placemarks.stream().filter(

                        // Not part of a route, actual placemark
                        p -> p.getExtendedData() != null

                                // Not a route coordinates set
                                && p.getPoint().getCoordinates().split(",").length == 3

                ).toList();
                resultPlacemarks.forEach(p -> p.setSource(resource.getFilename()));

                xmlPlacemarks.addAll(resultPlacemarks);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }
        return xmlPlacemarks.stream().map(PlacemarkConverter::apply).toList();
    }

}