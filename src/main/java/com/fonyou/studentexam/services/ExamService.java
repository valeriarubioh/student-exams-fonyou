package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.*;
import com.fonyou.studentexam.exceptions.BusinessException;
import com.fonyou.studentexam.payload.request.ExamQuestionsRequest;
import com.fonyou.studentexam.payload.request.ExamResponsesRequest;
import com.fonyou.studentexam.payload.request.ExamScheduleRequest;
import com.fonyou.studentexam.payload.request.QuestionRequest;
import com.fonyou.studentexam.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final StudentResponseRepository studentResponseRepository;

    @Transactional
    public ExamEntity createExamQuestions(ExamQuestionsRequest examQuestionsRequestList) {
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
        questionRepository.saveAll(questionEntities);
        return examEntitySaved;
    }

    public ExamScheduleEntity createExamSchedule(ExamScheduleRequest examScheduleRequest) {
        ExamEntity examEntity = examRepository.findById(examScheduleRequest.getExamId())
                .orElseThrow(() -> new BusinessException("Exam id not found"));
        StudentEntity studentEntity = studentRepository.findById(examScheduleRequest.getStudentId())
                .orElseThrow(() -> new BusinessException("Student id not found"));

        ExamScheduleEntity examScheduleEntity = ExamScheduleEntity.builder()
                .exam(examEntity)
                .student(studentEntity)
                .startDateTime(examScheduleRequest.getStartDateTime())
                .endDateTime(examScheduleRequest.getEndDateTime())
                .build();

        return examScheduleRepository.save(examScheduleEntity);
    }

    public List<StudentResponseEntity> submitExamResponses(Long examScheduleId, List<ExamResponsesRequest> examResponsesRequestList) {
        ExamScheduleEntity examScheduleEntity = examScheduleRepository.findById(examScheduleId)
                .orElseThrow(() -> new BusinessException("Exam scheduled has not been found"));

        List<QuestionEntity> questionEntities = examScheduleEntity.getExam().getQuestions();

        List<StudentResponseEntity> studentResponseEntityList = examResponsesRequestList.stream().map(examResponsesRequest -> {
                    QuestionEntity questionEntityFound = questionEntities.stream()
                            .filter(questionEntity -> questionEntity.getId().equals(examResponsesRequest.getQuestionId()))
                            .findFirst()
                            .orElseThrow(() -> new BusinessException("Question id given not found in the Exam"));
                    return StudentResponseEntity.builder().examSchedule(examScheduleEntity)
                            .studentResponse(examResponsesRequest.getStudentResponse())
                            .examSchedule(examScheduleEntity)
                            .question(questionEntityFound)
                            .build();
                }
        ).toList();

        return studentResponseRepository.saveAll(studentResponseEntityList);
    }

    public ExamGradeEntity getExamGrade(Long examScheduleId) {
        return new ExamGradeEntity();
    }
}