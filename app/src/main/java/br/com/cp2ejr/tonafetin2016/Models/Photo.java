package br.com.cp2ejr.tonafetin2016.Models;

/**
 * Created by Lara on 04/08/2016.
 */
public class Photo implements Comparable<Photo>{

    String id;
    String urlPhoto;
    String userInstagram;
    String legendPhoto;
    double createdTime;
    String tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUserInstagram() {
        return userInstagram;
    }

    public void setUserInstagram(String userInstagram) {
        this.userInstagram = userInstagram;
    }

    public String getLegendPhoto() {
        return legendPhoto;
    }

    public void setLegendPhoto(String legendPhoto) {
        this.legendPhoto = legendPhoto;
    }

    public double getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(double createdTime) {
        this.createdTime = createdTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int compareTo(Photo anotherPhoto) {

        if(this.getCreatedTime() > anotherPhoto.getCreatedTime()) {
            return -1;
        } else if(this.getCreatedTime() < anotherPhoto.getCreatedTime()) {
            return 1;
        } else {
            return 0;
        }

    }
}
