package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.*;
import com.fonyou.studentexam.exceptions.BusinessException;
import com.fonyou.studentexam.payload.request.ExamResponsesRequest;
import com.fonyou.studentexam.payload.response.ExamGradeResponse;
import com.fonyou.studentexam.repositories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SubmitExamResponsesTest {
    @Mock
    private ExamRepository examRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private ExamScheduleRepository examScheduleRepository;
    @Mock
    private ExamGradeRepository examGradeRepository;
    @Mock
    private StudentResponseRepository studentResponseRepository;
    @InjectMocks
    private ExamService examService;

    @Test
    @DisplayName("Should throw BusinessException when examScheduleId is invalid")
    void testSubmitExamResponses_throwsExamScheduleNotFound() {
        when(examScheduleRepository.findById(anyLong())).thenReturn(Optional.empty());
        List<ExamResponsesRequest> emptyExamResponsesRequestList = new ArrayList<>();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> examService.submitExamResponses(1L, emptyExamResponsesRequestList));
        assertEquals("Exam scheduled has not been found", exception.getMessage());
    }

    @Test
    void testSubmitExamResponses() {
        // Arrange
        Long examScheduleId = 1L;
        String studentTimeZone = "America/New_York";
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of(studentTimeZone));
        ZonedDateTime startDateTime = currentDateTime.minusHours(1);
        ZonedDateTime endDateTime = currentDateTime.plusHours(1);

        ExamScheduleEntity examScheduleEntity = new ExamScheduleEntity();
        examScheduleEntity.setId(examScheduleId);
        examScheduleEntity.setStudent(new StudentEntity(1L, "John Doe", 18, "Bogota", studentTimeZone));
        examScheduleEntity.setStartDateTime(startDateTime.withZoneSameInstant(ZoneId.of("America/Bogota")).toLocalDateTime());
        examScheduleEntity.setEndDateTime(endDateTime.withZoneSameInstant(ZoneId.of("America/Bogota")).toLocalDateTime());

        ExamEntity examEntity = new ExamEntity(1L, "Test Exam", new ArrayList<>());
        QuestionEntity question1 = new QuestionEntity(1L, examEntity, "Question 1", "A", "B", "C", "D", 2, 25);
        QuestionEntity question2 = new QuestionEntity(2L, examEntity, "Question 2", "A", "B", "C", "D", 4, 25);
        examEntity.setQuestions(List.of(question1, question2));
        examScheduleEntity.setExam(examEntity);

        ExamGradeEntity examGradeEntity = ExamGradeEntity.builder()
                .id(1L)
                .examSchedule(examScheduleEntity)
                .grade(25).build();

        when(examScheduleRepository.findById(examScheduleId)).thenReturn(Optional.of(examScheduleEntity));
        when(examGradeRepository.findByExamScheduleId(examScheduleId)).thenReturn(Optional.empty());
        when(examGradeRepository.save(any())).thenReturn(examGradeEntity);

        List<ExamResponsesRequest> examResponsesRequestList = List.of(
                new ExamResponsesRequest(question1.getId(), 2),
                new ExamResponsesRequest(question2.getId(), 3)
        );

        // Act
        ExamGradeResponse examGradeResponse = examService.submitExamResponses(examScheduleId, examResponsesRequestList);

        // Assert
        assertNotNull(examGradeResponse);
        assertEquals(25, examGradeResponse.getGrade());
        verify(studentResponseRepository, times(1)).saveAll(any());
        verify(examGradeRepository, times(1)).save(any());
    }

    @Test
    void testSubmitExamResponses_ExamDateTimeExpired() {
        // Arrange
        Long examScheduleId = 1L;
        String studentTimeZone = "America/New_York";
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of(studentTimeZone));
        ZonedDateTime startDateTime = currentDateTime.minusHours(2);
        ZonedDateTime endDateTime = currentDateTime.minusHours(1);

        ExamScheduleEntity examScheduleEntity = new ExamScheduleEntity();
        examScheduleEntity.setId(examScheduleId);
        examScheduleEntity.setStudent(new StudentEntity(1L, "John Doe", 18, "Bogota", studentTimeZone));
        examScheduleEntity.setStartDateTime(startDateTime.withZoneSameInstant(ZoneId.of("America/Bogota")).toLocalDateTime());
        examScheduleEntity.setEndDateTime(endDateTime.withZoneSameInstant(ZoneId.of("America/Bogota")).toLocalDateTime());

        ExamEntity examEntity = new ExamEntity(1L, "Test Exam", new ArrayList<>());
        QuestionEntity question1 = new QuestionEntity(1L, examEntity, "Question 1", "A", "B", "C", "D", 2, 25);
        QuestionEntity question2 = new QuestionEntity(2L, examEntity, "Question 2", "A", "B", "C", "D", 4, 25);
        examEntity.setQuestions(List.of(question1, question2));
        examScheduleEntity.setExam(examEntity);

        ExamGradeEntity examGradeEntity = ExamGradeEntity.builder()
                .id(1L)
                .examSchedule(examScheduleEntity)
                .grade(25).build();

        List<ExamResponsesRequest> examResponsesRequestList = List.of(
                new ExamResponsesRequest(question1.getId(), 2),
                new ExamResponsesRequest(question2.getId(), 3)
        );

        when(examScheduleRepository.findById(examScheduleId)).thenReturn(Optional.of(examScheduleEntity));
        when(examGradeRepository.findByExamScheduleId(examScheduleId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            examService.submitExamResponses(examScheduleId, examResponsesRequestList);
        });
        assertEquals("Exam date time has expired, please reschedule the exam", exception.getMessage());
    }

    @Test
    void testSubmitExamResponses_ExamAlreadySubmitted() {
        // Arrange
        Long examScheduleId = 1L;
        String studentTimeZone = "America/New_York";
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of(studentTimeZone));
        ZonedDateTime startDateTime = currentDateTime.minusHours(1);
        ZonedDateTime endDateTime = currentDateTime.plusHours(1);

        ExamScheduleEntity examScheduleEntity = new ExamScheduleEntity();
        examScheduleEntity.setId(examScheduleId);
        examScheduleEntity.setStudent(new StudentEntity(1L, "John Doe", 18, "Bogota", studentTimeZone));
        examScheduleEntity.setStartDateTime(startDateTime.withZoneSameInstant(ZoneId.of("America/Bogota")).toLocalDateTime());
        examScheduleEntity.setEndDateTime(endDateTime.withZoneSameInstant(ZoneId.of("America/Bogota")).toLocalDateTime());

        when(examScheduleRepository.findById(examScheduleId)).thenReturn(Optional.of(examScheduleEntity));
        when(examGradeRepository.findByExamScheduleId(examScheduleId)).thenReturn(Optional.of(new ExamGradeEntity()));

        List<ExamResponsesRequest> examResponsesRequestList = List.of(
                new ExamResponsesRequest(1L, 2),
                new ExamResponsesRequest(2L, 3)
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            examService.submitExamResponses(examScheduleId, examResponsesRequestList);
        });
        assertEquals("Exam has been submitted already", exception.getMessage());
    }
}
