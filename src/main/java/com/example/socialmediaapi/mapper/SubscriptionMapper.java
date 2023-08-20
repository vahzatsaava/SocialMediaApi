package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.SubscriptionDto;
import com.example.socialmediaapi.model.Subscription;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionDto toDto(Subscription subscription);

    @InheritInverseConfiguration
    Subscription toEntity(SubscriptionDto subscriptionDto);
}
