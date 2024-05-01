package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.ExamGradeEntity;
import com.fonyou.studentexam.entities.ExamScheduleEntity;
import com.fonyou.studentexam.entities.QuestionEntity;
import com.fonyou.studentexam.payload.request.ExamQuestionsRequest;
import com.fonyou.studentexam.payload.request.ExamResponsesRequest;
import com.fonyou.studentexam.payload.request.ExamScheduleRequest;
import com.fonyou.studentexam.repositories.ExamRepository;
import com.fonyou.studentexam.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;


    public void createExamQuestions(ExamQuestionsRequest examQuestionsRequestList) {
        QuestionEntity questionEntity = QuestionEntity.builder().build();
    }

    public ExamScheduleEntity createExamSchedule(ExamScheduleRequest examScheduleRequest) {
        return new ExamScheduleEntity();
    }

    public void submitExamResponses(List<ExamResponsesRequest> examResponsesRequestList) {
    }

    public ExamGradeEntity getExamGrade(Long examScheduleId) {
        return new ExamGradeEntity();
    }
}