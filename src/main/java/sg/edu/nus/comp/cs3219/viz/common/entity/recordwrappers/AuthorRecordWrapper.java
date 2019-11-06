package sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers;

import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorRecordWrapper {

    @JsonProperty("authorRecordWrapper")
    public List<AuthorRecord> authorRecordList;

    @JsonProperty("recordMetadata")
    public Conference conference;

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public List<AuthorRecord> getAuthorRecordList() {
        return authorRecordList;
    }

    public void setAuthorRecordList(List<AuthorRecord> authorRecordList) {
        this.authorRecordList = authorRecordList;
    }
}
