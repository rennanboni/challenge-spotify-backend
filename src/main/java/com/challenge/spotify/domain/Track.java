package com.challenge.spotify.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tracks")
public class Track {

  @Id
  @Column(nullable = false, unique = true)
  private String isrc;

  @Column(nullable = false)
  private String name;

  private String artistName;

  private String albumName;

  private String albumId;

  private boolean isExplicit;

  private Integer playbackSeconds;

  @Lob
  @Column(columnDefinition = "BYTEA")
  private byte[] coverImage;
}
