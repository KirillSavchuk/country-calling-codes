import React, {ChangeEvent, Component} from "react";
import CountryCallingCodesApi from "../api/country-calling-codes.api";
import ValidatePhoneNumberRequest from '../types/ValidatePhoneNumberRequest.type';
import ValidatePhoneNumberResponse from '../types/ValidatePhoneNumberResponse.type';
import ErrorResponse from "../types/ErrorResponse.type";
import {AxiosError} from "axios";

type Props = {};

type State = {
    phoneNumber: string,
    successResponse: ValidatePhoneNumberResponse,
    failedResponse: ErrorResponse
};

export default class ValidatePhoneNumber extends Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.onChangePhoneNumber = this.onChangePhoneNumber.bind(this);
        this.validatePhoneNumber = this.validatePhoneNumber.bind(this);
        this.handleKeypress = this.handleKeypress.bind(this);

        this.state = {
            phoneNumber: "",
            successResponse: null as any,
            failedResponse: null as any,
        };
    }

    onChangePhoneNumber(e: ChangeEvent<HTMLInputElement>) {
        const searchTitle = e.target.value;

        this.setState({
            phoneNumber: searchTitle
        });
    }

    handleKeypress(event: React.KeyboardEvent<HTMLInputElement>) {
        if (event.key === "Enter") {
            this.validatePhoneNumber();
        }
    };

    validatePhoneNumber() {
        const data: ValidatePhoneNumberRequest = {
            phoneNumber: this.state.phoneNumber
        };

        CountryCallingCodesApi.validatePhoneNumber(data)
            .then((response: any) => {
                this.setState({
                    successResponse: response.data
                });
            })
            .catch((error: AxiosError<ErrorResponse>) => {
                console.log(error.response?.data as ErrorResponse);
                this.setState({
                    failedResponse: error.response?.data as ErrorResponse
                });
            });
    }

    render() {
        const {phoneNumber, successResponse, failedResponse} = this.state;

        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-6 mt-5">
                        <h1>Phone Number Info</h1>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-6 mt-3 mb-3">
                        <div className="input-group mb-3">
                            <button
                                type="button"
                                className="btn btn-outline-info"
                                id="button-addon2"
                                onClick={this.validatePhoneNumber}
                            >
                                Get Info
                            </button>
                            <input type="text"
                                   className="form-control"
                                   placeholder="Please provide phone number..."
                                   value={phoneNumber}
                                   onChange={this.onChangePhoneNumber}
                                   onKeyPress={this.handleKeypress}
                            />
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-6">
                        {successResponse &&
                            <table className="table">
                                <thead>
                                <tr>
                                    <th>Country</th>
                                    <th>Calling Code</th>
                                </tr>
                                </thead>

                                <tbody>
                                <tr>
                                    <td>
                                        {successResponse.countryFlagUrl &&
                                            <img src={successResponse.countryFlagUrl}
                                                 className="me-2"
                                                 alt={successResponse.countryName}
                                                 title={successResponse.countryName}/>
                                        }
                                        <b>{successResponse.countryName}</b>
                                    </td>
                                    <td>{successResponse.countryCallingCode}</td>
                                </tr>
                                </tbody>
                            </table>
                        }
                        {failedResponse &&
                            <>
                                {failedResponse.errorMessage &&
                                    <>
                                        <h4 className="text-danger">Error Message:</h4>
                                        <p>{failedResponse.errorMessage}</p>
                                    </>
                                }
                                {failedResponse.violations &&
                                    <>
                                        <h4 className="text-danger">Input Violations:</h4>
                                        <ul>
                                            {failedResponse.violations.map(el => <li
                                                key={el.fieldName}>{el.message}</li>)}
                                        </ul>
                                    </>
                                }
                            </>
                        }
                    </div>
                </div>
            </div>
        );
    }
}
