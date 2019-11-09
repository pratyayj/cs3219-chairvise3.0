package sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers;

import sg.edu.nus.comp.cs3219.viz.common.entity.record.ReviewRecord;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewRecordWrapper {
    @JsonProperty("recordList")
    public List<ReviewRecord> reviewRecordList;

    @JsonProperty("selectedConferenceId")
    public Long selectedConferenceId;

    public Long getSelectedConferenceId() {
        return selectedConferenceId;
    }

    public void setSelectedConferenceId(Long selectedConferenceId) {
        this.selectedConferenceId = selectedConferenceId;
    }

    public List<ReviewRecord> getReviewRecordList() {
        return reviewRecordList;
    }

    public void setReviewRecordList(List<ReviewRecord> reviewRecordList) {
        this.reviewRecordList = reviewRecordList;
    }
}
