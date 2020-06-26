package com.codegun.kakaopay.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
public class IdentifiedValueObject {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;
}
