package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductController {
    @FXML
    private TableView<products> tableView;
    @FXML
    private TableColumn<products, Integer> idColumn;
    @FXML
    private TableColumn<products, String> nameColumn;
    @FXML
    private TableColumn<products, Double> priceColumn;
    @FXML
    private TableColumn<products, String> imageUrlColumn;

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        imageUrlColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        imageUrlColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (imageUrl == null || empty) {
                    setGraphic(null);
                } else {
                    try {
                        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                            Image image = new Image(imageUrl);
                            imageView.setImage(image);
                        } else {
                            File file = new File(imageUrl);
                            if (file.exists()) {
                                Image image = new Image(file.toURI().toString());
                                imageView.setImage(image);
                            } else {
                                setGraphic(null);
                                return;
                            }
                        }
                        imageView.setFitHeight(50); // Set the image height
                        imageView.setFitWidth(50);  // Set the image width
                        setGraphic(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                        setGraphic(null);
                    }
                }
            }
        });

        tableView.setItems(getProducts());
    }

    private ObservableList<products> getProducts() {
        ObservableList<products> products = FXCollections.observableArrayList();
        String query = "SELECT id, name, price, image_url FROM products";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String imageUrl = resultSet.getString("image_url");

                // Log the image URL
                System.out.println("Image URL: " + imageUrl);

                // Check if the file exists and is readable
                File file = new File(imageUrl);
                if (file.exists() && file.canRead()) {
                    System.out.println("File exists and is readable: " + imageUrl);
                } else {
                    System.out.println("File does not exist or is not readable: " + imageUrl);
                }

                products.add(new products(id, name, price, imageUrl));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}
