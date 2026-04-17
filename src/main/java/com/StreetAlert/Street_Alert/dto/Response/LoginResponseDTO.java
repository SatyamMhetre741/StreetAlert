package com.StreetAlert.Street_Alert.dto.Response;

import lombok.Data;

@Data
public class LoginResponseDTO {
    String jwt;

    Long UserId;
}
