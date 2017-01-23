package ubb.ro.templatetwo.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ubb.ro.templatetwo.Event;
import ubb.ro.templatetwo.list.EventViewInterface;
import ubb.ro.templatetwo.R;

/**
 * Created by calin on 23.01.2017.
 */
public class EventService {

    private static final int CODE_NOT_MODIFIED = 304;
    private static final String TAG = "TemplateApp";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    private final Retrofit mRetrofit;
    private String mLastModified;

    public EventService(Context context) {

        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        final Gson gson = gsonBuilder.create();


        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        final OkHttpClient okHttpClient = client.build();


        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(context.getString(R.string.api_url))
                .client(okHttpClient)
                .build();
    }

    public void fetchEventsAsync(final EventViewInterface view) {
        Date now = new Date();
        Log.d(TAG, "Last Modified : " + getLastModified());
        final ApiInterface api = mRetrofit.create(ApiInterface.class);
        Observable<Response<List<Event>>> events = api.getEvents();
        if (getLastModified() != null) {
            events = api.getEvents(getLastModified());
        }
        events.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Event>>>() {
                    @Override
                    public void onCompleted() {
                        view.showCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<Event>> events) {
                        if (events.code() == CODE_NOT_MODIFIED) {
                            Log.d(TAG, "304 Not modified!");
                            return;
                        }
                        final Headers headers = events.headers();
                        final String lastModified = headers.get(HEADER_LAST_MODIFIED);
                        setLastModified(lastModified);
                        view.showEvents(events.body());
                    }
                });
    }

    public void setLastModified(String lastModified) {
        this.mLastModified = lastModified;
    }

    public String getLastModified() {
        return mLastModified;
    }

}
