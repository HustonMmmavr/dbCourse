package course.db.views;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.criteria.CriteriaBuilder;

public class PostView implements AbstractView {
    Integer id;
    Integer parent;
    String author;
    String message;
    Boolean isEdited;
    String forum;
    Integer thread;
    String created;


}
