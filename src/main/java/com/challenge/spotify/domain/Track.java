package com.challenge.spotify.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @ManyToOne
  @JoinColumn(name = "album_id")
  private Album album;

  private boolean isExplicit;

  private Integer playbackSeconds;

}
