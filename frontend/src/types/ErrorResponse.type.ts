export default interface ErrorResponse {
    errorMessage?: string
    violations?: InputViolation[]
}

export interface InputViolation {
    fieldName: string
    message: string
}