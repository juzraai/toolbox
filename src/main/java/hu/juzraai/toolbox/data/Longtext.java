package hu.juzraai.toolbox.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field should be mapped to a LONGTEXT field in the database.
 *
 * @author Zsolt Jurányi
 * @since 16.07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Longtext {

}
