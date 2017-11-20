package com.innoteva.model;

public class Image {

    private int imageId;
    private String image_Name;
    private String description;
    private String path;
    
    public Image() {
    	super();
    }
    
    public int getImageId() {
        return imageId;
    }
    
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    
    public String getName() {
        return image_Name;
    }
    
    public void setName(String image_Name) {
        this.image_Name = image_Name;
    }
    //
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public String toString() {
        return "Image [imageId=" + imageId + ", image_Name=" + image_Name
                + ", description=" + description + ", path=" + path;
    }    
}