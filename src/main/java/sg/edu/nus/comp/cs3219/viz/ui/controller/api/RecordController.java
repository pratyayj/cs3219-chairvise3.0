package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers.AuthorRecordWrapper;
import sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers.ReviewRecordWrapper;
import sg.edu.nus.comp.cs3219.viz.common.entity.recordwrappers.SubmissionRecordWrapper;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.logic.RecordLogic;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class RecordController extends BaseRestController {

    private GateKeeper gateKeeper;

    private RecordLogic recordLogic;

    public RecordController(GateKeeper gateKeeper, RecordLogic recordLogic) {
        this.gateKeeper = gateKeeper;
        this.recordLogic = recordLogic;
    }

    @PostMapping("/record/author")
    public ResponseEntity<?> importAuthorRecord(@RequestBody AuthorRecordWrapper authorRecordWrapper) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();
        this.recordLogic.removeAndPersistAuthorRecordForConference(userInfo.getUserEmail(), authorRecordWrapper.authorRecordList,
                authorRecordWrapper.selectedConferenceId);
        return ResponseEntity.created(new URI("/record/author")).build();
    }

    @PostMapping("/record/review")
    public ResponseEntity<?> importReviewRecord(@RequestBody ReviewRecordWrapper reviewRecordWrapper) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.removeAndPersistReviewRecordForConference(userInfo.getUserEmail(), reviewRecordWrapper.reviewRecordList,
                reviewRecordWrapper.selectedConferenceId);

        return ResponseEntity.created(new URI("/record/review")).build();
    }

    @PostMapping("/record/submission")
    public ResponseEntity<?> importSubmissionRecord(@RequestBody SubmissionRecordWrapper submissionRecordWrapper) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.removeAndPersistSubmissionRecordForConference(userInfo.getUserEmail(), submissionRecordWrapper.submissionRecordList,
                submissionRecordWrapper.selectedConferenceId);

        return ResponseEntity.created(new URI("/record/review")).build();
    }
}
