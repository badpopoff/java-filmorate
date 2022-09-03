package ru.yandex.practicum.fimorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private final long duration;
    private final String name;
    private final LocalDate releaseDate;
    private final String description;
}
