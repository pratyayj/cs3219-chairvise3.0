import axios from 'axios';

export default {
    state: {
        conferenceRecordList: [],
        conferenceRecordListStatus: {
            isLoading: true,
            isApiError: false,
            apiErrorMsg: ''
        }
    },
    mutations: {
        setConferenceRecordListLoading(state, payload) {
            if (payload) {
                state.conferenceRecordListStatus.isApiError = false;
            }
            state.conferenceRecordListStatus.isLoading = payload;
        },
        setConferenceRecordListApiError(state, payload) {
            state.conferenceRecordListStatus.isApiError = true;
            state.conferenceRecordListStatus.apiErrorMsg = payload;
        },
        setConferenceRecordList(state, payload) {
            state.conferenceRecordList = payload;
        },
        resetConferenceRecordList(state) {
            state.conferenceRecordList = [];
        }
    },
    actions: {
        async getConferenceRecordList({commit}, conferenceId) {
            commit('setConferenceRecordListLoading', true);
            axios.get(`/api/conferenceRecords/${conferenceId}`)
                .then(response => {
                    commit('setConferenceRecordList', response.data)
                })
                .catch(e => {
                    commit('setConferenceRecordListApiError', e.toString());
                })
                .finally(() => {
                    commit('setConferenceRecordListLoading', false);
                });
        }
    }
}