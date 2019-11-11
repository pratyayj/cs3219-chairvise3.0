package sg.edu.nus.comp.cs3219.viz.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PresentationWrapper {

    @JsonProperty("presentationForm")
    public Presentation presentationForm;

    @JsonProperty("selectedPresentationConferenceId")
    public Long selectedPresentationConferenceId;

    public Presentation getPresentationForm() {
        return presentationForm;
    }

    public void setPresentationForm(Presentation presentationForm) {
        this.presentationForm = presentationForm;
    }

    public Long getSelectedPresentationConferenceId() {
        return selectedPresentationConferenceId;
    }

    public void setSelectedPresentationConferenceId(Long selectedPresentationConferenceId) {
        this.selectedPresentationConferenceId = selectedPresentationConferenceId;
    }
}
