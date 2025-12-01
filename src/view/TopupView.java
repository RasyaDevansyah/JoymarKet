package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class TopupView extends BorderPane {

    public TopupView() {
        Label title = new Label("Top-up");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.CENTER);
        setCenter(title);
    }
}
