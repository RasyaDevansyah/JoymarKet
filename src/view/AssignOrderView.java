package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class AssignOrderView extends BorderPane {

    public AssignOrderView() {
        Label title = new Label("Assign Order to Courier");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.CENTER);
        setCenter(title);
    }
}
