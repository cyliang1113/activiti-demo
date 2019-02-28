package cn.leo.lab.activitidemo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;

@Async
public class TestTest {
    @RequestMapping
    public static void main(String[] args) {
        Class<? extends TestTest> aClass = TestTest.class;
        Annotation[] annotations = aClass.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation.annotationType().getName());
        }
    }
}
