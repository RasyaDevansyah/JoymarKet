package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class EditDeliveryStatusView extends BorderPane {

    public EditDeliveryStatusView() {
        Label title = new Label("Edit Delivery Status");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.CENTER);
        setCenter(title);
    }
}
