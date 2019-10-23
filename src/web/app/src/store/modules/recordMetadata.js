/* eslint-disable */
import axios from 'axios';
import {deepCopy} from "@/common/utility"

export default {
    state: {
        recordMetadataForm: {
            conferenceName: 'default',
            conferenceYear: 1970,
        }
    },
    mutations: {
        setRecordMetadataForm(state, payload) {
            state.recordMetadataForm = payload;
        },

        setRecordMetadataFormField(state, {field, value}) {
            state.recordMetadataForm[field] = value;
        }
    }
}