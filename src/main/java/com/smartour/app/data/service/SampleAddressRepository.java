package com.smartour.app.data.service;

import com.smartour.app.data.entity.SampleAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleAddressRepository extends JpaRepository<SampleAddress, Integer> {

}