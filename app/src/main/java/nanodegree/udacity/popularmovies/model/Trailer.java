package nanodegree.udacity.popularmovies.model;

import java.net.URL;

/**
 * Created by Ahmd on 12/03/2018.
 */

public class Trailer {
    private String name;
    private URL link;

    public Trailer(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public Trailer(String name, URL link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public URL getLink() {
        return link;
    }
}
