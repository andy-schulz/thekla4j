package com.teststeps.thekla4j;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.core.activities.Sleep;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

// Task without annotation
class T_V2S extends Task<T_V2S, Void, String> {
    @Override
    public Either<Throwable, String> performAs(Actor actor, Void result) {
        return actor.attemptsTo(
                Sleep.forA(Duration.ofMillis(3))
        ).map(x -> "T_V2S return value");
    }

    public static T_V2S start() {
        return new T_V2S();
    }
}

@Workflow("start a String to String Task")
class T_S2S extends Task<T_S2S, String, String> {
    @Override
    public Either<Throwable, String> performAs(Actor actor, String result) {
        return actor.attemptsTo(
                Sleep.forA(Duration.ofMillis(3))
        ).map(x -> "T_S2S return value");
    }

    public static T_S2S start() {
        return new T_S2S();
    }
}

@Workflow("start a failing Task")
class T_V2V_Failing extends Task<T_V2V_Failing, Void, Void> {
    @Override
    public Either<Throwable, Void> performAs(Actor actor, Void result) {
        return Either.left(new Exception("Failing in T_V2V_Failing Task"));
    }

    public static T_V2V_Failing start() {
        return new T_V2V_Failing();
    }
}

public class ActorActivityLogTest {
    private Actor John = Actor.named("John");

    @Before
    public void init() {
    }

    @Test
    public void generateActorActivityLog() {
        John.attemptsTo(
                Sleep.forA(Duration.ofMillis(1))
        );

        assertThat(John.activityLog.getStructuredLog("\t"),
                equalTo("[✓ START] - John attempts to\n" +
                        "\t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)"));

        assertThat(John.activityLog.getEncodedStructuredHtmlLog(), equalTo("PHN0eWxlPgp1bCwgI0FjdGl2aXR5TG9nIHsNCiAgICBsaXN0LXN0eWxlLXR5cGU6IG5vbmU7DQogICAgZm9udC1mYW1pbHk6IG1vbm9zcGFjZTsNCn0NCg0KI0FjdGl2aXR5TG9nIHsNCiAgICBtYXJnaW46IDA7DQogICAgcGFkZGluZzogMDsNCn0NCg0KLnRhc2sgew0KICAgIGN1cnNvcjogcG9pbnRlcjsNCiAgICAtd2Via2l0LXVzZXItc2VsZWN0OiBub25lOyAvKiBTYWZhcmkgMy4xKyAqLw0KICAgIC1tb3otdXNlci1zZWxlY3Q6IG5vbmU7IC8qIEZpcmVmb3ggMisgKi8NCiAgICAtbXMtdXNlci1zZWxlY3Q6IG5vbmU7IC8qIElFIDEwKyAqLw0KICAgIHVzZXItc2VsZWN0OiBub25lOw0KfQ0KDQoudGFzazo6YmVmb3JlIHsNCiAgICAvKmNvbnRlbnQ6ICJcMjVCNiI7ICovDQogICAgY29udGVudDogIlwyNUI2IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgZGlzcGxheTogaW5saW5lLWJsb2NrOw0KICAgIG1hcmdpbi1yaWdodDogNnB4Ow0KfQ0KDQoudGFzay5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLnRhc2sucGFzc2VkOjpiZWZvcmUgew0KICAgIGNvbG9yOiBncmVlbjsNCn0NCg0KLnRhc2sucnVubmluZzo6YmVmb3JlIHsNCiAgICBjb2xvcjogYmxhY2s7DQp9DQoNCi50YXNrLW9wZW46OmJlZm9yZSB7DQogICAgLW1zLXRyYW5zZm9ybTogcm90YXRlKDkwZGVnKTsgLyogSUUgOSAqLw0KICAgIC13ZWJraXQtdHJhbnNmb3JtOiByb3RhdGUoOTBkZWcpOyAvKiBTYWZhcmkgKi8nDQp0cmFuc2Zvcm06IHJvdGF0ZSg5MGRlZyk7DQp9DQoNCi5uZXN0ZWQgew0KICAgIGRpc3BsYXk6IG5vbmU7DQp9DQoNCi5hY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQouaW50ZXJhY3Rpb246OmJlZm9yZSB7DQogICAgY29udGVudDogIlwyNUI3IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgbWFyZ2luLXJpZ2h0OiA2cHg7DQp9DQoNCi5pbnRlcmFjdGlvbi5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmludGVyYWN0aW9uLnBhc3NlZDo6YmVmb3JlIHsNCiAgICBjb2xvcjogZ3JlZW47DQp9DQoNCi5pbnRlcmFjdGlvbi5ydW5uaW5nOjpiZWZvcmUgew0KICAgIGNvbG9yOiBibGFjazsNCn0NCg0KLmxvZ01lc3NhZ2UuZmFpbCB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmFjdGl2aXR5TmFtZSB7DQogICAgY29sb3I6ICNmZjBiYjE7DQp9Cjwvc3R5bGU+IAoKPHVsIGlkID0gIkFjdGl2aXR5TG9nIj48bGk+PHNwYW4gY2xhc3M9InRhc2sgcGFzc2VkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3M9ImFjdGl2aXR5TmFtZSI+W1NUQVJUXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IEpvaG4gYXR0ZW1wdHMgdG88L3NwYW4+PC9zcGFuPjwvc3Bhbj48dWwgY2xhc3M9Im5lc3RlZCI+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMSBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PC91bD48L2xpPjwvdWw+IAoKPHNjcmlwdD4KdmFyIHRvZ2dsZXIgPSBkb2N1bWVudC5nZXRFbGVtZW50c0J5Q2xhc3NOYW1lKCJ0YXNrIik7CnZhciBpOwoKZm9yIChpID0gMDsgaSA8IHRvZ2dsZXIubGVuZ3RoOyBpKyspIHsKICB0b2dnbGVyW2ldLmFkZEV2ZW50TGlzdGVuZXIoImNsaWNrIiwgZnVuY3Rpb24oKSB7CiAgICB0aGlzLnBhcmVudEVsZW1lbnQucXVlcnlTZWxlY3RvcigiLm5lc3RlZCIpLmNsYXNzTGlzdC50b2dnbGUoImFjdGl2ZSIpOwogICAgdGhpcy5jbGFzc0xpc3QudG9nZ2xlKCJ0YXNrLW9wZW4iKTsKICB9KTsKfQo8L3NjcmlwdD4="));
    }

    @Test
    public void generateFailedActivityLog() {
        John.attemptsTo(
                Sleep.forA(Duration.ofMillis(1)),
                T_V2V_Failing.start()
        );

        assertThat(John.activityLog.getStructuredLog("\t"),
                equalTo("[✗ START] - John attempts to\n" +
                        "\t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)\n" +
                        "\t[✗ T_V2V_Failing] - start a failing Task"));

        assertThat(John.activityLog.getEncodedStructuredHtmlLog(), equalTo("PHN0eWxlPgp1bCwgI0FjdGl2aXR5TG9nIHsNCiAgICBsaXN0LXN0eWxlLXR5cGU6IG5vbmU7DQogICAgZm9udC1mYW1pbHk6IG1vbm9zcGFjZTsNCn0NCg0KI0FjdGl2aXR5TG9nIHsNCiAgICBtYXJnaW46IDA7DQogICAgcGFkZGluZzogMDsNCn0NCg0KLnRhc2sgew0KICAgIGN1cnNvcjogcG9pbnRlcjsNCiAgICAtd2Via2l0LXVzZXItc2VsZWN0OiBub25lOyAvKiBTYWZhcmkgMy4xKyAqLw0KICAgIC1tb3otdXNlci1zZWxlY3Q6IG5vbmU7IC8qIEZpcmVmb3ggMisgKi8NCiAgICAtbXMtdXNlci1zZWxlY3Q6IG5vbmU7IC8qIElFIDEwKyAqLw0KICAgIHVzZXItc2VsZWN0OiBub25lOw0KfQ0KDQoudGFzazo6YmVmb3JlIHsNCiAgICAvKmNvbnRlbnQ6ICJcMjVCNiI7ICovDQogICAgY29udGVudDogIlwyNUI2IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgZGlzcGxheTogaW5saW5lLWJsb2NrOw0KICAgIG1hcmdpbi1yaWdodDogNnB4Ow0KfQ0KDQoudGFzay5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLnRhc2sucGFzc2VkOjpiZWZvcmUgew0KICAgIGNvbG9yOiBncmVlbjsNCn0NCg0KLnRhc2sucnVubmluZzo6YmVmb3JlIHsNCiAgICBjb2xvcjogYmxhY2s7DQp9DQoNCi50YXNrLW9wZW46OmJlZm9yZSB7DQogICAgLW1zLXRyYW5zZm9ybTogcm90YXRlKDkwZGVnKTsgLyogSUUgOSAqLw0KICAgIC13ZWJraXQtdHJhbnNmb3JtOiByb3RhdGUoOTBkZWcpOyAvKiBTYWZhcmkgKi8nDQp0cmFuc2Zvcm06IHJvdGF0ZSg5MGRlZyk7DQp9DQoNCi5uZXN0ZWQgew0KICAgIGRpc3BsYXk6IG5vbmU7DQp9DQoNCi5hY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQouaW50ZXJhY3Rpb246OmJlZm9yZSB7DQogICAgY29udGVudDogIlwyNUI3IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgbWFyZ2luLXJpZ2h0OiA2cHg7DQp9DQoNCi5pbnRlcmFjdGlvbi5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmludGVyYWN0aW9uLnBhc3NlZDo6YmVmb3JlIHsNCiAgICBjb2xvcjogZ3JlZW47DQp9DQoNCi5pbnRlcmFjdGlvbi5ydW5uaW5nOjpiZWZvcmUgew0KICAgIGNvbG9yOiBibGFjazsNCn0NCg0KLmxvZ01lc3NhZ2UuZmFpbCB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmFjdGl2aXR5TmFtZSB7DQogICAgY29sb3I6ICNmZjBiYjE7DQp9Cjwvc3R5bGU+IAoKPHVsIGlkID0gIkFjdGl2aXR5TG9nIj48bGk+PHNwYW4gY2xhc3M9InRhc2sgZmFpbGVkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3M9ImFjdGl2aXR5TmFtZSI+W1NUQVJUXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IEpvaG4gYXR0ZW1wdHMgdG88L3NwYW4+PC9zcGFuPjwvc3Bhbj48dWwgY2xhc3M9Im5lc3RlZCI+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMSBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PGxpPjxzcGFuIGNsYXNzPSJ0YXNrIGZhaWxlZCI+PHNwYW4gY2xhc3M9ImxvZ01lc3NhZ2UiPjxzcGFuIGNsYXNzPSJhY3Rpdml0eU5hbWUiPltUX1YyVl9GYWlsaW5nXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IHN0YXJ0IGEgZmFpbGluZyBUYXNrPC9zcGFuPjwvc3Bhbj48L3NwYW4+PHVsIGNsYXNzPSJuZXN0ZWQiPjwvdWw+PC9saT48L3VsPjwvbGk+PC91bD4gCgo8c2NyaXB0Pgp2YXIgdG9nZ2xlciA9IGRvY3VtZW50LmdldEVsZW1lbnRzQnlDbGFzc05hbWUoInRhc2siKTsKdmFyIGk7Cgpmb3IgKGkgPSAwOyBpIDwgdG9nZ2xlci5sZW5ndGg7IGkrKykgewogIHRvZ2dsZXJbaV0uYWRkRXZlbnRMaXN0ZW5lcigiY2xpY2siLCBmdW5jdGlvbigpIHsKICAgIHRoaXMucGFyZW50RWxlbWVudC5xdWVyeVNlbGVjdG9yKCIubmVzdGVkIikuY2xhc3NMaXN0LnRvZ2dsZSgiYWN0aXZlIik7CiAgICB0aGlzLmNsYXNzTGlzdC50b2dnbGUoInRhc2stb3BlbiIpOwogIH0pOwp9Cjwvc2NyaXB0Pg=="));
    }

    @Test
    public void generateTreeActivityLogWithoutAnyAnnotation() {
        // activities of a task should be listed at the tasks level
        // in case the task does not contain an annotation
        John.attemptsTo(
                Sleep.forA(Duration.ofMillis(1)),
                T_V2S.start()
        );

        assertThat(John.activityLog.getStructuredLog("\t"),
                equalTo("[✓ START] - John attempts to\n" +
                        "\t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)\n" +
                        "\t[✓ Sleep] - pause all activities for 0 Seconds (3 ms)"));

        assertThat(John.activityLog.getEncodedStructuredHtmlLog(),
                equalTo("PHN0eWxlPgp1bCwgI0FjdGl2aXR5TG9nIHsNCiAgICBsaXN0LXN0eWxlLXR5cGU6IG5vbmU7DQogICAgZm9udC1mYW1pbHk6IG1vbm9zcGFjZTsNCn0NCg0KI0FjdGl2aXR5TG9nIHsNCiAgICBtYXJnaW46IDA7DQogICAgcGFkZGluZzogMDsNCn0NCg0KLnRhc2sgew0KICAgIGN1cnNvcjogcG9pbnRlcjsNCiAgICAtd2Via2l0LXVzZXItc2VsZWN0OiBub25lOyAvKiBTYWZhcmkgMy4xKyAqLw0KICAgIC1tb3otdXNlci1zZWxlY3Q6IG5vbmU7IC8qIEZpcmVmb3ggMisgKi8NCiAgICAtbXMtdXNlci1zZWxlY3Q6IG5vbmU7IC8qIElFIDEwKyAqLw0KICAgIHVzZXItc2VsZWN0OiBub25lOw0KfQ0KDQoudGFzazo6YmVmb3JlIHsNCiAgICAvKmNvbnRlbnQ6ICJcMjVCNiI7ICovDQogICAgY29udGVudDogIlwyNUI2IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgZGlzcGxheTogaW5saW5lLWJsb2NrOw0KICAgIG1hcmdpbi1yaWdodDogNnB4Ow0KfQ0KDQoudGFzay5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLnRhc2sucGFzc2VkOjpiZWZvcmUgew0KICAgIGNvbG9yOiBncmVlbjsNCn0NCg0KLnRhc2sucnVubmluZzo6YmVmb3JlIHsNCiAgICBjb2xvcjogYmxhY2s7DQp9DQoNCi50YXNrLW9wZW46OmJlZm9yZSB7DQogICAgLW1zLXRyYW5zZm9ybTogcm90YXRlKDkwZGVnKTsgLyogSUUgOSAqLw0KICAgIC13ZWJraXQtdHJhbnNmb3JtOiByb3RhdGUoOTBkZWcpOyAvKiBTYWZhcmkgKi8nDQp0cmFuc2Zvcm06IHJvdGF0ZSg5MGRlZyk7DQp9DQoNCi5uZXN0ZWQgew0KICAgIGRpc3BsYXk6IG5vbmU7DQp9DQoNCi5hY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQouaW50ZXJhY3Rpb246OmJlZm9yZSB7DQogICAgY29udGVudDogIlwyNUI3IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgbWFyZ2luLXJpZ2h0OiA2cHg7DQp9DQoNCi5pbnRlcmFjdGlvbi5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmludGVyYWN0aW9uLnBhc3NlZDo6YmVmb3JlIHsNCiAgICBjb2xvcjogZ3JlZW47DQp9DQoNCi5pbnRlcmFjdGlvbi5ydW5uaW5nOjpiZWZvcmUgew0KICAgIGNvbG9yOiBibGFjazsNCn0NCg0KLmxvZ01lc3NhZ2UuZmFpbCB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmFjdGl2aXR5TmFtZSB7DQogICAgY29sb3I6ICNmZjBiYjE7DQp9Cjwvc3R5bGU+IAoKPHVsIGlkID0gIkFjdGl2aXR5TG9nIj48bGk+PHNwYW4gY2xhc3M9InRhc2sgcGFzc2VkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3M9ImFjdGl2aXR5TmFtZSI+W1NUQVJUXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IEpvaG4gYXR0ZW1wdHMgdG88L3NwYW4+PC9zcGFuPjwvc3Bhbj48dWwgY2xhc3M9Im5lc3RlZCI+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMSBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMyBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PC91bD48L2xpPjwvdWw+IAoKPHNjcmlwdD4KdmFyIHRvZ2dsZXIgPSBkb2N1bWVudC5nZXRFbGVtZW50c0J5Q2xhc3NOYW1lKCJ0YXNrIik7CnZhciBpOwoKZm9yIChpID0gMDsgaSA8IHRvZ2dsZXIubGVuZ3RoOyBpKyspIHsKICB0b2dnbGVyW2ldLmFkZEV2ZW50TGlzdGVuZXIoImNsaWNrIiwgZnVuY3Rpb24oKSB7CiAgICB0aGlzLnBhcmVudEVsZW1lbnQucXVlcnlTZWxlY3RvcigiLm5lc3RlZCIpLmNsYXNzTGlzdC50b2dnbGUoImFjdGl2ZSIpOwogICAgdGhpcy5jbGFzc0xpc3QudG9nZ2xlKCJ0YXNrLW9wZW4iKTsKICB9KTsKfQo8L3NjcmlwdD4="));
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

        assertThat(John.activityLog.getStructuredLog("\t"),
                equalTo("[✓ START] - John attempts to\n" +
                        "\t[✓ Sleep] - pause all activities for 0 Seconds (1 ms)\n" +
                        "\t[✓ Sleep] - pause all activities for 0 Seconds (3 ms)\n" +
                        "\t[✓ T_S2S] - start a String to String Task\n" +
                        "\t\t[✓ Sleep] - pause all activities for 0 Seconds (3 ms)"));

        System.out.println(John.activityLog.getEncodedStructuredHtmlLog());

        assertThat(John.activityLog.getEncodedStructuredHtmlLog(),
                equalTo("PHN0eWxlPgp1bCwgI0FjdGl2aXR5TG9nIHsNCiAgICBsaXN0LXN0eWxlLXR5cGU6IG5vbmU7DQogICAgZm9udC1mYW1pbHk6IG1vbm9zcGFjZTsNCn0NCg0KI0FjdGl2aXR5TG9nIHsNCiAgICBtYXJnaW46IDA7DQogICAgcGFkZGluZzogMDsNCn0NCg0KLnRhc2sgew0KICAgIGN1cnNvcjogcG9pbnRlcjsNCiAgICAtd2Via2l0LXVzZXItc2VsZWN0OiBub25lOyAvKiBTYWZhcmkgMy4xKyAqLw0KICAgIC1tb3otdXNlci1zZWxlY3Q6IG5vbmU7IC8qIEZpcmVmb3ggMisgKi8NCiAgICAtbXMtdXNlci1zZWxlY3Q6IG5vbmU7IC8qIElFIDEwKyAqLw0KICAgIHVzZXItc2VsZWN0OiBub25lOw0KfQ0KDQoudGFzazo6YmVmb3JlIHsNCiAgICAvKmNvbnRlbnQ6ICJcMjVCNiI7ICovDQogICAgY29udGVudDogIlwyNUI2IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgZGlzcGxheTogaW5saW5lLWJsb2NrOw0KICAgIG1hcmdpbi1yaWdodDogNnB4Ow0KfQ0KDQoudGFzay5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLnRhc2sucGFzc2VkOjpiZWZvcmUgew0KICAgIGNvbG9yOiBncmVlbjsNCn0NCg0KLnRhc2sucnVubmluZzo6YmVmb3JlIHsNCiAgICBjb2xvcjogYmxhY2s7DQp9DQoNCi50YXNrLW9wZW46OmJlZm9yZSB7DQogICAgLW1zLXRyYW5zZm9ybTogcm90YXRlKDkwZGVnKTsgLyogSUUgOSAqLw0KICAgIC13ZWJraXQtdHJhbnNmb3JtOiByb3RhdGUoOTBkZWcpOyAvKiBTYWZhcmkgKi8nDQp0cmFuc2Zvcm06IHJvdGF0ZSg5MGRlZyk7DQp9DQoNCi5uZXN0ZWQgew0KICAgIGRpc3BsYXk6IG5vbmU7DQp9DQoNCi5hY3RpdmUgew0KICAgIGRpc3BsYXk6IGJsb2NrOw0KfQ0KDQouaW50ZXJhY3Rpb246OmJlZm9yZSB7DQogICAgY29udGVudDogIlwyNUI3IjsNCiAgICBjb2xvcjogYmxhY2s7DQogICAgbWFyZ2luLXJpZ2h0OiA2cHg7DQp9DQoNCi5pbnRlcmFjdGlvbi5mYWlsZWQ6OmJlZm9yZSB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmludGVyYWN0aW9uLnBhc3NlZDo6YmVmb3JlIHsNCiAgICBjb2xvcjogZ3JlZW47DQp9DQoNCi5pbnRlcmFjdGlvbi5ydW5uaW5nOjpiZWZvcmUgew0KICAgIGNvbG9yOiBibGFjazsNCn0NCg0KLmxvZ01lc3NhZ2UuZmFpbCB7DQogICAgY29sb3I6IHJlZDsNCn0NCg0KLmFjdGl2aXR5TmFtZSB7DQogICAgY29sb3I6ICNmZjBiYjE7DQp9Cjwvc3R5bGU+IAoKPHVsIGlkID0gIkFjdGl2aXR5TG9nIj48bGk+PHNwYW4gY2xhc3M9InRhc2sgcGFzc2VkIj48c3BhbiBjbGFzcz0ibG9nTWVzc2FnZSI+PHNwYW4gY2xhc3M9ImFjdGl2aXR5TmFtZSI+W1NUQVJUXTwvc3Bhbj4gLSA8c3BhbiBjbGFzcz0iYWN0aXZpdHlEZXNjcmlwdGlvbiI+IEpvaG4gYXR0ZW1wdHMgdG88L3NwYW4+PC9zcGFuPjwvc3Bhbj48dWwgY2xhc3M9Im5lc3RlZCI+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMSBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PGxpIGNsYXNzPSJpbnRlcmFjdGlvbiBwYXNzZWQiPjxzcGFuIGNsYXNzPSJsb2dNZXNzYWdlIj48c3BhbiBjbGFzcz0iYWN0aXZpdHlOYW1lIj5bU2xlZXBdPC9zcGFuPiAtIDxzcGFuIGNsYXNzPSJhY3Rpdml0eURlc2NyaXB0aW9uIj4gcGF1c2UgYWxsIGFjdGl2aXRpZXMgZm9yIDAgU2Vjb25kcyAoMyBtcyk8L3NwYW4+PC9zcGFuPjwvbGk+PGxpPjxzcGFuIGNsYXNzPSJ0YXNrIHBhc3NlZCI+PHNwYW4gY2xhc3M9ImxvZ01lc3NhZ2UiPjxzcGFuIGNsYXNzPSJhY3Rpdml0eU5hbWUiPltUX1MyU108L3NwYW4+IC0gPHNwYW4gY2xhc3M9ImFjdGl2aXR5RGVzY3JpcHRpb24iPiBzdGFydCBhIFN0cmluZyB0byBTdHJpbmcgVGFzazwvc3Bhbj48L3NwYW4+PC9zcGFuPjx1bCBjbGFzcz0ibmVzdGVkIj48bGkgY2xhc3M9ImludGVyYWN0aW9uIHBhc3NlZCI+PHNwYW4gY2xhc3M9ImxvZ01lc3NhZ2UiPjxzcGFuIGNsYXNzPSJhY3Rpdml0eU5hbWUiPltTbGVlcF08L3NwYW4+IC0gPHNwYW4gY2xhc3M9ImFjdGl2aXR5RGVzY3JpcHRpb24iPiBwYXVzZSBhbGwgYWN0aXZpdGllcyBmb3IgMCBTZWNvbmRzICgzIG1zKTwvc3Bhbj48L3NwYW4+PC9saT48L3VsPjwvbGk+PC91bD48L2xpPjwvdWw+IAoKPHNjcmlwdD4KdmFyIHRvZ2dsZXIgPSBkb2N1bWVudC5nZXRFbGVtZW50c0J5Q2xhc3NOYW1lKCJ0YXNrIik7CnZhciBpOwoKZm9yIChpID0gMDsgaSA8IHRvZ2dsZXIubGVuZ3RoOyBpKyspIHsKICB0b2dnbGVyW2ldLmFkZEV2ZW50TGlzdGVuZXIoImNsaWNrIiwgZnVuY3Rpb24oKSB7CiAgICB0aGlzLnBhcmVudEVsZW1lbnQucXVlcnlTZWxlY3RvcigiLm5lc3RlZCIpLmNsYXNzTGlzdC50b2dnbGUoImFjdGl2ZSIpOwogICAgdGhpcy5jbGFzc0xpc3QudG9nZ2xlKCJ0YXNrLW9wZW4iKTsKICB9KTsKfQo8L3NjcmlwdD4="));
    }
}
