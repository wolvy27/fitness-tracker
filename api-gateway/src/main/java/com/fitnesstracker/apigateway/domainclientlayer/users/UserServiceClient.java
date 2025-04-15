package com.fitnesstracker.apigateway.domainclientlayer.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.presentationlayer.users.UserResponseModel;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
import com.fitnesstracker.apigateway.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fitnesstracker.apigateway.utils.HttpErrorInfo;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String USER_SERVICE_URL;

    public UserServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                             @Value("${app.users-service.host}") String userServiceHost,
                             @Value("${app.users-service.port}") String userServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.USER_SERVICE_URL = "http://" + userServiceHost + ":" + userServicePort + "/api/v1/users";
    }

    public UserResponseModel getUserByUserId(String userId) {
        try {
            String url = USER_SERVICE_URL + "/" + userId;
            log.debug("Getting user by id: {}", userId);
            UserResponseModel userResponseModel = restTemplate.getForObject(url, UserResponseModel.class);
            return userResponseModel;
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<UserResponseModel> getUsers() {
        try{
            String url = USER_SERVICE_URL;
            log.debug("Getting users");
            UserResponseModel[] userResponseModels = restTemplate.getForObject(url, UserResponseModel[].class);
            return Arrays.asList(userResponseModels);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
//include all possible responses from the client
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }
//add an if-statement for your domain-specific exception here
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }


}
