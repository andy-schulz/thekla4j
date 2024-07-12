package com.teststeps.thekla4j.core.activityLog;

import com.teststeps.thekla4j.core.base.activities.Activity;
import io.vavr.Function1;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


@Log4j2(topic = "Annotation Operations")
public class AnnotationFunctions {

  private AnnotationFunctions() {
    // prevent instantiation of utility class
  }

  public static final Function1<Field, Field> makePrivateFieldAccessible = field -> {
    if (Modifier.isPrivate(field.getModifiers())) {
      field.setAccessible(true);
    }
    if(field.getModifiers() == 0) {
      field.setAccessible(true);
    }
    return field;
  };


  /*
get field value, if the field is a function execute it and take the result
 */
  public static <I, O> Function1<Field , Object> getFieldValueOfActivity(Activity<I, O> activity) {

   return field -> {
     try {
       return field.get(activity) == null ? "null" : field.get(activity);
     } catch (IllegalAccessException e) {

       log.error("Error: Cant access field {} declared in class {}. \nStacktrace: {}",
           field.getName(), field.getDeclaringClass().getSimpleName(), e.getStackTrace());

       return "Error: Cant access field " + field.getName();
     }
   };

  }
}
