package project.deepdot.medication.medicationtime.api;

import project.deepdot.medication.medicationtime.domain.MedicationTime;

import java.time.LocalTime;

// 응답 DTO
public record MedicationTimeResponse(Long id, LocalTime time) {
    public static MedicationTimeResponse from(MedicationTime mt) {
        return new MedicationTimeResponse(mt.getId(), mt.getTime());
    }
}