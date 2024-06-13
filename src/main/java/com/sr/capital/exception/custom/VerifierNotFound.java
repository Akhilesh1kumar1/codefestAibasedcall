package com.sr.capital.exception.custom;

public class VerifierNotFound extends CustomException {
    public VerifierNotFound() {
        super("Verifier not found for the task!");
    }
}
