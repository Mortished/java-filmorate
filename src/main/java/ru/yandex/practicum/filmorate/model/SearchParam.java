package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum SearchParam {

    DIRECTOR,
    TITLE;

    public static SearchParam getParam(String code) {
        switch (code) {
            case "director":
                return DIRECTOR;
            case "title":
                return TITLE;
            default:
                return null;
        }
    }

}
