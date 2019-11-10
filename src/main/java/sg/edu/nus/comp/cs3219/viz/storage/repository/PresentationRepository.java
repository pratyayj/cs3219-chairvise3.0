package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;

import java.util.List;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {

    List<Presentation> findByCreatorIdentifier(String creatorIdentifier);

    void deleteByConference(Conference conference);

    void deleteAllByConference(Conference conference);

    Presentation findByConference(Conference conference);
}
