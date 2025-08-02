package project.deepdot.medication.medicationtime.api;

import java.time.LocalTime;

// 요청 DTO
public record MedicationTimeRequest(LocalTime time) {}
