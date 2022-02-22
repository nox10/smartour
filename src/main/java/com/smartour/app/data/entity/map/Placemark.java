package com.smartour.app.data.entity.map;

import com.smartour.app.data.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Placemark extends AbstractEntity {
    private String name;
    private String normalizedName;

    @Column(length = 50000)
    private String description;

    @Column(length = 50000)
    private String normalizedDescription;

    @Column(length = 4000)
    private String styleUrl;

    @Column(length = 50000)
    private String data;

    private Double latitude;
    private Double longitude;
    private Double height;

    private String source;
}
