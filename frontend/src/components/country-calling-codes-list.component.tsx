import React, {Component} from "react";
import CountryCallingCodesApi from "../api/country-calling-codes.api";
import ErrorResponse from "../types/ErrorResponse.type";
import {AxiosError} from "axios";
import CountryCallingCodesResponse from "../types/CountryCallingCodesResponse.type";

type Props = {};

type State = {
    successResponse: CountryCallingCodesResponse[],
    failedResponse: ErrorResponse
};

export default class CountryCallingCodesList extends Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.getCountryCallingCodesList = this.getCountryCallingCodesList.bind(this);

        this.state = {
            successResponse: null as any,
            failedResponse: null as any,
        };
    }

    componentDidMount() {
        this.getCountryCallingCodesList();
    }

    getCountryCallingCodesList() {
        CountryCallingCodesApi.listCountryCallingCodes()
            .then((response: any) => {
                this.setState({
                    successResponse: response.data as CountryCallingCodesResponse[]
                });
            })
            .catch((error: AxiosError<ErrorResponse>) => {
                this.setState({
                    failedResponse: error.response?.data as ErrorResponse
                });
            });
    }

    render() {
        const {successResponse, failedResponse} = this.state;

        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12 mt-5">
                        <h1>Country Calling Codes</h1>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-10">
                        {successResponse &&
                            <table className="table table-hover">
                                <thead>
                                <tr>
                                    <th>Country</th>
                                    <th>Calling Codes</th>
                                </tr>
                                </thead>
                                <tbody>
                                {successResponse.map(country => (
                                    <tr>
                                        <td>
                                            {country.flagUrl &&
                                                <img src={country.flagUrl}
                                                     className="me-2"
                                                     alt={country.name}
                                                     title={country.name}/>
                                            }
                                            <span>{country.name}</span>
                                        </td>
                                        {country.codes &&
                                            <td>{country.codes.map(code => `+${code}`).join(", ")}</td>
                                        }
                                    </tr>
                                ))}

                                </tbody>
                            </table>
                        }
                        {failedResponse && failedResponse.errorMessage &&
                            <>
                                <h4 className="text-danger">Failed to fetch Country Calling Codes due to an error:</h4>
                                <p>{failedResponse.errorMessage}</p>
                            </>
                        }
                    </div>
                </div>
            </div>
        );
    }
}
