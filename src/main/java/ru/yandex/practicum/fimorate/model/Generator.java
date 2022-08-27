package ru.yandex.practicum.fimorate.model;

public class Generator {
    private int newId;
    public Generator() {
    }

    public int getNewId(){
        return ++newId;
    }
    public void setNewId(int Id) {
        if (newId < Id) {
            newId = Id;
        }
    }
}
