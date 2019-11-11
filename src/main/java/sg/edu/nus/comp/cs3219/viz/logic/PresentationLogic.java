package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationRepository;

import java.util.List;
import java.util.Optional;

@Component
public class PresentationLogic {

    private final PresentationRepository presentationRepository;
    private final ConferenceRepository conferenceRepository;

    public PresentationLogic(PresentationRepository presentationRepository, ConferenceRepository conferenceRepository) {
        this.presentationRepository = presentationRepository;
        this.conferenceRepository = conferenceRepository;
    }

    public List<Presentation> findAllForUser(UserInfo userInfo) {
        return presentationRepository.findByCreatorIdentifier(userInfo.getUserEmail());
    }

    public Optional<Presentation> findById(Long id) {
        return presentationRepository.findById(id);
    }

    public Presentation findByConference(Conference selectedConference) {
        return presentationRepository.findByConference(selectedConference);
    }

    public Presentation saveForUser(Presentation presentation, UserInfo userInfo, Long selectedPresentationConferenceId) {
        Presentation newPresentation = new Presentation();
        newPresentation.setName(presentation.getName());
        newPresentation.setDescription(presentation.getDescription());
        newPresentation.setCreatorIdentifier(userInfo.getUserEmail());
        Conference selectedConference = conferenceRepository.getOne(selectedPresentationConferenceId);
        newPresentation.setConference(selectedConference);

        return presentationRepository.save(newPresentation);
    }

    public Presentation updatePresentation(Presentation oldPresentation, Presentation newPresentation) {
        oldPresentation.setName(newPresentation.getName());
        oldPresentation.setDescription(newPresentation.getDescription());
        return presentationRepository.save(oldPresentation);
    }

    public void deleteById(Long id) {
        presentationRepository.deleteById(id);
    }
}
