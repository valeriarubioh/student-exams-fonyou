package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.ExamEntity;
import com.fonyou.studentexam.entities.ExamScheduleEntity;
import com.fonyou.studentexam.entities.StudentEntity;
import com.fonyou.studentexam.exceptions.BusinessException;
import com.fonyou.studentexam.payload.request.ExamScheduleRequest;
import com.fonyou.studentexam.payload.response.ExamScheduleResponse;
import com.fonyou.studentexam.repositories.ExamRepository;
import com.fonyou.studentexam.repositories.ExamScheduleRepository;
import com.fonyou.studentexam.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateExamScheduleTest {
    @Mock
    private ExamRepository examRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ExamScheduleRepository examScheduleRepository;
    @InjectMocks
    private ExamService examService;


    @Test
    void shouldCreateExamScheduleWithValidInput() {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusHours(2);
        ExamEntity examEntity = new ExamEntity();
        examEntity.setId(1L);
        examEntity.setExamName("Test Exam");
        StudentEntity studentEntity = new StudentEntity(2L, "Pepe Perez", 18, "Tokio", "Asia/Tokyo");

        ExamScheduleRequest examScheduleRequest = ExamScheduleRequest.builder()
                .examId(1L)
                .studentId(2L)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();

        ExamScheduleEntity savedExamSchedule = new ExamScheduleEntity(1L, examEntity, studentEntity, startDateTime, endDateTime);

        when(examRepository.findById(1L)).thenReturn(Optional.of(examEntity));
        when(studentRepository.findById(2L)).thenReturn(Optional.of(studentEntity));
        when(examScheduleRepository.save(any(ExamScheduleEntity.class))).thenReturn(savedExamSchedule);

        ExamScheduleResponse response = examService.createExamSchedule(examScheduleRequest);

        assertEquals(savedExamSchedule.getId(), response.getId());
        assertEquals(examEntity.getId(), response.getIdExam());
        assertEquals(examEntity.getExamName(), response.getExamName());
        assertEquals(studentEntity, response.getStudent());
        assertEquals(startDateTime, response.getStartDateTime());
        assertEquals(endDateTime, response.getEndDateTime());
    }

    @Test
    void shouldThrowExceptionWhenExamEntityNotExist() {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusHours(2);
        long examId = 1L;
        long studentId = 2L;

        ExamScheduleRequest examScheduleRequest = ExamScheduleRequest.builder()
                .examId(examId)
                .studentId(studentId)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();

        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> examService.createExamSchedule(examScheduleRequest));
        assertEquals("Exam id not found", exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenStudentEntityNotExist() {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusHours(2);
        long examId = 1L;
        long studentId = 2L;

        ExamEntity examEntity = new ExamEntity();
        examEntity.setId(1L);
        examEntity.setExamName("Test Exam");

        ExamScheduleRequest examScheduleRequest = ExamScheduleRequest.builder()
                .examId(examId)
                .studentId(studentId)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();


        when(examRepository.findById(examId)).thenReturn(Optional.of(examEntity));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> examService.createExamSchedule(examScheduleRequest));
        assertEquals("Student id not found", exception.getMessage());
    }
}