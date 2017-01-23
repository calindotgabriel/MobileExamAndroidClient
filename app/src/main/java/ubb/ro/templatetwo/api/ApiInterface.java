package ubb.ro.templatetwo.api;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;
import ubb.ro.templatetwo.Event;

/**
 * Created by calin on 23.01.2017.
 */

public interface ApiInterface {

    @GET("event")
    Observable<Response<List<Event>>> getEvents(@Header("If-Modified-Since") String ifModifiedSince);


    @GET("event")
    Observable<Response<List<Event>>> getEvents();

}
