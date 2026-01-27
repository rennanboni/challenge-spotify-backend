package com.challenge.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTrackDto {

  private String name;

  @JsonProperty("explicit")
  private boolean isExplicit;

  @JsonProperty("duration_ms")
  private Integer durationMs;

  private Album album;

  private List<Artist> artists;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Album {
    private String id;
    private String name;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Artist {
    private String name;
  }
}
