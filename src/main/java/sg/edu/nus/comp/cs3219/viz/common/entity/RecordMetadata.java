package sg.edu.nus.comp.cs3219.viz.common.entity;

import sg.edu.nus.comp.cs3219.viz.common.entity.record.Exportable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Exportable(name = "Record Metadata", nameInDB = "record_metadata")
@Entity
public class RecordMetadata {

    @Id
    @GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "sg.edu.nus.comp.cs3219.viz.common.entity.UseExistingIdOtherwiseGenerateUsingIdentity")
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "rm_id")
    private Long id;

    @Column(name = "data_set")
    private String dataSet;

    @Column(name = "rm_conference_name")
    private String conferenceName;

    @Column(name = "rm_record_type")
    private String recordType;

    @Column(name = "rm_conference_year")
    private int conferenceYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
