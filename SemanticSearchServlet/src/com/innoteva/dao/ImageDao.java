package com.innoteva.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.innoteva.model.Image;
import com.innoteva.util.DbUtil;

public class ImageDao {

    private Connection connection;

    public ImageDao() {
        connection = DbUtil.getConnection();
    }

    public void addImage(Image image) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into contacts(image_name, description, path) values (?, ?, ?)");
            // Parameters start with 1
            preparedStatement.setString(1, image.getName());
            preparedStatement.setString(2, image.getDescription());
            //preparedStatement.setDate(3, new java.sql.Date(image.getDob().getTime()));
            preparedStatement.setString(3, image.getPath());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteImage(int imageId) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from contacts where image_id=?");
            // Parameters start with 1
            preparedStatement.setInt(1, imageId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * This method update the image depending of the id assigned as new
	 * Recommendation: first retrieve the image to be updated and put the information inside and add the information
	 * with the same id
	 */
    public void updateImage(Image image) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update contacts set image_name=?, description=?, path=?" +
                            "where image_id=?");
            // Parameters start with 1
            preparedStatement.setString(1, image.getName());
            preparedStatement.setString(2, image.getDescription());
            //preparedStatement.setDate(3, new java.sql.Date(image.getDob().getTime()));
            preparedStatement.setString(3, image.getPath());
            preparedStatement.setInt(4, image.getImageId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Image> getAllImages() {
        List<Image> images = new ArrayList<Image>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from contacts");
            while (rs.next()) {
                Image image = new Image();
                image.setImageId(rs.getInt("image_id"));
                image.setName(rs.getString("image_name"));
                image.setDescription(rs.getString("description"));
                image.setPath(rs.getString("path"));
                //image.setEmail(rs.getString("email"));
                images.add(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return images;
    }

    public Image getImageById(int imageId) {
        Image image = new Image();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from contacts where image_id=?");
            preparedStatement.setInt(1, imageId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                image.setImageId(rs.getInt("image_id"));
                image.setName(rs.getString("image_name"));
                image.setDescription(rs.getString("description"));
                image.setPath(rs.getString("path"));
                //image.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return image;
    }
}