package com.challenge.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyAlbumDto {
  private List<Image> images;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Image {
    private String url;
    private int height;
    private int width;
  }
}
