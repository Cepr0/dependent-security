package io.github.cepr0.dependent_security.security;

import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions#spel-evaluationcontext-extension-model
 * @author Cepro, 2017-09-02
 */
@Component
public class SecurityEvaluationContextExtension extends EvaluationContextExtensionSupport {

	@Override
	public String getExtensionId() {
		return "security";
	}

	@Override
	public SecurityExpressionRoot getRootObject() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return new SecurityExpressionRoot(authentication) {
		};
	}
}
