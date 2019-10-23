package sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers;

import sg.edu.nus.comp.cs3219.viz.common.entity.RecordMetadata;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorRecordWrapper {

    @JsonProperty("authorRecordWrapper")
    public List<AuthorRecord> authorRecordList;

    @JsonProperty("recordMetadata")
    public RecordMetadata recordMetadata;

    public RecordMetadata getRecordMetadata() {
        return recordMetadata;
    }

    public void setRecordMetadata(RecordMetadata recordMetadata) {
        this.recordMetadata = recordMetadata;
    }

    public List<AuthorRecord> getAuthorRecordList() {
        return authorRecordList;
    }

    public void setAuthorRecordList(List<AuthorRecord> authorRecordList) {
        this.authorRecordList = authorRecordList;
    }
}
