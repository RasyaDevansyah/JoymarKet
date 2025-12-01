package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class OrderHistoryView extends BorderPane {

    public OrderHistoryView() {
        Label title = new Label("Order History");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.CENTER);
        setCenter(title);
    }
}
