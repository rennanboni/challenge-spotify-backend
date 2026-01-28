package com.challenge.spotify.dto;

import com.challenge.spotify.domain.Track;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackDto {

  private String isrc;
  private String name;
  private String artistName;
  private String albumName;
  private String coverImage;
  private boolean isExplicit;
  private Integer playbackSeconds;

  public static TrackDto fromEntity(Track track) {
    return TrackDto.builder()
      .isrc(track.getIsrc())
      .name(track.getName())
      .artistName(track.getArtistName())
      .albumName(track.getAlbum().getName())
      .coverImage(track.getAlbum().getCoverImage())
      .isExplicit(track.isExplicit())
      .playbackSeconds(track.getPlaybackSeconds())
      .build();
  }
}
