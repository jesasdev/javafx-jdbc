package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.SellerService;

public class SellerFormController implements Initializable {

	// dependencia para o departamento
	private Seller entity;

	private SellerService service;

	private List<DatachangeListener> dataChangeListeners = new ArrayList<>();
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	// método que inscreve objetos na lista
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

		} catch (ValidationException e) {

			setErrorMenseges(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saing object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DatachangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	// méthodo responsável em pegar os dados do formulario e instanciar um
	// departamento
	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException exception = new ValidationException("Validation Exception");
		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "O Campo nao pode ser vazio");
		}
		obj.setName(txtName.getText());

		if (exception.getErrors().size() > 0) {
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
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

	}

	// méthod que pega os dados do departamento e insere nas caixas de texto
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
	}

	private void setErrorMenseges(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}

	}
}
