package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	//acessa o estado aonde o controle que recebeu o evento est�
public static Stage currentStage(ActionEvent event) {
	return (Stage) ((Node)event.getSource()).getScene().getWindow();
}
}
