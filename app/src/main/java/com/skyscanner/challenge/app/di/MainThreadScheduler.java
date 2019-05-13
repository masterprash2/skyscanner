package com.skyscanner.challenge.app.di;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface MainThreadScheduler {
}
