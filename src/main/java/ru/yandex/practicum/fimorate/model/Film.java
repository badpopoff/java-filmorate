package ru.yandex.practicum.fimorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    private final long duration;
    private final String name;
    private final LocalDate releaseDate;
    private final String description;
    private final Set<Integer> idLikeUsers = new HashSet<>();
}
