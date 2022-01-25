package com.smartour.app.data.generator;

import com.smartour.app.data.Role;
import com.smartour.app.data.entity.SampleAddress;
import com.smartour.app.data.entity.User;
import com.smartour.app.data.service.SampleAddressRepository;
import com.smartour.app.data.service.UserRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, SampleAddressRepository sampleAddressRepository,
            UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (sampleAddressRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Address entities...");
            ExampleDataGenerator<SampleAddress> sampleAddressRepositoryGenerator = new ExampleDataGenerator<>(
                    SampleAddress.class, LocalDateTime.of(2022, 1, 25, 0, 0, 0));
            sampleAddressRepositoryGenerator.setData(SampleAddress::setId, DataType.ID);
            sampleAddressRepositoryGenerator.setData(SampleAddress::setStreet, DataType.ADDRESS);
            sampleAddressRepositoryGenerator.setData(SampleAddress::setPostalCode, DataType.ZIP_CODE);
            sampleAddressRepositoryGenerator.setData(SampleAddress::setCity, DataType.CITY);
            sampleAddressRepositoryGenerator.setData(SampleAddress::setState, DataType.STATE);
            sampleAddressRepositoryGenerator.setData(SampleAddress::setCountry, DataType.COUNTRY);
            sampleAddressRepository.saveAll(sampleAddressRepositoryGenerator.create(100, seed));

            logger.info("... generating 2 User entities...");
            User user = new User();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);
            User admin = new User();
            admin.setName("Emma Powerful");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            admin.setRoles(Stream.of(Role.USER, Role.ADMIN).collect(Collectors.toSet()));
            userRepository.save(admin);

            logger.info("Generated demo data");
        };
    }

}