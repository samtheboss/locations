package Utils;

import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class utils {
    public static String genUpdateSQL(Object object, List<WhereBy> clauses, String table) {
        if (object == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for (Field f : object.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String col = f.getName();
            Object val = null;
            try {
                val = f.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (Objects.isNull(val)) {
                continue;
            }
            String str = Objects.toString(val);

            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(col).append(" = ");
            if (val instanceof String || val instanceof Timestamp) {
                sb.append("'").append(val).append("'");
            } else {
        sb.append(escape(str));
            }
        }

        return "UPDATE " + table + " SET " + sb + whereClauses(clauses);
    }
    public static String generateInsertSQL(Object object, String tableName) {
        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        boolean isFirst = true;
        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = field.getName();
            Object value;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                value = null;
            }
            if (value != null) {
                if (!isFirst) {
                    columnsBuilder.append(", ");
                    valuesBuilder.append(", ");
                }
                columnsBuilder.append(columnName);
                valuesBuilder.append(getFormattedValue(value));
                isFirst = false;
            }
        }
        return "INSERT INTO "
                + tableName
                + " ("
                + columnsBuilder
                + ") VALUES ("
                + valuesBuilder
                + ")";
    }
    private static String getFormattedValue(Object value) {
        if (value instanceof String || value instanceof Timestamp) {
            return "'" + value + "'";
        }
        return value.toString();
    }
    public void Notification(String title, String text, String type) {
        Notifications note = Notifications.create().title(title).text(text).position(Pos.CENTER);
        if ("error".equals(type)) {
            note.showError();
        } else {
            note.showInformation();
        }
    }
    public static boolean showConfirmationDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(buttonTypeNo) == buttonTypeYes;
    }
    public static void fillWithZeroIfEmpty(String defaultValue, JFXTextField... textFields) {
        for (JFXTextField textField : textFields) {
            textField.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
                if (!newFocus && textField.getText().isEmpty()) {
                    textField.setText(defaultValue);
                }
            });
        }
    }
    public static void restrictToNumbersOnly(JFXTextField... jfxTextFields) {
        for (JFXTextField jfxTextField : jfxTextFields) {
            String pattern = "[\\d,./]*";
      jfxTextField.setTextFormatter(
          new TextFormatter<>(
              change -> {
                String newText = change.getControlNewText();
                if (newText.matches(pattern)) {
                  return change;
                } else {
                  Label label = new Label("Digits Only");
                  PopOver popOver = new PopOver();
                  popOver.setTitle("");
                  popOver.setContentNode(label);
                  popOver.show(jfxTextField);
                  return null;
                }
              }));
        }
    }
    public static boolean isAnyTextFieldEmpty(JFXTextField... textFields) {
        for (JFXTextField textField : textFields) {
            if (textField.getText().isEmpty() || textField.getText() == null) {
                textField.getId();
                Label label = new Label("Fill this field");
                PopOver popOver = new PopOver();
                popOver.setTitle("");
                popOver.setContentNode(label);
                popOver.show(textField);
                textField.requestFocus();
                Notifications note = Notifications.create().title("Empty Field").text(textField.getId() + " is Empty");
                note.showError();
                return true;
            }
        }
        return false;
    }

    public static StringBuilder whereClauses(List<WhereBy> clauses) {
        StringBuilder sql = new StringBuilder("");
        if (clauses != null && !clauses.isEmpty()) {
            sql.append(" WHERE");
            int i = 0;
            for (WhereBy whereBy : clauses) {
                if (i > 0) {
                    sql.append(" AND");
                }
                sql.append(" ").append(whereBy.column);
                Object value = whereBy.value;
                if (null == whereBy.equate) {
                    sql.append(" ").append(" <> ").append(dbArg(value));
                } else {
                    switch (whereBy.equate) {
                        case EQUALS:
                            sql.append(" = ").append(dbArg(value));
                            break;
                        case GREATER_THAN:
                            sql.append(" > ").append(dbArg(value));
                            break;
                        case LESS_THAN:
                            sql.append(" < ").append(dbArg(value));
                            break;
                        case LIKE:
                            sql.append(" LIKE '%").append(escape(Objects.toString(value))).append("%'");
                            break;
                        case BETWEEN:
                            List<Object> l = new ArrayList<>((List<Object>) value);
                            sql.append(" BETWEEN ")
                                    .append(dbArg(l.get(0)))
                                    .append(" AND ")
                                    .append(dbArg(l.get(1)));
                            break;
                        default:
                            sql.append(" ").append(" <> ").append(dbArg(value));
                            break;
                    }
                }
                i++;
            }
        }
        return sql;
    }
    private static String dbArg(Object o) {
        if (o == null) {
            return "NULL";
        }
        if (o instanceof String || o instanceof Timestamp) {
            return "'" + o + "'";
        }
        return Objects.toString(o);
    }
    private static String escape(String s) {
        return s.replace("'", "''");
    }
}
