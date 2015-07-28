package com.akartkam.inShop.presentation.admin;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
 
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
@Target({METHOD, FIELD })
@Retention(RUNTIME)
public @interface AdminPresentation {
	EditTab tab ();
	int tabOrder() default -1;
}
