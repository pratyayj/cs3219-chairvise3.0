package sg.edu.nus.comp.cs3219.viz.storage.repository;

import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.ConferenceRecord;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRecordRepository extends JpaRepository<ConferenceRecord, Long> {

    List<ConferenceRecord> findConferenceRecordsByConference(Conference conference);

}
