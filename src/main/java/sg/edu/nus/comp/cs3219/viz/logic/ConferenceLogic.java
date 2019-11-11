package sg.edu.nus.comp.cs3219.viz.logic;

import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class ConferenceLogic {

    private final ConferenceRepository conferenceRepository;

    public ConferenceLogic(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public List<Conference> findAllForUser(UserInfo userInfo) {
        return conferenceRepository.findByCreatorIdentifier(userInfo.getUserEmail());
    }

    public Optional<Conference> findById(Long id) {
        return conferenceRepository.findById(id);
    }

    public Conference saveForUser(Conference conference, UserInfo userInfo) {
        Conference newConference = new Conference();
        newConference.setConferenceName(conference.getConferenceName());
        newConference.setConferenceYear(conference.getConferenceYear());
        newConference.setCreatorIdentifier(userInfo.getUserEmail());
        return conferenceRepository.save(newConference);
    }

    public Conference updateConference(Conference oldConference, Conference newConference) {
        oldConference.setConferenceName(newConference.getConferenceName());
        oldConference.setConferenceYear(newConference.getConferenceYear());
        return conferenceRepository.save(oldConference);
    }

    public void deleteById(Long id) {
        conferenceRepository.deleteById(id);
    }
}
