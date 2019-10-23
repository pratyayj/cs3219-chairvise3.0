package sg.edu.nus.comp.cs3219.viz.storage.repository;

import sg.edu.nus.comp.cs3219.viz.common.entity.RecordMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// TODO: add relevant methods as necessary to modify/retrieve information about metadata
public interface RecordMetadataRepository extends JpaRepository<RecordMetadata, Long> {

    List<RecordMetadata> findByDataSetEquals(String dataset);

    void deleteAllByConferenceNameEquals(String conferenceName);

}
