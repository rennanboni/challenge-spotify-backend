package com.challenge.spotify.controller;

import com.challenge.spotify.dto.TrackDto;
import com.challenge.spotify.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/codechallenge")
@RequiredArgsConstructor
public class TrackController {

  private final TrackService trackService;

  @PostMapping("/createTrack")
  public Mono<ResponseEntity<TrackDto>> createTrack(@RequestParam String isrc) {
    return trackService.createTrack(isrc)
      .map(track -> ResponseEntity.ok(TrackDto.fromEntity(track)))
      .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.badRequest().body(null)));
  }

  @GetMapping("/getTrackMetadata")
  public ResponseEntity<TrackDto> getTrackMetadata(@RequestParam String isrc) {
    return trackService.getTrackMetadata(isrc)
      .map(track -> ResponseEntity.ok(TrackDto.fromEntity(track)))
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/getCover")
  public ResponseEntity<byte[]> getCover(@RequestParam String isrc) {
    return trackService.getTrackCover(isrc)
      .map(cover -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(cover))
      .orElse(ResponseEntity.notFound().build());
  }
}
