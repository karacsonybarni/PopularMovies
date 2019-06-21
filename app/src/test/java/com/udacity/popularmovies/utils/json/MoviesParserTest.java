package com.udacity.popularmovies.utils.json;

import com.udacity.popularmovies.TestData;
import com.udacity.popularmovies.data.database.Movie;

import org.json.JSONException;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MoviesParserTest {

    @Test
    public void parse() throws JSONException {
        List<Movie> movies = Parser.parseMovies(TestData.moviesJSON);
        Movie aladdin = movies.get(0);
        assertThat(aladdin.getId()).isEqualTo(420817);
        assertThat(aladdin.getTitle()).isEqualTo("Aladdin");
        assertThat(aladdin.getPopularity()).isEqualTo(501.86);
    }
}