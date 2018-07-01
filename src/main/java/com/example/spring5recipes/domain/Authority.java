package com.example.spring5recipes.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode
public class Authority {

    @Id
    private String name;
}
