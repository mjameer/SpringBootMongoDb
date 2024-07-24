package com.mongo.collections;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    private String street;
    private String city;
    private String country;
    private Integer zip;
    private String addressType;
}
