package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.sun.nio.sctp.IllegalReceiveException;

import db.DbException;
import gui.listeners.DatachangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable {
	// dependencia para o departamento

	private Department entity;

	private DepartmentService service;
    
	private List<DatachangeListener>dataChangeListeners = new ArrayList<>();
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	//método que inscreve objetos na lista
	public void subscribeDataChangeListener(DatachangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalReceiveException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			notifyDataChangeListeners();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close();
			
		}catch(ValidationException e) {
		
			setErrorMenseges(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saing object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for(DatachangeListener  listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}

	// méthodo responsável em pegar os dados do formulario e instanciar um
	// departamento
	private Department getFormData() {
		Department obj = new Department();
		ValidationException exception = new ValidationException("Validation Exception");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText()==null || txtName.getText().trim().equals("")) {
			exception.addError("name", "O Campo nao pode ser vazio");
		}
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size()>0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializableNodes();

	}

	private void initializableNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	// méthod que pega os dados do departamento e insere nas caixas de texto
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	
	private void setErrorMenseges(Map<String, String>errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
		
	}
}
