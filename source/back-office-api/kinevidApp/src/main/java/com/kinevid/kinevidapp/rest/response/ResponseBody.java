package com.kinevid.kinevidapp.rest.response;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody<T>{

    String code = "000";
    String message;
    T data;
}
