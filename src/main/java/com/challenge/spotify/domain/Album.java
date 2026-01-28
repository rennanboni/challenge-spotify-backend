package com.challenge.spotify.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "albums")
public class Album {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    private String name;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(columnDefinition = "BYTEA")
    private byte[] coverImage;
}
