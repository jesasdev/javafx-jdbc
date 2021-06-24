package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	@FXML 
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer>tableColunId;
	
	@FXML
	private TableColumn<Department, String>tableColunName;
	
	@FXML
	private Button BtNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("OI OI OI OI");
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializenodes();	
		
		
	}
	
	//inicia o comportamento das colunas
		private void initializenodes() {
			tableColunId.setCellValueFactory(new PropertyValueFactory<>("id"));
			tableColunName.setCellValueFactory(new PropertyValueFactory<>("name"));
			
			Stage stage = (Stage) Main.getMainScene().getWindow();
			//faz acompanhar a altura da janela
			tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		}

}
