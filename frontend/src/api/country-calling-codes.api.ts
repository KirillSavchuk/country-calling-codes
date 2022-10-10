import http from "./http-common";
import ValidatePhoneNumberRequest from "../types/ValidatePhoneNumberRequest.type"
import ValidatePhoneNumberResponse from "../types/ValidatePhoneNumberResponse.type";
import CountryCallingCodesResponse from "../types/CountryCallingCodesResponse.type";

const BASE_PATH: string = "api/v1/country-calling-codes";

class CountryCallingCodesApi {

    validatePhoneNumber(data: ValidatePhoneNumberRequest) {
        return http.post<ValidatePhoneNumberResponse>(`${BASE_PATH}/validate-phone-number`, data);
    }

    listCountryCallingCodes() {
        return http.get<Array<CountryCallingCodesResponse>>(`${BASE_PATH}/list`);
    }

}

export default new CountryCallingCodesApi();