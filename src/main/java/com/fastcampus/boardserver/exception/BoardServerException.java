package com.fastcampus.boardserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.http.HttpClient;

@AllArgsConstructor
@Getter
@Setter
public class BoardServerException extends RuntimeException {

    HttpClient code;
    String msg;


}
