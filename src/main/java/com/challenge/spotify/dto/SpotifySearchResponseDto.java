package com.challenge.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifySearchResponseDto {
  private Tracks tracks;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Tracks {
    private SpotifyTrackDto[] items;
  }
}
