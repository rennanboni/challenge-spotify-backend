package com.challenge.spotify.service;

import com.challenge.spotify.dto.SpotifyAlbumDto;
import com.challenge.spotify.dto.SpotifySearchResponseDto;
import com.challenge.spotify.dto.SpotifyTokenResponseDto;
import com.challenge.spotify.dto.SpotifyTrackDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Optional;

@Service
public class SpotifyService {

  private final WebClient webClient;
  private final String clientId;
  private final String clientSecret;
  private String accessToken;

  public SpotifyService(
    WebClient.Builder webClientBuilder,
    @Value("${spotify.client-id}") String clientId,
    @Value("${spotify.client-secret}") String clientSecret
  ) {
    this.webClient = webClientBuilder.baseUrl("https://api.spotify.com/v1").build();
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  private Mono<String> getAccessToken() {
    if (accessToken != null) {
      return Mono.just(accessToken);
    }

    WebClient authClient = WebClient.builder().baseUrl("https://accounts.spotify.com/api/token").build();

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "client_credentials");

    return authClient.post()
      .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(BodyInserters.fromFormData(formData))
      .retrieve()
      .bodyToMono(SpotifyTokenResponseDto.class)
      .map(SpotifyTokenResponseDto::getAccessToken)
      .doOnNext(token -> this.accessToken = token);
  }

  public Mono<SpotifyTrackDto> searchTrackByIsrc(String isrc) {
    return getAccessToken().flatMap(token ->
      webClient.get()
        .uri(uriBuilder -> uriBuilder
          .path("/search")
          .queryParam("q", "isrc:" + isrc)
          .queryParam("type", "track")
          .build())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .retrieve()
        .bodyToMono(SpotifySearchResponseDto.class)
        .map(response -> {
          if (response.getTracks() != null && response.getTracks().getItems().length > 0) {
            return response.getTracks().getItems()[0];
          }
          return null;
        })
    );
  }

  public Mono<SpotifyAlbumDto> getAlbumById(String albumId) {
    return getAccessToken().flatMap(token ->
      webClient.get()
        .uri("/albums/{id}", albumId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .retrieve()
        .bodyToMono(SpotifyAlbumDto.class)
    );
  }

  public Mono<byte[]> getCoverImage(String url) {
    WebClient client = WebClient.create();
    return client.get()
      .uri(url)
      .retrieve()
      .bodyToMono(byte[].class);
  }
}
