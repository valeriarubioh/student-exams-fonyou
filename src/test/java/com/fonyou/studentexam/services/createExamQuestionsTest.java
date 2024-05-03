package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.ExamEntity;
import com.fonyou.studentexam.entities.QuestionEntity;
import com.fonyou.studentexam.exceptions.BusinessException;
import com.fonyou.studentexam.payload.request.ExamQuestionsRequest;
import com.fonyou.studentexam.payload.request.QuestionRequest;
import com.fonyou.studentexam.repositories.ExamRepository;
import com.fonyou.studentexam.repositories.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class createExamQuestionsTest {
    @Mock
    private ExamRepository examRepository;
    @Mock
    private QuestionRepository questionRepository;
    @InjectMocks
    private ExamService examService;
    private List<QuestionRequest> getValidQuestionRequests() {
        return Arrays.asList(
                QuestionRequest.builder()
                        .question("Question 1")
                        .choice1("Choice 1")
                        .choice2("Choice 2")
                        .choice3("Choice 3")
                        .choice4("Choice 4")
                        .correctChoice(1)
                        .score(50)
                        .build(),
                QuestionRequest.builder()
                        .question("Question 2")
                        .choice1("Choice 1")
                        .choice2("Choice 2")
                        .choice3("Choice 3")
                        .choice4("Choice 4")
                        .correctChoice(2)
                        .score(50)
                        .build()
        );
    }

    private List<QuestionRequest> getInvalidQuestionRequests() {
        return Arrays.asList(
                QuestionRequest.builder()
                        .question("Question 1")
                        .choice1("Choice 1")
                        .choice2("Choice 2")
                        .choice3("Choice 3")
                        .choice4("Choice 4")
                        .correctChoice(1)
                        .score(40)
                        .build(),
                QuestionRequest.builder()
                        .question("Question 2")
                        .choice1("Choice 1")
                        .choice2("Choice 2")
                        .choice3("Choice 3")
                        .choice4("Choice 4")
                        .correctChoice(2)
                        .score(50)
                        .build()
        );
    }
    @Test
    @DisplayName("Should create exam questions with valid input")
    void testCreateExamQuestions_ValidInput() {
        List<QuestionRequest> questions = getValidQuestionRequests();
        ExamQuestionsRequest request = createExamQuestionsRequest(questions);

        ExamEntity examEntity = new ExamEntity();
        examEntity.setId(1L);
        examEntity.setExamName("Test Exam");

        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (QuestionRequest questionRequest : questions) {
            QuestionEntity questionEntity = QuestionEntity.builder()
                    .exam(examEntity)
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
        examEntity.setQuestions(questionEntities);

        when(examRepository.save(any(ExamEntity.class))).thenReturn(examEntity);
        when(questionRepository.saveAll(anyList())).thenReturn(questionEntities);

        ExamEntity result = examService.createExamQuestions(request);

        assertThat(result)
                .isEqualTo(examEntity)
                .extracting(ExamEntity::getId, ExamEntity::getExamName, ExamEntity::getQuestions)
                .containsExactly(examEntity.getId(), examEntity.getExamName(), examEntity.getQuestions());
    }



    @Test
    @DisplayName("Should throw BusinessException when score sum is invalid")
    void testCreateExamQuestions_InvalidScoreSum() {
        List<QuestionRequest> questions = getInvalidQuestionRequests();
        ExamQuestionsRequest request = createExamQuestionsRequest(questions);
        BusinessException exception = assertThrows(BusinessException.class, () -> examService.createExamQuestions(request));
        assertEquals("The sum of scores for all questions should be 100", exception.getMessage());
    }

    private ExamQuestionsRequest createExamQuestionsRequest(List<QuestionRequest> questions) {
        return ExamQuestionsRequest.builder()
                .examName("Test Exam")
                .questions(questions)
                .build();
    }
}
