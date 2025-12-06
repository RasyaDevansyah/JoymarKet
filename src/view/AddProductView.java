package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class AddProductView extends BorderPane {

    public AddProductView() {
        Label title = new Label("Add Product");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.CENTER);
        setCenter(title);
    }
}
