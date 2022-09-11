package ru.yandex.practicum.fimorate.model;

import org.springframework.stereotype.Component;

@Component
public class Generator {
    private int newId;

    public Generator() {
    }

    public int getNewId(){
        return ++newId;
    }

}
