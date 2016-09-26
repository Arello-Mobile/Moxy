package com.arellomobile.mvp;

import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.TYPE;

/**
 * <p>Generate view state class for annotated view interface.</p>
 * <p>Generated class implements this view interface.</p>
 *
 * @deprecated As of release 0.4.1, {@link InjectViewState} generate view state, if it needed
 */
@Target(value = TYPE)
@Deprecated
public @interface GenerateViewState {
}
