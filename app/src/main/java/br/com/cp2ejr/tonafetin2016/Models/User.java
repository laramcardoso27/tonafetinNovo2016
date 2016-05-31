package br.com.cp2ejr.tonafetin2016.Models;

import java.io.Serializable;

/**
 * Created by Leonardo on 17/05/2016.
 */
public class User implements Serializable{
    private String id;       //ID user facebook.
    private String name;     //User name.
    private String photoUrl; //URL facebook photo.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
