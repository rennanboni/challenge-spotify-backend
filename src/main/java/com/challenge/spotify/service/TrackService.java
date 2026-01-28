package com.challenge.spotify.service;

import com.challenge.spotify.domain.Album;
import com.challenge.spotify.domain.Track;
import com.challenge.spotify.dto.SpotifyAlbumDto;
import com.challenge.spotify.dto.SpotifyTrackDto;
import com.challenge.spotify.repository.AlbumRepository;
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
  private final AlbumRepository albumRepository;
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
        
        Optional<Album> existingAlbum = albumRepository.findById(spotifyTrack.getAlbum().getId());

        Mono<Album> albumMono = existingAlbum.map(Mono::just).orElseGet(() ->
            spotifyService.getAlbumById(spotifyTrack.getAlbum().getId())
                .flatMap(spotifyAlbum -> {
                    String imageUrl = spotifyAlbum.getImages().stream()
                        .findFirst()
                        .map(SpotifyAlbumDto.Image::getUrl)
                        .orElse(null);
                    return spotifyService.getCoverImage(imageUrl)
                        .flatMap(imageBytes -> {
                            Album newAlbum = Album.builder()
                                .id(spotifyTrack.getAlbum().getId())
                                .name(spotifyTrack.getAlbum().getName())
                                .coverImage(imageBytes)
                                .build();
                            return Mono.just(albumRepository.save(newAlbum));
                        });
                })
        );
        
        return albumMono.flatMap(album -> {
            Track track = Track.builder()
              .isrc(isrc)
              .name(spotifyTrack.getName())
              .isExplicit(spotifyTrack.isExplicit())
              .playbackSeconds(spotifyTrack.getDurationMs() != null ? spotifyTrack.getDurationMs() / 1000 : null)
              .album(album)
              .artistName(spotifyTrack.getArtists().stream().map(SpotifyTrackDto.Artist::getName).collect(Collectors.joining(", ")))
              .build();
            return Mono.just(trackRepository.save(track));
        });
      });
  }

  public Optional<Track> getTrackMetadata(String isrc) {
    return trackRepository.findById(isrc);
  }

  public Mono<byte[]> getTrackCover(String isrc) {
    return Mono.justOrEmpty(trackRepository.findById(isrc))
        .map(track -> track.getAlbum().getCoverImage());
  }
}
