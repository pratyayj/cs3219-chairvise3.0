package sg.edu.nus.comp.cs3219.viz.logic;

import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.ConferenceRecord;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRecordRepository;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ConferenceRecordLogic {

    private final ConferenceRecordRepository conferenceRecordRepository;

    public ConferenceRecordLogic(ConferenceRecordRepository conferenceRecordRepository) {
        this.conferenceRecordRepository = conferenceRecordRepository;
    }

    public List<ConferenceRecord> findAllForConference(Conference selectedConference) {
        System.out.println("here in back end ");
        return conferenceRecordRepository.findConferenceRecordsByConference(selectedConference);
    }
}
