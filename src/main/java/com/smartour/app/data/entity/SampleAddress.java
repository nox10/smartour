package com.smartour.app.data.entity;

import com.smartour.app.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class SampleAddress extends AbstractEntity {

    private String street;
    private String postalCode;
    private String city;
    private String state;
    private String country;
}
