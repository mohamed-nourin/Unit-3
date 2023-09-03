package com.javaunit3.springmvc;

import org.springframework.stereotype.Component;

@Component
public class TitanicMovie implements Movie {
    @Override
    public String getTitle() {
        return "Titanic";
    }

    @Override
    public String getMaturityRating() {
        return "PG-13";
    }

    @Override
    public String getGenre() {
        return "Romance";
    }
}