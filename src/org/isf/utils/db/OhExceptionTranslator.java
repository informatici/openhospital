package org.isf.utils.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OhExceptionTranslator {

	@Around("within(@org.isf.utils.db.TranslateOHException *)")
	public Object translateSqlExceptionToOhException(ProceedingJoinPoint pjp) throws OHException {
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			throw new OHException(e.getMessage(), e);
		}
	}
}
