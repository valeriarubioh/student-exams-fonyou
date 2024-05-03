package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.*;
import com.fonyou.studentexam.exceptions.BusinessException;
import com.fonyou.studentexam.payload.request.ExamQuestionsRequest;
import com.fonyou.studentexam.payload.request.ExamResponsesRequest;
import com.fonyou.studentexam.payload.request.ExamScheduleRequest;
import com.fonyou.studentexam.payload.request.QuestionRequest;
import com.fonyou.studentexam.payload.response.*;
import com.fonyou.studentexam.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final StudentResponseRepository studentResponseRepository;
    private final ExamGradeRepository examGradeRepository;

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

    public ExamScheduleResponse createExamSchedule(ExamScheduleRequest examScheduleRequest) {
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

    public ExamGradeResponse submitExamResponses(Long examScheduleId, List<ExamResponsesRequest> examResponsesRequestList) {
        ExamScheduleEntity examScheduleEntity = examScheduleRepository.findById(examScheduleId)
                .orElseThrow(() -> new BusinessException("Exam scheduled has not been found"));

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
        ExamGradeEntity examGradeEntitySaved = ExamGradeEntity.builder()
                .examSchedule(examScheduleEntity)
                .grade(totalScore)
                .build();

        studentResponseRepository.saveAll(studentResponseEntityList);
        examGradeRepository.save(examGradeEntitySaved);
        return examGradeEntityToExamGradeResponse(examGradeEntitySaved,studentResponseEntityList);
    }

    public ExamGradeEntity getExamGrade(Long examScheduleId) {
        return new ExamGradeEntity();
    }

    public ExamResponse examEntityToExamResponse(ExamEntity examEntity){
        return ExamResponse.builder()
                .id(examEntity.getId())
                .examName(examEntity.getExamName())
                .questions(examEntity.getQuestions().stream().map(this::questionEntityToQuestionResponse).collect(Collectors.toList()))
                .build();
    }
    public StudentResponseResponse studentResponseEntityToStudentResponseResponse(StudentResponseEntity studentResponseEntity){
        return StudentResponseResponse.builder()
                .question(studentResponseEntity.getQuestion().getQuestion())
                .studentResponse(studentResponseEntity.getStudentResponse())
                .build();
    }
    public QuestionResponse questionEntityToQuestionResponse(QuestionEntity questionEntity){
        return QuestionResponse.builder()
                .question(questionEntity.getQuestion())
                .choice1(questionEntity.getChoice1())
                .choice2(questionEntity.getChoice2())
                .choice3(questionEntity.getChoice3())
                .choice4(questionEntity.getChoice4())
                .correctChoice(questionEntity.getCorrectChoice())
                .score(questionEntity.getScore())
                .build();
    }
    public ExamGradeResponse examGradeEntityToExamGradeResponse(ExamGradeEntity examGradeEntity,List<StudentResponseEntity> studentResponseEntityList){
        return ExamGradeResponse.builder()
                .exam(examEntityToExamResponse(examGradeEntity.getExamSchedule().getExam()))
                .response(studentResponseEntityList.stream().map(this::studentResponseEntityToStudentResponseResponse).collect(Collectors.toList()))
                .grade(examGradeEntity.getGrade())
                .build();
    }


}
