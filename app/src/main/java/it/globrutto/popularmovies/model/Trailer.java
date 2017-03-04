package it.globrutto.popularmovies.model;

/**
 * Created by giuseppelobrutto on 04/03/17.
 */

public class Trailer {
    private String id;
    private String iso3166_1;
    private String ISO_639_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIso3166_1(String iso3166_1) {
        this.iso3166_1 = iso3166_1;
    }

    public String getIso3166_1() {
        return iso3166_1;
    }

    public void setISO_639_1(String ISO_639_1) {
        this.ISO_639_1 = ISO_639_1;
    }

    public String getISO_639_1() {
        return ISO_639_1;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSite() {
        return site;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + id + '\'' +
                ", iso3166_1='" + iso3166_1 + '\'' +
                ", ISO_639_1='" + ISO_639_1 + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }
}
