package com.javaunit3.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

public interface Movie {

    public String getTitle();

    public String getMaturityRating();

    public String getGenre();
}
