package com.fitnesstracker.apigateway.domainclientlayer.dailylogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.presentationlayer.dailylogs.DailyLogRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.dailylogs.DailyLogResponseModel;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
import com.fitnesstracker.apigateway.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
public class DailyLogServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String DAILYLOG_SERVICE_URL;

    public DailyLogServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                 @Value("${app.dailylogs-service.host}") String dailyLogServiceHost,
                                 @Value("${app.dailylogs-service.port}") String dailyLogServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.DAILYLOG_SERVICE_URL = "http://" + dailyLogServiceHost + ":" + dailyLogServicePort + "/api/v1";
    }

    public DailyLogResponseModel getDailyLogByDailyLogId(String dailyLogId, String userId) {
        try {
            String url = DAILYLOG_SERVICE_URL + "/" + userId + "/dailyLogs/" + dailyLogId;
            log.debug("Getting daily log by id: {}", dailyLogId);
            return restTemplate.getForObject(url, DailyLogResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<DailyLogResponseModel> getDailyLogs(String userId) {
        try {
            String url = DAILYLOG_SERVICE_URL + "/" + userId + "/dailyLogs";
            log.debug("Getting all daily logs for user: {}", userId);
            DailyLogResponseModel[] dailyLogs = restTemplate.getForObject(url, DailyLogResponseModel[].class);
            return Arrays.asList(dailyLogs);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public DailyLogResponseModel addDailyLog(DailyLogRequestModel dailyLogRequestModel, String userId) {
        try {
            String url = DAILYLOG_SERVICE_URL + "/" + userId + "/dailyLogs";
            log.debug("Adding daily log: {}", dailyLogRequestModel);
            return restTemplate.postForObject(url, dailyLogRequestModel, DailyLogResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public DailyLogResponseModel updateDailyLog(DailyLogRequestModel dailyLogRequestModel, String dailyLogId, String userId) {
        try {
            String url = DAILYLOG_SERVICE_URL + "/" + userId + "/dailyLogs/" + dailyLogId;
            log.debug("Updating daily log: {}", dailyLogId);
            HttpEntity<DailyLogRequestModel> requestEntity = new HttpEntity<>(dailyLogRequestModel);
            return restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
                    new ParameterizedTypeReference<DailyLogResponseModel>() {}).getBody();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteDailyLog(String dailyLogId, String userId) {
        try {
            String url = DAILYLOG_SERVICE_URL + "/" + userId + "/dailyLogs/" + dailyLogId;
            log.debug("Deleting daily log: {}", dailyLogId);
            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ioex.getMessage();
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}