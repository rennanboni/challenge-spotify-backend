package com.challenge.spotify.service;

import com.challenge.spotify.domain.Track;
import com.challenge.spotify.dto.SpotifyAlbumDto;
import com.challenge.spotify.dto.SpotifyTrackDto;
import com.challenge.spotify.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {

  private final TrackRepository trackRepository;
  private final SpotifyService spotifyService;

  public Mono<Track> createTrack(String isrc) {
    if (trackRepository.existsById(isrc)) {
      return Mono.error(new IllegalArgumentException("Track with ISRC " + isrc + " already exists."));
    }

    return spotifyService.searchTrackByIsrc(isrc)
      .flatMap(spotifyTrack -> {
        if (spotifyTrack == null) {
          return Mono.error(new IllegalArgumentException("Track not found for ISRC " + isrc));
        }
        return spotifyService.getAlbumById(spotifyTrack.getAlbum().getId())
          .flatMap(spotifyAlbum -> {
            String imageUrl = spotifyAlbum.getImages().stream()
              .findFirst()
              .map(SpotifyAlbumDto.Image::getUrl)
              .orElse(null);

            Mono<byte[]> coverImageMono = imageUrl != null ? spotifyService.getCoverImage(imageUrl) : Mono.just(new byte[0]);

            return coverImageMono.map(coverImage -> {
              Track track = Track.builder()
                .isrc(isrc)
                .name(spotifyTrack.getName())
                .isExplicit(spotifyTrack.isExplicit())
                .playbackSeconds(spotifyTrack.getDurationMs() != null ? spotifyTrack.getDurationMs() / 1000 : null)
                .albumName(spotifyTrack.getAlbum().getName())
                .albumId(spotifyTrack.getAlbum().getId())
                .artistName(spotifyTrack.getArtists().stream().map(SpotifyTrackDto.Artist::getName).collect(Collectors.joining(", ")))
                .coverImage(coverImage)
                .build();
              return trackRepository.save(track);
            });
          });
      });
  }

  public Optional<Track> getTrackMetadata(String isrc) {
    return trackRepository.findById(isrc);
  }

  public Optional<byte[]> getTrackCover(String isrc) {
    return trackRepository.findById(isrc).map(Track::getCoverImage);
  }
}
