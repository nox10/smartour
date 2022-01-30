package com.smartour.app.data.service;

import com.smartour.app.data.entity.map.Placemark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacemarkRepository extends JpaRepository<Placemark, Integer> {
}
