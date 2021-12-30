package io.javabrains.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {
    @Autowired
    private RestTemplate restTemplate;


    /**
     * TO ADD PROPERTIES TO HYSTRIX COMMAND
     * @param rating
     * @return
     */
//    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
//            commandProperties = {
//            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"),
//                    @HystrixProperty(name="circuiBreaker.requestVolumeThreshold", value="5"),
//                    @HystrixProperty(name="circuitBreaked.errorThresholdPercentage", value="50"),
//                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="5000")
//    })

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movies/"+ rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName() , "Test", rating.getRating());
    }

    public CatalogItem getFallbackCatalogItem(Rating rating){
        return new CatalogItem("Movie name not found" , "", rating.getRating());
    }

    /**
     * USING BULK HEAD
     * @param rating
     * @return
     */
    @HystrixCommand(fallbackMethod = "getFallbackCatalogItemBulkHead", threadPoolKey = "movieInfoPool", threadPoolProperties = {
            @HystrixProperty(name="coreSize", value="20"),
            @HystrixProperty(name="maxQueueSize" , value = "10")
    })
    public CatalogItem getCatalogItemBulkHead(Rating rating) {
        Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movies/"+ rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName() , "Test", rating.getRating());
    }

//similary do this for the other userRating method. We are creating 2 threadpools with unique keys. Then we are setting the thread pool size
    //coreSize=20 means thre shouldnt be more than 20 threads waiting for responsse from movieInfo API.
    //maxQueue=10 The requests that can be queued/ which are waiting even though they are not consuming any threads
}
