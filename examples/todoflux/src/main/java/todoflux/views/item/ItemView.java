package todoflux.views.item;

import eu.lestard.fluxfx.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import todoflux.actions.ChangeStateForSingleItemAction;
import todoflux.actions.DeleteItemAction;
import todoflux.actions.EditAction;
import todoflux.actions.SwitchEditModeAction;
import todoflux.data.TodoItem;

public class ItemView implements View {

    public static final String STRIKETHROUGH_CSS_CLASS = "strikethrough";

    @FXML
    public Label contentLabel;

    @FXML
    public CheckBox completed;

    @FXML
    public TextField contentInput;

    @FXML
    public HBox root;
    @FXML
    public Button deleteButton;

    @FXML
    public HBox contentBox;

    private String id;


    public void initialize() {
        deleteButton.setVisible(false);
        root.setOnMouseEntered(event -> deleteButton.setVisible(true));
        root.setOnMouseExited(event -> deleteButton.setVisible(false));


        completed.setOnAction(event -> publishAction(new ChangeStateForSingleItemAction(id, completed.isSelected())));

        contentLabel.setOnMouseClicked(event -> {
            if(event.getClickCount() > 1) {
                publishAction(new SwitchEditModeAction(id, true));
            }
        });

        contentInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                publishAction(new SwitchEditModeAction(id, false));
            }
        });

        contentInput.setOnAction(event -> publishAction(new EditAction(id, contentInput.getText())));
    }

    public void update(TodoItem item) {
        id = item.getId();
        contentLabel.setText(item.getText());
        contentInput.setText(item.getText());
        completed.setSelected(item.isCompleted());
        if(item.isCompleted()) {
            contentLabel.getStyleClass().add(STRIKETHROUGH_CSS_CLASS);
        } else {
            contentLabel.getStyleClass().remove(STRIKETHROUGH_CSS_CLASS);
        }


        initEditMode(item.isEditMode());
    }

    private void initEditMode(boolean editMode){
        contentInput.setVisible(editMode);
        if(editMode) {
            contentInput.requestFocus();
        }
        contentBox.setVisible(!editMode);
        completed.setVisible(!editMode);
    }

    public void delete() {
        publishAction(new DeleteItemAction(id));
    }
}
