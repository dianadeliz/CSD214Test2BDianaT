package com.trios.csd214test2b_dianat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.Optional;

public class CrudController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        loadProducts();
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> showProductDetails(newSelection));
    }

    private void loadProducts() {
        productList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Products");
            while (rs.next()) {
                Product product = new Product();
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                productList.add(product);
            }
            productTable.setItems(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showProductDetails(Product product) {
        if (product != null) {
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        quantityField.clear();
    }

    public void addProduct() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Products (name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nameField.getText());
            stmt.setDouble(2, Double.parseDouble(priceField.getText()));
            stmt.setInt(3, Integer.parseInt(quantityField.getText()));
            stmt.executeUpdate();
            loadProducts();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE Products SET price = ?, quantity = ? WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, Double.parseDouble(priceField.getText()));
            stmt.setInt(2, Integer.parseInt(quantityField.getText()));
            stmt.setString(3, selected.getName());
            stmt.executeUpdate();
            loadProducts();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Product");
        confirm.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM Products WHERE name = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, selected.getName());
                stmt.executeUpdate();
                loadProducts();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


