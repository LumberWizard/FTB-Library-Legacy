package latmod.lib.annotations;

import java.lang.annotation.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumberBounds
{
	double min() default Double.NEGATIVE_INFINITY;
	double max() default Double.POSITIVE_INFINITY;
}