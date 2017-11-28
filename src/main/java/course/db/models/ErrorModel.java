package course.db.models;

import course.db.views.ErrorView;

public class ErrorModel {
    private String message;

    public ErrorModel() {}

    public ErrorModel(String message) {this.message = message;}

    public ErrorView toView() {
        return new ErrorView(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
