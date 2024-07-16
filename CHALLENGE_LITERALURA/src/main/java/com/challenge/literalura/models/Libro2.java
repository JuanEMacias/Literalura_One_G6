package com.challenge.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Libro2(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<Autor2> autor,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Double numeroDeDescargas
) {
}
