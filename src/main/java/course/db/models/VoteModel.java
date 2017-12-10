package course.db.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import course.db.views.VoteView;

public class VoteModel {
    String nickname;
    Integer vote;

    @JsonCreator
    public VoteModel(VoteView view) {
        this.nickname = view.getNickname();
        this.vote = view.getVote();
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getVote() {
        return vote;
    }
}
