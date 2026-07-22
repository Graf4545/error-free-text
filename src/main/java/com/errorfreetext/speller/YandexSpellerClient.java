package com.errorfreetext.speller;

import com.errorfreetext.exception.SpellerApiException;
import com.errorfreetext.speller.dto.SpellError;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class YandexSpellerClient {

    private static final Logger log = LoggerFactory.getLogger(YandexSpellerClient.class);
    private static final TypeReference<List<List<SpellError>>> RESPONSE_TYPE = new TypeReference<>() {
    };

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String checkTextsUrl;

    public YandexSpellerClient(
            RestTemplate spellerRestTemplate,
            ObjectMapper objectMapper,
            @Value("${yandex.speller.base-url}") String baseUrl
    ) {
        this.restTemplate = spellerRestTemplate;
        this.objectMapper = objectMapper;
        this.checkTextsUrl = baseUrl + "/checkTexts";
    }

    public List<List<SpellError>> checkTexts(List<String> texts, String lang, int options) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        texts.forEach(text -> body.add("text", text));
        body.add("lang", lang);
        body.add("options", String.valueOf(options));
        body.add("format", "plain");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(checkTextsUrl, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new SpellerApiException("Yandex Speller returned status: " + response.getStatusCode());
            }
            List<List<SpellError>> parsed = objectMapper.readValue(response.getBody(), RESPONSE_TYPE);
            return parsed != null ? parsed : List.of();
        } catch (SpellerApiException ex) {
            throw ex;
        } catch (RestClientException ex) {
            log.error("Failed to call Yandex Speller API", ex);
            throw new SpellerApiException("Failed to call Yandex Speller API: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Failed to parse Yandex Speller response", ex);
            throw new SpellerApiException("Failed to parse Yandex Speller response: " + ex.getMessage(), ex);
        }
    }

    public List<SpellError> checkText(String text, String lang, int options) {
        List<List<SpellError>> response = checkTexts(List.of(text), lang, options);
        if (response.isEmpty()) {
            return List.of();
        }
        return new ArrayList<>(response.get(0));
    }
}
