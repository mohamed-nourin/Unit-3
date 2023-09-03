package com.javaunit3.springmvc;

import com.javaunit3.springmvc.model.MovieEntity;
import com.javaunit3.springmvc.model.VoteEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MovieController {
    @Autowired
    private BestMovieService bestMovieService;

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @RequestMapping("/bestMovie")
    public String getBestMoviePage(Model model) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        movieEntityList.sort(Comparator.comparing(movieEntity -> movieEntity.getVotes().size()));

        MovieEntity bestMovie = movieEntityList.get(movieEntityList.size() - 1);
        ArrayList<String> voterNamesList = new ArrayList<>();

        for (VoteEntity vote : bestMovie.getVotes()) {
            voterNamesList.add(vote.getVoterName());
        }

        String voterNames = String.join(", ", voterNamesList);

        model.addAttribute("BestMovie", bestMovie.getTitle());
        model.addAttribute("BestMovieVoters", voterNames);

        return "bestMovie";
    }

    @RequestMapping("/voteForBestMovieForm")
    public String voteForBestMovieFormPage(Model model) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        session.getTransaction().commit();
        model.addAttribute("movies", movieEntityList);

        return "voteForBestMovie";
    }

    @RequestMapping("/voteForBestMovie")
    public String voteForBestMovie(HttpServletRequest request, Model model) {
        String voterName = request.getParameter("name-input");
        String movieId = request.getParameter("movie-id");

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        MovieEntity movieEntity = (MovieEntity) session.get(MovieEntity.class, Integer.parseInt(movieId));
        VoteEntity newVote = new VoteEntity();
        newVote.setVoterName(voterName);
        movieEntity.addVote(newVote);

        session.update(movieEntity);
        session.getTransaction().commit();

        return "voteForBestMovie";
    }

    @RequestMapping("/addMovieForm")
    public String addMovieForm() {
        return "addMovie";
    }

    @RequestMapping("/addMovie")
    public String addMovie(HttpServletRequest request, Model model) {
        String movieTitle = request.getParameter("add-movie-title");
        String maturityRating = request.getParameter("add-movie-maturity");
        String movieGenre = request.getParameter("add-movie-genre");

        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle(movieTitle);
        movieEntity.setMaturityRating(maturityRating);
        movieEntity.setGenre(movieGenre);

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(movieEntity);
        session.getTransaction().commit();

        return "addMovie";
    }
}