package com.teststeps.thekla4j.core.activityLog;

import com.teststeps.thekla4j.activityLog.ActivityLogEntryType;
import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.CalledList;
import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.errors.DetectedNullObject;
import com.teststeps.thekla4j.core.base.errors.DoesNotHave;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@Log4j2(topic = "ProcessLogAnnotation")
public class ProcessLogAnnotation<P1, R1> {

  public static class ProcessAnnotationHelper<P2, R2> {
    private final Activity<P2, R2> activity;
    private Option<P2> parameter = Option.none();

    ProcessAnnotationHelper(Activity<P2, R2> activity) {
      this.activity = activity;
    }

    public Try<ActivityLogData> andActor(Actor actor) {
      return new ProcessLogAnnotation<P2, R2>(this.activity, parameter, actor).process();
    }

    public ProcessAnnotationHelper<P2, R2> withParameter(P2 parameter) {
      this.parameter = Option.of(parameter);
      return this;
    }
  }

  public static class ActivityLogData {
    public final String description;
    public final ActivityLogEntryType activityType;
    public final TASK_LOG logType;

    public ActivityLogData(String description, ActivityLogEntryType activityType, TASK_LOG logType) {
      this.description = description;
      this.activityType = activityType;
      this.logType = logType;
    }
  }

  private final Activity<P1, R1> activity;
  private final Actor actor;
  private final Option<P1> parameter;

  public static <P3, R3> ProcessAnnotationHelper<P3, R3> forActivity(Activity<P3, R3> activity) {
    return new ProcessAnnotationHelper<>(activity);
  }

  private Try<ActivityLogData> process() {
    if (this.activity == null)
      return Try.failure(DetectedNullObject.forProperty(this.getClass()
          .getSimpleName() + "::activity"));

    if (this.actor == null)
      return Try.failure(DetectedNullObject.forProperty(this.getClass()
          .getSimpleName() + "::actor"));

    Class<?> clazz = this.activity.getClass();

    final Workflow flowAnT = clazz.getAnnotation(Workflow.class);
    final Action interactionAnT = clazz.getAnnotation(Action.class);

    if (flowAnT == null && interactionAnT == null)
      return Try.failure(DoesNotHave
          .logAnnotation(Workflow.class.getSimpleName() + " or " + Action.class.getSimpleName())
          .in(this.activity));

    TASK_LOG logType = flowAnT == null ? interactionAnT.log() : flowAnT.log();

    return Try.of(() -> createLogDescription(
            flowAnT != null ?
                flowAnT.value() :
                interactionAnT.value(),
            clazz,
            (Option<Object>) this.parameter
                                            ))
        .map(desc -> new ActivityLogData(desc, flowAnT != null ?
            ActivityLogEntryType.Task :
            ActivityLogEntryType.Interaction, logType));
  }


  /**
   * filter annotations to Called and Called List
   */
  private final Function1<List<Annotation>, List<Annotation>> filterAnnotations =
      list -> list.filter(annotation ->
          annotation instanceof CalledList ||
              annotation instanceof Called);

  private final Function1<Field, Field> makePrivateFieldAccessible = field -> {
    if (Modifier.isPrivate(field.getModifiers())) {
      field.setAccessible(true);
    }
    return field;
  };

  /**
   * construct a tuple with field value and field annotations
   */
  /*accessibility update is needed to create the log */
  @SuppressWarnings("java:S3011")
  private final Function2<Activity<P1, R1>, Class<?>, List<Tuple2<Option<Object>, List<Annotation>>>> getFieldValueAndAnnotations =
      (actvty, clazz) -> {

//        Function1<Field, Field> makePrivateFieldAccessible = field -> {
//          if (Modifier.isPrivate(field.getModifiers())) {
//            field.setAccessible(true);
//          }
//          return field;
//        };

    /*
    get field value, if the field is a function execute it and take the result
     */
        Function1<Field, Object> getFieldValue = field -> {
          try {
            return (field.get(actvty) instanceof Function0) ?
                ((Function0<Object>) field.get(actvty)).apply() :
                field.get(actvty) == null ? "null" : field.get(actvty);

          } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "Error: Cant access field " + field.getName();
          }
        };

        return List.of(clazz.getFields())
            .appendAll(Arrays.asList(clazz.getDeclaredFields()))
            .map(field -> Tuple.of(field, field.getAnnotations()))
            .map(t -> t.map2(List::of))
            .map(t -> t.map2(filterAnnotations))
            .filter(t -> !t._2.isEmpty())
            .map(t -> t.map1(makePrivateFieldAccessible))
            .map(t -> t.map1(getFieldValue))
            .map(t -> t.map1(Option::of));
      };


  /**
   * construct a tuple with parameter value and parameter annotations
   */
  private final Function2<Option<Object>, Class<?>, List<Tuple2<Option<Object>, List<Annotation>>>> getAnnotationsOfParameters1 =
      (object, clazz) -> List.of(clazz.getDeclaredMethods())
          // the annotation is only valid for the performAs method
          // other methods cant be supported
          .filter(method -> method.getName()
              .equals("performAs"))
          .map(method -> Tuple.of(object, method.getParameterAnnotations()))
          // select only annotations of performAs second parameter
          .map(t -> t.map2(annos -> annos[1]))
          .map(t -> t.map2(List::of))
          // only Called and CalledList annotations are of interest
          .map(t -> t.map2(filterAnnotations));


  private final Function2<Class<? extends Object>, String, Try<Field>> getFieldByName =
      (clazz, fieldName) -> Try.of(() -> clazz.getDeclaredField(fieldName))
          .map(makePrivateFieldAccessible);


  /*
   * get the value of fieldName from object (by reflection)
   */
  private final Function2<Option<Object>, String, Option<Object>> getAttribute =
      (obj, fieldName) ->
          obj.map(Object::getClass)
              .flatMap(clz -> Try
                  // don't try to find a field for fieldName "" in the object, just return the object
                  .of(() -> fieldName.equals("") ?
                      obj.get() :
                      getFieldByName.apply(clz, fieldName)
                          .mapTry(f -> f.get(obj.get()))
                          .get())
                  .onFailure(ex -> log.error(ex + " in object of type " + clz.getSimpleName()))
                  // toOption is used so that I can use flatMap
                  .toOption());


  private final Function1<CalledList, List<Called>> processAnnotationList =
      annotation -> List.of((annotation).value());


  /**
   * extract the value of attributeString from parameter
   * <p>
   * e.g. let attributeString = "referencedObject.name"
   * <p>
   * and parameter is an object of:
   * <p>
   * MyObject1( MyObject2 referencedObject = obj; )
   * <p>
   * with:
   * <p>
   * MyObject2( String name = "MyObjectName" )
   * <p>
   * then "MyObjectName" will be returned
   */
  private final Function2<Option<Object>, String, String> extractMyAttribute =
      (param, attributeString) -> List.of(attributeString.split("\\."))
          .foldLeft(param, getAttribute)
          .map(Object::toString)
          .getOrElse("not found");

  private final Function2<String, Tuple2<String, String>, String> replaceSingleAttributeInString =
      (theDesc, tu) -> theDesc.replace(tu._2, tu._1);

  /**
   * process the annotation list and replace the annotation value in the description string
   */
  private final Function2<List<Tuple2<Option<Object>, List<Annotation>>>, String, String> processAnnotationsAndReplace =
      (list, descr) -> list
          // flatten the Annotation list
          // List<Tuple2<Option<Object>, List<Annotation>>> -> List<Tuple2<Option<Object>, Annotation>>
          .flatMap(t -> t.apply((t1, t2) -> t2.map(a -> Tuple.of(t1, a))))
          // check if multiple Called Annotations are applied
          .map(tuple -> tuple
              .map2(annotation -> Match(annotation).of(
                  Case($(a -> a instanceof CalledList), a -> processAnnotationList.apply((CalledList) a)),
                  Case($(a -> a instanceof Called), a -> List.of((Called) a))
                                                      )))
          // flatten the annotation list again
          .flatMap(t -> t.apply((t1, t2) -> t2.map(a -> Tuple.of(t1, a))))
          // get the replacements from parameter
          // if it's a nested object traverse it e.g.(Called(name = "myName", value = "nested1.nested2"))
          // if it's just a plain object just return it e.g.(Called(name = "myName"))
          .map(t -> t.map(p -> extractMyAttribute.apply(p, t._2.value()), Called::name))
          // create the replacement string to search for in the description
          .map(t -> t.map2(s -> "@{" + s + "}"))
          // replace everything the description
          .foldLeft(descr, replaceSingleAttributeInString)
          .trim();

  private String createLogDescription(String description, Class<?> clazz, Option<Object> param) {


    return Option.of(description)
        // process and replace the fields
        .map(processAnnotationsAndReplace.apply(getFieldValueAndAnnotations.apply(this.activity, clazz)))
        // process and replace the parameter in the performAs method
        .map(processAnnotationsAndReplace.apply(getAnnotationsOfParameters1.apply(param, clazz)))
        .get();
  }

  ProcessLogAnnotation(Activity<P1, R1> activity, Option<P1> param, Actor actor) {
    this.activity = activity;
    this.actor = actor;
    this.parameter = param;
  }
}