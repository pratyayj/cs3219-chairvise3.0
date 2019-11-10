package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.ConferenceRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.ReviewRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionAuthorRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;
import sg.edu.nus.comp.cs3219.viz.common.util.Const;
import sg.edu.nus.comp.cs3219.viz.storage.repository.AuthorRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ReviewRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.SubmissionAuthorRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.SubmissionRecordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecordLogic {
    private AuthorRecordRepository authorRecordRepository;

    private SubmissionRecordRepository submissionRecordRepository;

    private SubmissionAuthorRecordRepository submissionAuthorRecordRepository;

    private ReviewRecordRepository reviewRecordRepository;

    private ConferenceRepository conferenceRepository;

    private ConferenceRecordRepository conferenceRecordRepository;

    public RecordLogic(AuthorRecordRepository authorRecordRepository,
                       SubmissionRecordRepository submissionRecordRepository,
                       SubmissionAuthorRecordRepository submissionAuthorRecordRepository,
                       ReviewRecordRepository reviewRecordRepository,
                       ConferenceRepository conferenceRepository,
                       ConferenceRecordRepository conferenceRecordRepository) {
        this.authorRecordRepository = authorRecordRepository;
        this.submissionRecordRepository = submissionRecordRepository;
        this.submissionAuthorRecordRepository = submissionAuthorRecordRepository;
        this.reviewRecordRepository = reviewRecordRepository;
        this.conferenceRepository = conferenceRepository;
        this.conferenceRecordRepository = conferenceRecordRepository;
    }

    @Transactional
    public void removeAndPersistAuthorRecordForConference(String dataSet, List<AuthorRecord> authorRecordList, Long selectedConferenceId) {
        Conference selectedConference = retrieveSelectedConference(selectedConferenceId);
        conferenceRecordRepository.deleteConferenceRecordByConferenceAndRecordType(selectedConference, Const.RecordTypes.AUTHOR_RECORD);
        authorRecordRepository.deleteAllByConferenceEquals(selectedConference);
        // authorRecordRepository.deleteAllByDataSetEquals(dataSet);
        authorRecordRepository.saveAll(authorRecordList.stream().peek(r -> {
            // should not set ID when creating records
            r.setId(null);
            // should set dataSet
            r.setDataSet(dataSet);
            r.setConference(selectedConference);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
        ConferenceRecord conferenceRecord = new ConferenceRecord(selectedConference, Const.RecordTypes.AUTHOR_RECORD);
        conferenceRecordRepository.save(conferenceRecord);
    }

    @Transactional
    public void removeAndPersistReviewRecordForConference(String dataSet, List<ReviewRecord> reviewRecordList, Long selectedConferenceId) {
        Conference selectedConference = retrieveSelectedConference(selectedConferenceId);
        conferenceRecordRepository.deleteConferenceRecordByConferenceAndRecordType(selectedConference, Const.RecordTypes.REVIEW_RECORD);
        reviewRecordRepository.deleteAllByConferenceEquals(selectedConference);
        // reviewRecordRepository.deleteAllByDataSetEquals(dataSet);
        reviewRecordRepository.saveAll(reviewRecordList.stream().peek(r -> {
            // should not set ID when creating records
            r.setId(null);
            // should set dataSet
            r.setDataSet(dataSet);
            r.setConference(selectedConference);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
        ConferenceRecord conferenceRecord = new ConferenceRecord(selectedConference, Const.RecordTypes.REVIEW_RECORD);
        conferenceRecordRepository.save(conferenceRecord);
    }

    @Transactional
    public void removeAndPersistSubmissionRecordForConference(String dataSet, List<SubmissionRecord> submissionRecordList, Long selectedConferenceId) {
        Conference selectedConference = retrieveSelectedConference(selectedConferenceId);
        conferenceRecordRepository.deleteConferenceRecordByConferenceAndRecordType(selectedConference, Const.RecordTypes.SUBMISSION_RECORD);
        removeSubmissionAndAuthorRecordsForConference(selectedConference);
        // submissionRecordRepository.deleteAllByDataSetEquals(dataSet);
        // submissionAuthorRecordRepository.deleteAllByDataSetEquals(dataSet);
        submissionRecordRepository.saveAll(submissionRecordList.stream().peek(s -> {
            // should not set ID when creating records
            s.setId(null);
            // should set dataSet
            s.setDataSet(dataSet);
            s.setConference(selectedConference);
            // create many to many relationship for authors
            List<SubmissionAuthorRecord> submissionAuthorRecords = s.getAuthors().stream()
                    .map(authorName -> {
                        SubmissionAuthorRecord existing = submissionAuthorRecordRepository.findFirstByNameEqualsAndDataSetEquals(authorName, dataSet);
                        if (existing == null) {
                            existing = new SubmissionAuthorRecord();
                            existing.setDataSet(dataSet);
                            existing.setName(authorName);
                            existing.setConference(selectedConference);
                            existing = submissionAuthorRecordRepository.save(existing);
                        }
                        return existing;
                    })
                    .collect(Collectors.toList());
            s.setAuthorSet(submissionAuthorRecords);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
        ConferenceRecord conferenceRecord = new ConferenceRecord(selectedConference, Const.RecordTypes.SUBMISSION_RECORD);
        conferenceRecordRepository.save(conferenceRecord);
    }

    // retrieve the selected conference that the record is to be tied to
    private Conference retrieveSelectedConference(Long selectedConferenceId) {
        return conferenceRepository.getOne(selectedConferenceId);
    }

    @Transactional
    public void removeSubmissionAndAuthorRecordsForConference(Conference selectedConference) {
        submissionRecordRepository.deleteAllByConferenceEquals(selectedConference);
        submissionAuthorRecordRepository.deleteAllByConferenceEquals(selectedConference);
    }
}
