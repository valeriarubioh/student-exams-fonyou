package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.StudentEntity;
import com.fonyou.studentexam.payload.request.StudentRequest;
import com.fonyou.studentexam.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    @Test
    @DisplayName("Should create a new student with valid input")
    void testCreateStudent_ValidInput() {
        StudentRequest studentRequest = StudentRequest.builder()
                .name("Pepe Perez")
                .age(18)
                .city("Tokio")
                .cityTimeZone("Asia/Tokyo")
                .build();
        StudentEntity expectedStudentEntity = StudentEntity.builder()
                .id(1L)
                .name("Pepe Perez")
                .age(18)
                .city("Tokio")
                .cityTimeZone("Asia/Tokyo")
                .build();

        when(studentRepository.save(any(StudentEntity.class))).thenReturn(expectedStudentEntity);
        StudentEntity result = studentService.createStudent(studentRequest);

        assertNotNull(result);
        assertEquals(expectedStudentEntity.getId(), result.getId());
        assertEquals(expectedStudentEntity.getName(), result.getName());
        assertEquals(expectedStudentEntity.getAge(), result.getAge());
        assertEquals(expectedStudentEntity.getCity(), result.getCity());
        assertEquals(expectedStudentEntity.getCityTimeZone(), result.getCityTimeZone());
    }
}
