package sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers;

import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorRecordWrapper {

    @JsonProperty("recordList")
    public List<AuthorRecord> authorRecordList;

    @JsonProperty("selectedConferenceId")
    public Long selectedConferenceId;

    public Long getSelectedConferenceId() {
        return selectedConferenceId;
    }

    public void setSelectedConferenceId(Long selectedConferenceId) {
        this.selectedConferenceId = selectedConferenceId;
    }

    public List<AuthorRecord> getAuthorRecordList() {
        return authorRecordList;
    }

    public void setAuthorRecordList(List<AuthorRecord> authorRecordList) {
        this.authorRecordList = authorRecordList;
    }
}
