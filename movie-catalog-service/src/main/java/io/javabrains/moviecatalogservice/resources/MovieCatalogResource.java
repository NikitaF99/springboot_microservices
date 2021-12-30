package io.javabrains.moviecatalogservice.resources;

import com.netflix.discovery.DiscoveryClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import io.javabrains.moviecatalogservice.services.MovieInfo;
import io.javabrains.moviecatalogservice.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//How to make it REST... Just add the annotation
//So whenever a request is made, it checks the controller for any action to be done
@RestController

//Further to say springboot to treat this as api which is accessible at /catalog/something
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired //I am basically telling spring that somebody has a BEAN  somewhere, offsitre restTempltae//GET ME THT THING
    private RestTemplate restTemplate;

    //FOR WEB CLIENT
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}") //userId is a variable and it will passed

    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){



        //get all rated movie ID
        //For now assume that this is the response that you got from Ratings API

        UserRating ratings = userRatingInfo.getRatings(userId);




        //for each rating I should make a call to api, but for now its hardcoded
//        return ratings.stream().map(rating -> new CatalogItem("Transformers" , "Test", 4))
//                .collect(Collectors.toList());

        //Now lets sett how to get the name and description from API
        //make a call to movie infor

        return ratings.getRatings().stream().map(rating -> {
                    /**
                     * FRO WEB CLIENT
                     */
//                   Movie movie= webClientBuilder.build()
//                            .get()//get or put or post
//                            .uri("http://localhost:8082/movies/"+rating.getMovieId())
//                            .retrieve()
//                            .bodyToMono(Movie.class)//whatever body u get back, convert into instance of movie class
//                            .block();

            //so for each movie with a rating, im going to get the movieID and call the movie infor to get
            //more details about the movie.
            //returs as a movie class object
                    //
            // for each movie ID call movie info and get details

                    return movieInfo.getCatalogItem(rating);
                    //so now you can see we have connected 2 things together.
                //MovieInfo and ratings. Ratings is hardcoded



            })
                .collect(Collectors.toList());
        //put them together


    }

    public List<CatalogItem> getFallBackCatalog(@PathVariable("userId") String userId){
        return Arrays.asList(new CatalogItem("No movie", "", 0));//returning default list
    }
}
