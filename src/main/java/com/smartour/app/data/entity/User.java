package com.smartour.app.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartour.app.data.AbstractEntity;
import com.smartour.app.data.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import java.util.Set;

@Entity
@Getter
@Setter
public class User extends AbstractEntity {

    private String username;
    private String name;
    @JsonIgnore
    private String hashedPassword;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Lob
    private String profilePictureUrl;
}
