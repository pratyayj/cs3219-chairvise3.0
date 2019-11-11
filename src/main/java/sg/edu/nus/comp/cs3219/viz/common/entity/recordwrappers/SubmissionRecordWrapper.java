package sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers;

import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmissionRecordWrapper {
    @JsonProperty("recordList")
    public List<SubmissionRecord> submissionRecordList;

    @JsonProperty("selectedConferenceId")
    public Long selectedConferenceId;

    public Long getSelectedConferenceId() {
        return selectedConferenceId;
    }

    public void setSelectedConferenceId(Long selectedConferenceId) {
        this.selectedConferenceId = selectedConferenceId;
    }

    public List<SubmissionRecord> getSubmissionRecordList() {
        return submissionRecordList;
    }

    public void setAuthorRecordList(List<SubmissionRecord> submissionRecordList) {
        this.submissionRecordList = submissionRecordList;
    }
}
