package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.*;
import com.fonyou.studentexam.exceptions.BusinessException;
import com.fonyou.studentexam.payload.request.ExamQuestionsRequest;
import com.fonyou.studentexam.payload.request.ExamResponsesRequest;
import com.fonyou.studentexam.payload.request.ExamScheduleRequest;
import com.fonyou.studentexam.payload.request.QuestionRequest;
import com.fonyou.studentexam.payload.response.ExamGradeResponse;
import com.fonyou.studentexam.payload.response.ExamResponse;
import com.fonyou.studentexam.payload.response.ExamScheduleResponse;
import com.fonyou.studentexam.payload.response.QuestionAndResponse;
import com.fonyou.studentexam.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ExamService {

    public static final String AMERICA_BOGOTA = "America/Bogota";
    private final ExamRepository examRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final StudentResponseRepository studentResponseRepository;
    private final ExamGradeRepository examGradeRepository;

    public static boolean isInRange(ZonedDateTime dateTime, ZonedDateTime startDate, ZonedDateTime endDate) {
        return dateTime.isAfter(startDate.minusNanos(1)) && dateTime.isBefore(endDate.plusNanos(1));
    }

    @Transactional
    public ExamResponse createExamQuestions(ExamQuestionsRequest examQuestionsRequestList) {
        ExamEntity examEntity = ExamEntity.builder()
                .examName(examQuestionsRequestList.getExamName())
                .build();

        ExamEntity examEntitySaved = examRepository.save(examEntity);

        List<QuestionEntity> questionEntities = new ArrayList<>();
        int totalScore = 0;

        for (QuestionRequest questionRequest : examQuestionsRequestList.getQuestions()) {
            totalScore += questionRequest.getScore();
            QuestionEntity questionEntity = QuestionEntity.builder()
                    .exam(examEntitySaved)
                    .question(questionRequest.getQuestion())
                    .choice1(questionRequest.getChoice1())
                    .choice2(questionRequest.getChoice2())
                    .choice3(questionRequest.getChoice3())
                    .choice4(questionRequest.getChoice4())
                    .correctChoice(questionRequest.getCorrectChoice())
                    .score(questionRequest.getScore())
                    .build();

            questionEntities.add(questionEntity);
        }
        if (totalScore != 100) {
            throw new BusinessException("The sum of scores for all questions should be 100");
        }
        List<QuestionEntity> questionEntitiesSaved = questionRepository.saveAll(questionEntities);

        return ExamResponse.builder()
                .id(examEntitySaved.getId())
                .examName(examEntitySaved.getExamName())
                .questions(questionEntitiesSaved)
                .build();
    }

    public ExamScheduleResponse createExamSchedule(ExamScheduleRequest examScheduleRequest) {
        ExamEntity examEntity = examRepository.findById(examScheduleRequest.getExamId())
                .orElseThrow(() -> new BusinessException("Exam id not found"));
        StudentEntity studentEntity = studentRepository.findById(examScheduleRequest.getStudentId())
                .orElseThrow(() -> new BusinessException("Student id not found"));

        List<ExamScheduleEntity> examScheduleEntityList = examScheduleRepository.findByExamAndStudent(examEntity, studentEntity);

        if (!examScheduleEntityList.isEmpty()) {
            throw new BusinessException("Student has scheduled this exam before, try with another exam");
        }

        ExamScheduleEntity examScheduleEntity = ExamScheduleEntity.builder()
                .exam(examEntity)
                .student(studentEntity)
                .startDateTime(examScheduleRequest.getStartDateTime())
                .endDateTime(examScheduleRequest.getEndDateTime())
                .build();

        ExamScheduleEntity save = examScheduleRepository.save(examScheduleEntity);
        return ExamScheduleResponse.builder()
                .id(save.getId())
                .idExam(save.getExam().getId())
                .examName(save.getExam().getExamName())
                .student(save.getStudent())
                .startDateTime(save.getStartDateTime())
                .endDateTime(save.getEndDateTime())
                .build();
    }

    @Transactional
    public ExamGradeResponse submitExamResponses(Long examScheduleId, List<ExamResponsesRequest> examResponsesRequestList) {
        ExamScheduleEntity examScheduleEntity = examScheduleRepository.findById(examScheduleId)
                .orElseThrow(() -> new BusinessException("Exam scheduled has not been found"));

        Optional<ExamGradeEntity> examGradeEntityOptional = examGradeRepository.findByExamScheduleId(examScheduleId);
        if (examGradeEntityOptional.isPresent()) {
            throw new BusinessException("Exam has been submitted already");
        }

        ZoneId studentZoneId = ZoneId.of(examScheduleEntity.getStudent().getCityTimeZone());
        ZonedDateTime studentCurrentDateTime = ZonedDateTime.now(studentZoneId);

        ZonedDateTime startDateTime = examScheduleEntity.getStartDateTime().atZone(ZoneId.of(AMERICA_BOGOTA));
        ZonedDateTime endDateTime = examScheduleEntity.getEndDateTime().atZone(ZoneId.of(AMERICA_BOGOTA));
        ZonedDateTime startDateTimeWithStudentTimeZone = startDateTime.withZoneSameInstant(studentZoneId);
        ZonedDateTime endDateTimeWithStudentTimeZone = endDateTime.withZoneSameInstant(studentZoneId);

        if (!isInRange(studentCurrentDateTime, startDateTimeWithStudentTimeZone, endDateTimeWithStudentTimeZone)) {
            log.info("{} is not within the range.", studentCurrentDateTime);
            throw new BusinessException("Exam date time has expired, please reschedule the exam");
        }
        log.info("{} is within the range of {} to {}", studentCurrentDateTime, startDateTimeWithStudentTimeZone, endDateTimeWithStudentTimeZone);

        List<QuestionEntity> questionEntities = examScheduleEntity.getExam().getQuestions();

        List<StudentResponseEntity> studentResponseEntityList = examResponsesRequestList.stream().map(examResponsesRequest -> {
                    QuestionEntity questionEntityFound = questionEntities.stream()
                            .filter(questionEntity -> questionEntity.getId().equals(examResponsesRequest.getQuestionId()))
                            .findFirst()
                            .orElseThrow(() -> new BusinessException("Question id given not found in the Exam"));
                    return StudentResponseEntity.builder()
                            .studentResponse(examResponsesRequest.getStudentResponse())
                            .examSchedule(examScheduleEntity)
                            .question(questionEntityFound)
                            .build();
                }
        ).toList();

        int totalScore = 0;
        for (StudentResponseEntity studentResponseEntity : studentResponseEntityList) {
            if (studentResponseEntity.getStudentResponse() == studentResponseEntity.getQuestion().getCorrectChoice()) {
                totalScore += studentResponseEntity.getQuestion().getScore();
            }
        }
        ExamGradeEntity examGradeEntity = ExamGradeEntity.builder()
                .examSchedule(examScheduleEntity)
                .grade(totalScore)
                .build();

        List<StudentResponseEntity> studentResponseEntities = studentResponseRepository.saveAll(studentResponseEntityList);
        ExamGradeEntity examGradeEntitySaved = examGradeRepository.save(examGradeEntity);

        return buildExamGradeResponse(questionEntities, studentResponseEntities, examGradeEntitySaved);
    }

    public ExamGradeResponse getExamGrade(Long examGradeId) {
        ExamGradeEntity examGradeEntity = examGradeRepository.findById(examGradeId).orElseThrow(() -> new BusinessException("Exam grade not found"));
        ExamScheduleEntity examScheduleEntity = examGradeEntity.getExamSchedule();
        List<StudentResponseEntity> studentResponseEntities = studentResponseRepository.findByExamSchedule(examScheduleEntity);
        List<QuestionEntity> questionEntities = examScheduleEntity.getExam().getQuestions();
        return buildExamGradeResponse(questionEntities, studentResponseEntities, examGradeEntity);
    }

    public ExamGradeResponse buildExamGradeResponse(List<QuestionEntity> questionEntities,
                                                    List<StudentResponseEntity> studentResponseEntities,
                                                    ExamGradeEntity examGradeEntity) {
        List<QuestionAndResponse> questionAndResponsesList = new ArrayList<>();
        for (QuestionEntity questionEntity : questionEntities) {
            for (StudentResponseEntity studentResponseEntity : studentResponseEntities) {
                if (questionEntity.getId().equals(studentResponseEntity.getQuestion().getId())) {
                    QuestionAndResponse questionAndResponse = QuestionAndResponse.builder()
                            .question(questionEntity.getQuestion())
                            .choice1(questionEntity.getChoice1())
                            .choice2(questionEntity.getChoice2())
                            .choice3(questionEntity.getChoice3())
                            .choice4(questionEntity.getChoice4())
                            .correctChoice(questionEntity.getCorrectChoice())
                            .studentResponse(studentResponseEntity.getStudentResponse())
                            .score(questionEntity.getScore())
                            .build();
                    questionAndResponsesList.add(questionAndResponse);
                    break;
                }
            }
        }
        return ExamGradeResponse.builder()
                .examGradeId(examGradeEntity.getId())
                .examScheduleId(examGradeEntity.getExamSchedule().getId())
                .grade(examGradeEntity.getGrade())
                .examName(examGradeEntity.getExamSchedule().getExam().getExamName())
                .questionsAndResponses(questionAndResponsesList)
                .build();
    }
}
