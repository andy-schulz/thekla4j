package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Sleep;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.*;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.time.Duration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestActorActivityLog {
  private final Actor John = Actor.named("John");

  @BeforeAll
  public static void init() {
  }

  @Test
  public void generateActorActivityLog() {
    John.attemptsTo(
      Sleep.forA(Duration.ofMillis(1))
                   );

    assertThat(
      John.activityLog.getStructuredLog("\t"),
      equalTo("[✓ START] - John attempts to\n" +
        "\t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)")
              );
  }

  @Test
  public void generateFailedActivityLog() {
    John.attemptsTo(
      Sleep.forA(Duration.ofMillis(1)),
      T_V2V_Failing.start()
                   );

    assertThat(
      John.activityLog.getStructuredLog("\t"),
      equalTo("""
        [✗ START] - John attempts to
        \t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)
        \t[✗ T_V2V_Failing] - start a failing Task""")
              );

//    assertThat(John.activityLog.getEncodedStructuredHtmlLog(), equalTo("PHN0eWxlPgp1bCwgI0FjdGl2aXR5TG9nIHsNCiAgICBsaXN0LXN0eWxlLXR5cGU6IG5vbmU7DQogICAgZm9udC1mYW1pbHk6IG1vbm9zcGFjZTsNCn0NCg0KI0FjdGl2aXR5TG9nIHsNCiAgICBtYXJnaW46IDA7DQogICAgcGFkZGluZzogMDsNCn0NCg0KLnRhc2sgew0KICAgIGN1cnNvcjogcG9pbnRlcjsNCiAgICAtd2Via2l0LXVzZXItc2VsZWN0OiBub25lOyAvKiBTYWZhcmkgMy4xKyAqLw0KICAgIC1tb3otdXNlci1zZWxlY3Q6IG5vbmU7IC8qIEZpcmVmb3ggMisgKi8NCiAgICAtbXMtdXNlci1zZWxlY3Q6IG5vbmU7IC8qIElFIDEwKyAqLw0KICAgIHVzZXItc2VsZWN0OiBub25lOw0KfQ0KDQoudGFzazo6YmVmb3JlIHsNCiAgICAvKmNvbnRlbnQ6ICJcMjVCNiI7ICovDQogICAgY29udGVudDogIlwyNUI2IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgZGlzcGxheTogaW5saW5lLWJsb2NrOw0KICAgIG1hcmdpbi1yaWdodDogNnB4Ow0KfQ0KDQoudGFzay5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLnRhc2sucGFzc2VkOjpiZWZvcmUgew0KICAgIGNvbG9yOiBncmVlbjsNCn0NCg0KLnRhc2sucnVubmluZzo6YmVmb3JlIHsNCiAgICBjb2xvcjogYmxhY2s7DQp9DQoNCi50YXNrLW9wZW46OmJlZm9yZSB7DQogICAgLW1zLXRyYW5zZm9ybTogcm90YXRlKDkwZGVnKTsgLyogSUUgOSAqLw0KICAgIC13ZWJraXQtdHJhbnNmb3JtOiByb3RhdGUoOTBkZWcpOyAvKiBTYWZhcmkgKi8nDQp0cmFuc2Zvcm06IHJvdGF0ZSg5MGRlZyk7DQp9DQoNCi5uZXN0ZWQgew0KICAgIGRpc3BsYXk6IG5vbmU7DQp9DQoNCi5hY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQouaW50ZXJhY3Rpb246OmJlZm9yZSB7DQogICAgY29udGVudDogIlwyNUI3IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgbWFyZ2luLXJpZ2h0OiA2cHg7DQp9DQoNCi5pbnRlcmFjdGlvbi5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmludGVyYWN0aW9uLnBhc3NlZDo6YmVmb3JlIHsNCiAgICBjb2xvcjogZ3JlZW47DQp9DQoNCi5pbnRlcmFjdGlvbi5ydW5uaW5nOjpiZWZvcmUgew0KICAgIGNvbG9yOiBibGFjazsNCn0NCg0KLmxvZ01lc3NhZ2UuZmFpbCB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmFjdGl2aXR5TmFtZSB7DQogICAgY29sb3I6ICNmZjBiYjE7DQp9Cjwvc3R5bGU+IAoKPHVsIGlkID0gIkFjdGl2aXR5TG9nIj48bGk+PHNwYW4gY2xhc3M9InRhc2sgZmFpbGVkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3M9ImFjdGl2aXR5TmFtZSI+W1NUQVJUXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IEpvaG4gYXR0ZW1wdHMgdG88L3NwYW4+PC9zcGFuPjwvc3Bhbj48dWwgY2xhc3M9Im5lc3RlZCI+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMSBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PGxpPjxzcGFuIGNsYXNzPSJ0YXNrIGZhaWxlZCI+PHNwYW4gY2xhc3M9ImxvZ01lc3NhZ2UiPjxzcGFuIGNsYXNzPSJhY3Rpdml0eU5hbWUiPltUX1YyVl9GYWlsaW5nXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IHN0YXJ0IGEgZmFpbGluZyBUYXNrPC9zcGFuPjwvc3Bhbj48L3NwYW4+PHVsIGNsYXNzPSJuZXN0ZWQiPjwvdWw+PC9saT48L3VsPjwvbGk+PC91bD4gCgo8c2NyaXB0Pgp2YXIgdG9nZ2xlciA9IGRvY3VtZW50LmdldEVsZW1lbnRzQnlDbGFzc05hbWUoInRhc2siKTsKdmFyIGk7Cgpmb3IgKGkgPSAwOyBpIDwgdG9nZ2xlci5sZW5ndGg7IGkrKykgewogIHRvZ2dsZXJbaV0uYWRkRXZlbnRMaXN0ZW5lcigiY2xpY2siLCBmdW5jdGlvbigpIHsKICAgIHRoaXMucGFyZW50RWxlbWVudC5xdWVyeVNlbGVjdG9yKCIubmVzdGVkIikuY2xhc3NMaXN0LnRvZ2dsZSgiYWN0aXZlIik7CiAgICB0aGlzLmNsYXNzTGlzdC50b2dnbGUoInRhc2stb3BlbiIpOwogIH0pOwp9Cjwvc2NyaXB0Pg=="));
  }

  @Test
  public void generateTreeActivityLogWithoutAnyAnnotation() {
    // activities of a task should be listed at the tasks level
    // in case the task does not contain an annotation
    John.attemptsTo(
      Sleep.forA(Duration.ofMillis(1)),
      T_V2S.start()
                   );

    assertThat(
      John.activityLog.getStructuredLog("\t"),
      equalTo("""
        [✓ START] - John attempts to
        \t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)
        \t[✓ Sleep] - pause all activities for 0 Seconds (3 ms)""")
              );

//    assertThat(
//        John.activityLog.getEncodedStructuredHtmlLog(),
//        equalTo(
//            "PHN0eWxlPgp1bCwgI0FjdGl2aXR5TG9nIHsNCiAgICBsaXN0LXN0eWxlLXR5cGU6IG5vbmU7DQogICAgZm9udC1mYW1pbHk6IG1vbm9zcGFjZTsNCn0NCg0KI0FjdGl2aXR5TG9nIHsNCiAgICBtYXJnaW46IDA7DQogICAgcGFkZGluZzogMDsNCn0NCg0KLnRhc2sgew0KICAgIGN1cnNvcjogcG9pbnRlcjsNCiAgICAtd2Via2l0LXVzZXItc2VsZWN0OiBub25lOyAvKiBTYWZhcmkgMy4xKyAqLw0KICAgIC1tb3otdXNlci1zZWxlY3Q6IG5vbmU7IC8qIEZpcmVmb3ggMisgKi8NCiAgICAtbXMtdXNlci1zZWxlY3Q6IG5vbmU7IC8qIElFIDEwKyAqLw0KICAgIHVzZXItc2VsZWN0OiBub25lOw0KfQ0KDQoudGFzazo6YmVmb3JlIHsNCiAgICAvKmNvbnRlbnQ6ICJcMjVCNiI7ICovDQogICAgY29udGVudDogIlwyNUI2IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgZGlzcGxheTogaW5saW5lLWJsb2NrOw0KICAgIG1hcmdpbi1yaWdodDogNnB4Ow0KfQ0KDQoudGFzay5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLnRhc2sucGFzc2VkOjpiZWZvcmUgew0KICAgIGNvbG9yOiBncmVlbjsNCn0NCg0KLnRhc2sucnVubmluZzo6YmVmb3JlIHsNCiAgICBjb2xvcjogYmxhY2s7DQp9DQoNCi50YXNrLW9wZW46OmJlZm9yZSB7DQogICAgLW1zLXRyYW5zZm9ybTogcm90YXRlKDkwZGVnKTsgLyogSUUgOSAqLw0KICAgIC13ZWJraXQtdHJhbnNmb3JtOiByb3RhdGUoOTBkZWcpOyAvKiBTYWZhcmkgKi8nDQp0cmFuc2Zvcm06IHJvdGF0ZSg5MGRlZyk7DQp9DQoNCi5uZXN0ZWQgew0KICAgIGRpc3BsYXk6IG5vbmU7DQp9DQoNCi5hY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQouaW50ZXJhY3Rpb246OmJlZm9yZSB7DQogICAgY29udGVudDogIlwyNUI3IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgbWFyZ2luLXJpZ2h0OiA2cHg7DQp9DQoNCi5pbnRlcmFjdGlvbi5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmludGVyYWN0aW9uLnBhc3NlZDo6YmVmb3JlIHsNCiAgICBjb2xvcjogZ3JlZW47DQp9DQoNCi5pbnRlcmFjdGlvbi5ydW5uaW5nOjpiZWZvcmUgew0KICAgIGNvbG9yOiBibGFjazsNCn0NCg0KLmxvZ01lc3NhZ2UuZmFpbCB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmFjdGl2aXR5TmFtZSB7DQogICAgY29sb3I6ICNmZjBiYjE7DQp9Cjwvc3R5bGU+IAoKPHVsIGlkID0gIkFjdGl2aXR5TG9nIj48bGk+PHNwYW4gY2xhc3M9InRhc2sgcGFzc2VkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3M9ImFjdGl2aXR5TmFtZSI+W1NUQVJUXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IEpvaG4gYXR0ZW1wdHMgdG88L3NwYW4+PC9zcGFuPjwvc3Bhbj48dWwgY2xhc3M9Im5lc3RlZCI+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMSBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMyBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PC91bD48L2xpPjwvdWw+IAoKPHNjcmlwdD4KdmFyIHRvZ2dsZXIgPSBkb2N1bWVudC5nZXRFbGVtZW50c0J5Q2xhc3NOYW1lKCJ0YXNrIik7CnZhciBpOwoKZm9yIChpID0gMDsgaSA8IHRvZ2dsZXIubGVuZ3RoOyBpKyspIHsKICB0b2dnbGVyW2ldLmFkZEV2ZW50TGlzdGVuZXIoImNsaWNrIiwgZnVuY3Rpb24oKSB7CiAgICB0aGlzLnBhcmVudEVsZW1lbnQucXVlcnlTZWxlY3RvcigiLm5lc3RlZCIpLmNsYXNzTGlzdC50b2dnbGUoImFjdGl2ZSIpOwogICAgdGhpcy5jbGFzc0xpc3QudG9nZ2xlKCJ0YXNrLW9wZW4iKTsKICB9KTsKfQo8L3NjcmlwdD4=")
//              );
  }


  @Test
  public void generateLogUsingRecord() {
    John.attemptsTo(
      TaskWithRecordData.start());

    assertThat(
      John.activityLog.getStructuredLog("\t"),
      equalTo("[✓ START] - John attempts to\n" +
        "\t[✓ TaskWithRecordData] - task with user data name: 'TesterRecord'")
              );
  }

  @Test
  public void generateTreeActivityLog() {
    // activities of a task should be listed at the tasks level
    // in case the task does not contain an annotation
    John.attemptsTo(
      Sleep.forA(Duration.ofMillis(1)),
      T_V2S.start(),
      T_S2S.start()
                   );

    assertThat(
      John.activityLog.getStructuredLog("\t"),
      equalTo("""
        [✓ START] - John attempts to
        \t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)
        \t[✓ Sleep] - pause all activities for 0 Seconds (3 ms)
        \t[✓ T_S2S] - start a String to String Task
        \t\t[✓ Sleep] - pause all activities for 0 Seconds (3 ms)""")
              );

    System.out.println(John.activityLog.getEncodedStructuredHtmlLog());

//    assertThat(
//        John.activityLog.getEncodedStructuredHtmlLog(),
//        equalTo(
//            "PHN0eWxlPgp1bCwgI0FjdGl2aXR5TG9nIHsNCiAgICBsaXN0LXN0eWxlLXR5cGU6IG5vbmU7DQogICAgZm9udC1mYW1pbHk6IG1vbm9zcGFjZTsNCn0NCg0KI0FjdGl2aXR5TG9nIHsNCiAgICBtYXJnaW46IDA7DQogICAgcGFkZGluZzogMDsNCn0NCg0KLnRhc2sgew0KICAgIGN1cnNvcjogcG9pbnRlcjsNCiAgICAtd2Via2l0LXVzZXItc2VsZWN0OiBub25lOyAvKiBTYWZhcmkgMy4xKyAqLw0KICAgIC1tb3otdXNlci1zZWxlY3Q6IG5vbmU7IC8qIEZpcmVmb3ggMisgKi8NCiAgICAtbXMtdXNlci1zZWxlY3Q6IG5vbmU7IC8qIElFIDEwKyAqLw0KICAgIHVzZXItc2VsZWN0OiBub25lOw0KfQ0KDQoudGFzazo6YmVmb3JlIHsNCiAgICAvKmNvbnRlbnQ6ICJcMjVCNiI7ICovDQogICAgY29udGVudDogIlwyNUI2IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgZGlzcGxheTogaW5saW5lLWJsb2NrOw0KICAgIG1hcmdpbi1yaWdodDogNnB4Ow0KfQ0KDQoudGFzay5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLnRhc2sucGFzc2VkOjpiZWZvcmUgew0KICAgIGNvbG9yOiBncmVlbjsNCn0NCg0KLnRhc2sucnVubmluZzo6YmVmb3JlIHsNCiAgICBjb2xvcjogYmxhY2s7DQp9DQoNCi50YXNrLW9wZW46OmJlZm9yZSB7DQogICAgLW1zLXRyYW5zZm9ybTogcm90YXRlKDkwZGVnKTsgLyogSUUgOSAqLw0KICAgIC13ZWJraXQtdHJhbnNmb3JtOiByb3RhdGUoOTBkZWcpOyAvKiBTYWZhcmkgKi8nDQp0cmFuc2Zvcm06IHJvdGF0ZSg5MGRlZyk7DQp9DQoNCi5uZXN0ZWQgew0KICAgIGRpc3BsYXk6IG5vbmU7DQp9DQoNCi5hY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQoubG9uZ0Rlc2NyaXB0aW9uIHsNCiAgICBkaXNwbGF5OiBub25lOw0KfQ0KDQouZGVzY3JpcHRpb25BY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQoubG9uZ0Rlc2NyaXB0aW9uIC5kZXNjSGVhZGVyIHsNCiAgICBiYWNrZ3JvdW5kOiBsaWdodGdyYXk7DQogICAgYm9yZGVyLXJhZGl1czogMWVtOw0KICAgIGJvcmRlcjogMXB4IHNvbGlkICM4OTg5ODk7DQogICAgcG9zaXRpb246IHJlbGF0aXZlOw0KICAgIHRvcDogMC43ZW07DQogICAgd2lkdGg6IG1heC1jb250ZW50Ow0KICAgIHJpZ2h0OiAtMWVtOw0KICAgIHBhZGRpbmctbGVmdDogMWVtOw0KICAgIHBhZGRpbmctcmlnaHQ6IDFlbTsNCn0NCg0KLmxvbmdEZXNjcmlwdGlvbiAuZGVzY01lc3NhZ2Ugew0KICAgIGJhY2tncm91bmQ6IGxpZ2h0Z3JheTsNCiAgICBib3JkZXItcmFkaXVzOiAwLjJlbTsNCiAgICBib3JkZXI6IDFweCBzb2xpZCAjODk4OTg5Ow0KICAgIHBhZGRpbmctbGVmdDogMC41ZW07DQp9DQoNCi5pbkluZm8gew0KICAgIGRpc3BsYXk6IG5vbmU7DQogICAgcGFkZGluZy1sZWZ0OiAyZW07DQp9DQoub3V0SW5mbyB7DQogICAgZGlzcGxheTogbm9uZTsNCiAgICBwYWRkaW5nLWxlZnQ6IDJlbTsNCn0NCg0KLmluZm9IZWFkZXIgew0KICAgIGJhY2tncm91bmQ6IGxpZ2h0Z3JheTsNCiAgICBib3JkZXItcmFkaXVzOiAxZW07DQogICAgYm9yZGVyOiAxcHggc29saWQgIzg5ODk4OTsNCiAgICBwb3NpdGlvbjogcmVsYXRpdmU7DQogICAgdG9wOiAwLjdlbTsNCiAgICB3aWR0aDogbWF4LWNvbnRlbnQ7DQogICAgcmlnaHQ6IC0xZW07DQogICAgcGFkZGluZy1sZWZ0OiAxZW07DQogICAgcGFkZGluZy1yaWdodDogMWVtOw0KfQ0KDQouaW5mb01lc3NhZ2Ugew0KICAgIGJhY2tncm91bmQ6IGxpZ2h0Z3JheTsNCiAgICBib3JkZXItcmFkaXVzOiAwLjJlbTsNCiAgICBib3JkZXI6IDFweCBzb2xpZCAjODk4OTg5Ow0KICAgIHBhZGRpbmctbGVmdDogMC41ZW07DQp9DQoNCi5pb0NvbnRlbnQgew0KICAgIG1hcmdpbi1ib3R0b206IDEwcHg7DQp9DQoub3V0QWN0aXZlIHsNCiAgICBkaXNwbGF5OiBibG9jazsNCn0NCg0KLmluQWN0aXZlIHsNCiAgICBkaXNwbGF5OiBibG9jazsNCn0NCg0KLmlvSW5mbyB7DQogICAgZGlzcGxheTogbm9uZTsNCn0NCg0KLmlvQWN0aXZlIHsNCiAgICBkaXNwbGF5OiBibG9jazsNCn0NCg0KLmNvbnRlbnRCdXR0b24gew0KICAgIC8qcG9zaXRpb246IGFic29sdXRlOyovDQogICAgY3Vyc29yOiBoZWxwOw0KICAgIC8qYm9yZGVyOiAxcHggc29saWQgd2hpdGU7Ki8NCiAgICBib3JkZXItcmFkaXVzOiAycHg7DQogICAgYm94LXNoYWRvdzogMXB4IDFweCAxcHggIzgwODA4MDsNCiAgICBjb2xvcjogd2hpdGU7DQogICAgZm9udC13ZWlnaHQ6IGJvbGQ7DQogICAgaGVpZ2h0OiAxMXB4Ow0KICAgIHBhZGRpbmc6IDJweCA0cHggMnB4IDRweDsNCiAgICB0ZXh0LWFsaWduOiBjZW50ZXI7DQogICAgbWluLXdpZHRoOiAxMnB4Ow0KICAgIGZvbnQtc2l6ZTogeC1zbWFsbDsNCiAgICBkaXNwbGF5OiBpbmxpbmUtYmxvY2s7DQogICAgbWFyZ2luLWxlZnQ6IDFlbTsNCiAgICBiYWNrZ3JvdW5kOiAjMzE3MDhmOw0KICAgIC8qYmFja2dyb3VuZDogcmFkaWFsLWdyYWRpZW50KCA1cHggLTlweCwgY2lyY2xlLCB3aGl0ZSA4JSwgIzE0YTBmZiAyNnB4ICk7Ki8NCiAgICAvKmJhY2tncm91bmQ6IC1tb3otcmFkaWFsLWdyYWRpZW50KCA1cHggLTlweCwgY2lyY2xlLCB3aGl0ZSA4JSwgIzE0YTBmZiAyNnB4ICk7Ki8NCiAgICAvKmJhY2tncm91bmQ6IC1tcy1yYWRpYWwtZ3JhZGllbnQoIDVweCAtOXB4LCBjaXJjbGUsIHdoaXRlIDglLCAjMTRhMGZmIDI2cHggKTsqLw0KICAgIC8qYmFja2dyb3VuZDogLW8tcmFkaWFsLWdyYWRpZW50KCA1cHggLTlweCwgY2lyY2xlLCB3aGl0ZSA4JSwgIzE0YTBmZiAyNnB4ICk7Ki8NCiAgICAvKmJhY2tncm91bmQ6IC13ZWJraXQtcmFkaWFsLWdyYWRpZW50KCA1cHggLTlweCwgY2lyY2xlLCB3aGl0ZSA4JSwgIzE0YTBmZiAyNnB4ICk7Ki8NCn0NCg0KLmNvbnRlbnRCdXR0b24uYWN0aXZlIHsNCiAgICBiYWNrZ3JvdW5kOiAjNzMzMThmOw0KfQ0KDQouaW50ZXJhY3Rpb246OmJlZm9yZSB7DQogICAgY29udGVudDogIlwyNUI3IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgbWFyZ2luLXJpZ2h0OiA2cHg7DQp9DQoNCi5pbnRlcmFjdGlvbi5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmludGVyYWN0aW9uLnBhc3NlZDo6YmVmb3JlIHsNCiAgICBjb2xvcjogZ3JlZW47DQp9DQoNCi5pbnRlcmFjdGlvbi5ydW5uaW5nOjpiZWZvcmUgew0KICAgIGNvbG9yOiBibGFjazsNCn0NCg0KLmxvZ01lc3NhZ2UuZmFpbCB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmxvZ01lc3NhZ2UgLnRpbWVzdGFtcCB7DQogICAgY29sb3I6IGxpZ2h0c2t5Ymx1ZTsNCn0NCg0KLmFjdGl2aXR5TmFtZSB7DQogICAgY29sb3I6ICNmZjBiYjE7DQp9DQoNCnByZSB7DQogICAgd2hpdGUtc3BhY2U6cHJlLXdyYXA7DQogICAgb3ZlcmZsb3ctd3JhcDogYnJlYWstd29yZDsNCiAgICBtYXJnaW4tcmlnaHQ6IDMwcHg7DQp9Cjwvc3R5bGU+IAoKPHVsIGlkID0gIkFjdGl2aXR5TG9nIj48bGk+PHNwYW4gY2xhc3M9InRhc2sgcGFzc2VkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3MgPSAidGltZXN0YW1wIj4yMDIzLTEwLTE4IDA5OjAyOjMxIC0gPC9zcGFuPjxzcGFuIGNsYXNzPSJhY3Rpdml0eU5hbWUiPltTVEFSVF08L3NwYW4+IC0gPHNwYW4gY2xhc3M9ImFjdGl2aXR5RGVzY3JpcHRpb24iPiBKb2huIGF0dGVtcHRzIHRvPC9zcGFuPjwvc3Bhbj48L3NwYW4+PGRpdiBjbGFzcz0ibG9uZ0Rlc2NyaXB0aW9uIj48ZGl2IGNsYXNzPSJpbmZvSGVhZGVyIj5GdWxsIERlc2NyaXB0aW9uPC9kaXY+PGRpdiBjbGFzcz0iaW5mb01lc3NhZ2UiPjxwcmU+PC9wcmU+PC9kaXY+PC9kaXY+PHVsIGNsYXNzPSJuZXN0ZWQiPjxsaSBjbGFzcz0iaW50ZXJhY3Rpb24gcGFzc2VkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3MgPSAidGltZXN0YW1wIj4yMDIzLTEwLTE4IDA5OjAyOjMyIC0gPC9zcGFuPjxzcGFuIGNsYXNzPSJhY3Rpdml0eU5hbWUiPltTbGVlcF08L3NwYW4+IC0gPHNwYW4gY2xhc3M9ImFjdGl2aXR5RGVzY3JpcHRpb24iPiBwYXVzZSBhbGwgYWN0aXZpdGllcyBmb3IgMCBTZWNvbmRzICgxIG1zKTwvc3Bhbj48L3NwYW4+PGRpdiBjbGFzcz0ibG9uZ0Rlc2NyaXB0aW9uIj48ZGl2IGNsYXNzPSJpbmZvSGVhZGVyIj5GdWxsIERlc2NyaXB0aW9uPC9kaXY+PGRpdiBjbGFzcz0iaW5mb01lc3NhZ2UiPjxwcmU+PC9wcmU+PC9kaXY+PC9kaXY+PC9saT48bGkgY2xhc3M9ImludGVyYWN0aW9uIHBhc3NlZCI+PHNwYW4gY2xhc3M9ImxvZ01lc3NhZ2UiPjxzcGFuIGNsYXNzID0gInRpbWVzdGFtcCI+MjAyMy0xMC0xOCAwOTowMjozMiAtIDwvc3Bhbj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMyBtcyk8L3NwYW4+PC9zcGFuPjxkaXYgY2xhc3M9ImxvbmdEZXNjcmlwdGlvbiI+PGRpdiBjbGFzcz0iaW5mb0hlYWRlciI+RnVsbCBEZXNjcmlwdGlvbjwvZGl2PjxkaXYgY2xhc3M9ImluZm9NZXNzYWdlIj48cHJlPjwvcHJlPjwvZGl2PjwvZGl2PjwvbGk+PGxpPjxzcGFuIGNsYXNzPSJ0YXNrIHBhc3NlZCI+PHNwYW4gY2xhc3M9ImxvZ01lc3NhZ2UiPjxzcGFuIGNsYXNzID0gInRpbWVzdGFtcCI+MjAyMy0xMC0xOCAwOTowMjozMiAtIDwvc3Bhbj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bVF9TMlNdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gc3RhcnQgYSBTdHJpbmcgdG8gU3RyaW5nIFRhc2s8L3NwYW4+PC9zcGFuPjwvc3Bhbj48c3BhbiBjbGFzcz0ibGFiZWwgY29udGVudEJ1dHRvbiBpbkNvbnRlbnRCdXR0b24iPkluPC9zcGFuPjxzcGFuIGNsYXNzPSJsYWJlbCBjb250ZW50QnV0dG9uIG91dENvbnRlbnRCdXR0b24iPk91dDwvc3Bhbj48ZGl2IGNsYXNzPSJsb25nRGVzY3JpcHRpb24iPjxkaXYgY2xhc3M9ImluZm9IZWFkZXIiPkZ1bGwgRGVzY3JpcHRpb248L2Rpdj48ZGl2IGNsYXNzPSJpbmZvTWVzc2FnZSI+PHByZT48L3ByZT48L2Rpdj48L2Rpdj48c3BhbiBjbGFzcz0id2l0aCBvcHRpb25zIj4KICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9ImluSW5mbyI+CiAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz0iaW9Db250ZW50Ij48ZGl2IGNsYXNzPSJpbmZvSGVhZGVyIj5JbnB1dDwvZGl2PiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9ImluZm9NZXNzYWdlIj48cHJlPlRfVjJTIHJldHVybiB2YWx1ZTwvcHJlPjwvZGl2PgogICAgICAgICAgICAgICAgICAgIDwvZGl2PgogICAgICAgICAgICAgICAgPC9kaXY+ICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9Im91dEluZm8iPgogICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9ImlvQ29udGVudCI+PGRpdiBjbGFzcz0iaW5mb0hlYWRlciI+T3V0cHV0PC9kaXY+ICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz0iaW5mb01lc3NhZ2UiPjxwcmU+VF9TMlMgcmV0dXJuIHZhbHVlPC9wcmU+PC9kaXY+CiAgICAgICAgICAgICAgICAgICAgPC9kaXY+CiAgICAgICAgICAgICAgICA8L2Rpdj48L3NwYW4+PHVsIGNsYXNzPSJuZXN0ZWQiPjxsaSBjbGFzcz0iaW50ZXJhY3Rpb24gcGFzc2VkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3MgPSAidGltZXN0YW1wIj4yMDIzLTEwLTE4IDA5OjAyOjMyIC0gPC9zcGFuPjxzcGFuIGNsYXNzPSJhY3Rpdml0eU5hbWUiPltTbGVlcF08L3NwYW4+IC0gPHNwYW4gY2xhc3M9ImFjdGl2aXR5RGVzY3JpcHRpb24iPiBwYXVzZSBhbGwgYWN0aXZpdGllcyBmb3IgMCBTZWNvbmRzICgzIG1zKTwvc3Bhbj48L3NwYW4+PGRpdiBjbGFzcz0ibG9uZ0Rlc2NyaXB0aW9uIj48ZGl2IGNsYXNzPSJpbmZvSGVhZGVyIj5GdWxsIERlc2NyaXB0aW9uPC9kaXY+PGRpdiBjbGFzcz0iaW5mb01lc3NhZ2UiPjxwcmU+PC9wcmU+PC9kaXY+PC9kaXY+PC9saT48L3VsPjwvbGk+PC91bD48L2xpPjwvdWw+IAoKPHNjcmlwdD4KdmFyIHRvZ2dsZXIgPSBkb2N1bWVudC5xdWVyeVNlbGVjdG9yQWxsKCIudGFzayIpOwp2YXIgaW5Ub2dnbGVyID0gZG9jdW1lbnQucXVlcnlTZWxlY3RvckFsbCgiLmxhYmVsLmluQ29udGVudEJ1dHRvbiIpOwp2YXIgb3V0VG9nZ2xlciA9IGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3JBbGwoIi5sYWJlbC5vdXRDb250ZW50QnV0dG9uIik7CnZhciBkZXNjVG9nZ2xlciA9IGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3JBbGwoIi5lbGxpcHNlcyIpOwp2YXIgaTsKCmZvciAoaSA9IDA7IGkgPCB0b2dnbGVyLmxlbmd0aDsgaSsrKSB7CiAgdG9nZ2xlcltpXS5hZGRFdmVudExpc3RlbmVyKCJjbGljayIsIGZ1bmN0aW9uKCkgewogICAgdGhpcy5wYXJlbnRFbGVtZW50LnF1ZXJ5U2VsZWN0b3IoIi5uZXN0ZWQiKS5jbGFzc0xpc3QudG9nZ2xlKCJhY3RpdmUiKTsKICAgIHRoaXMuY2xhc3NMaXN0LnRvZ2dsZSgidGFzay1vcGVuIik7CiAgfSk7Cn0KCmZvciAoaSA9IDA7IGkgPCBpblRvZ2dsZXIubGVuZ3RoOyBpKyspIHsKICAgIGluVG9nZ2xlcltpXS5hZGRFdmVudExpc3RlbmVyKCJjbGljayIsIGZ1bmN0aW9uKCkgewogICAgICAgIHRoaXMucGFyZW50RWxlbWVudC5xdWVyeVNlbGVjdG9yKCIuaW5JbmZvIikuY2xhc3NMaXN0LnRvZ2dsZSgiaW5BY3RpdmUiKTsKICAgICAgICB0aGlzLmNsYXNzTGlzdC50b2dnbGUoImFjdGl2ZSIpOwogICAgfSk7Cn0KCmZvciAoaSA9IDA7IGkgPCBvdXRUb2dnbGVyLmxlbmd0aDsgaSsrKSB7CiAgICBvdXRUb2dnbGVyW2ldLmFkZEV2ZW50TGlzdGVuZXIoImNsaWNrIiwgZnVuY3Rpb24oKSB7CiAgICAgICAgdGhpcy5wYXJlbnRFbGVtZW50LnF1ZXJ5U2VsZWN0b3IoIi5vdXRJbmZvIikuY2xhc3NMaXN0LnRvZ2dsZSgib3V0QWN0aXZlIik7CiAgICAgICAgdGhpcy5jbGFzc0xpc3QudG9nZ2xlKCJhY3RpdmUiKTsKICAgIH0pOwp9CmZvciAoaSA9IDA7IGkgPCBkZXNjVG9nZ2xlci5sZW5ndGg7IGkrKykgewogICAgZGVzY1RvZ2dsZXJbaV0uYWRkRXZlbnRMaXN0ZW5lcigiY2xpY2siLCBmdW5jdGlvbigpIHsKICAgICAgICB0aGlzLnBhcmVudEVsZW1lbnQucXVlcnlTZWxlY3RvcigiLmxvbmdEZXNjcmlwdGlvbiIpLmNsYXNzTGlzdC50b2dnbGUoImRlc2NyaXB0aW9uQWN0aXZlIik7CiAgICAgICAgdGhpcy5jbGFzc0xpc3QudG9nZ2xlKCJhY3RpdmUiKTsKICAgIH0pOwp9Cjwvc2NyaXB0Pg==")
//              );
  }


  @Test
  public void attemptsTwoWithOneParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithTwoParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithThreeParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithFourParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithFiveParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithSixParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithSevenParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithEightParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithNineParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithTenParameterCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description");

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }


  @Test
  public void attemptsTwoWithOneParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithTwoParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithThreeParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithFourParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithFiveParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithSixParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithSevenParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithEightParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithNineParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }

  @Test
  public void attemptsTwoWithTenParameterUsingInputCallsFailingTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo$_(
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      TaskReturningVoid.start(),
      T_V2V_Failing.start(),
      "Group", "Group Description")
      .apply(null);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("first entry is group", log.activityNodes.get(0).name, equalTo("Group"));
    assertThat("first entry is group", log.activityNodes.get(0).description, equalTo("Group Description"));
  }


}
