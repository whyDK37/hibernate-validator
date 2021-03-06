/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.internal.engine;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidatorContext;
import org.hibernate.validator.internal.util.ExecutableParameterNameProvider;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @author Kevin Pollet &lt;kevin.pollet@serli.com&gt; (C) 2011 SERLI
 * @author Gunnar Morling
 * @author Chris Beckey &lt;cbeckey@paypal.com&gt;
 */
public class ValidatorContextImpl implements HibernateValidatorContext {

	private final ValidatorFactoryImpl validatorFactory;

	private MessageInterpolator messageInterpolator;
	private TraversableResolver traversableResolver;
	private ConstraintValidatorFactory constraintValidatorFactory;
	private ExecutableParameterNameProvider parameterNameProvider;
	private boolean failFast;
	private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;
	private TimeProvider timeProvider;
	private final MethodValidationConfiguration methodValidationConfiguration = new MethodValidationConfiguration();


	public ValidatorContextImpl(ValidatorFactoryImpl validatorFactory) {
		this.validatorFactory = validatorFactory;
		this.messageInterpolator = validatorFactory.getMessageInterpolator();
		this.traversableResolver = validatorFactory.getTraversableResolver();
		this.constraintValidatorFactory = validatorFactory.getConstraintValidatorFactory();
		this.parameterNameProvider = validatorFactory.getExecutableParameterNameProvider();
		this.failFast = validatorFactory.isFailFast();
		this.validatedValueHandlers = new ArrayList<>(
				validatorFactory.getValidatedValueHandlers()
		);
		this.timeProvider = validatorFactory.getTimeProvider();
	}

	@Override
	public HibernateValidatorContext messageInterpolator(MessageInterpolator messageInterpolator) {
		if ( messageInterpolator == null ) {
			this.messageInterpolator = validatorFactory.getMessageInterpolator();
		}
		else {
			this.messageInterpolator = messageInterpolator;
		}
		return this;
	}

	@Override
	public HibernateValidatorContext traversableResolver(TraversableResolver traversableResolver) {
		if ( traversableResolver == null ) {
			this.traversableResolver = validatorFactory.getTraversableResolver();
		}
		else {
			this.traversableResolver = traversableResolver;
		}
		return this;
	}

	@Override
	public HibernateValidatorContext constraintValidatorFactory(ConstraintValidatorFactory factory) {
		if ( factory == null ) {
			this.constraintValidatorFactory = validatorFactory.getConstraintValidatorFactory();
		}
		else {
			this.constraintValidatorFactory = factory;
		}
		return this;
	}

	@Override
	public HibernateValidatorContext parameterNameProvider(ParameterNameProvider parameterNameProvider) {
		if ( parameterNameProvider == null ) {
			this.parameterNameProvider = validatorFactory.getExecutableParameterNameProvider();
		}
		else {
			this.parameterNameProvider = new ExecutableParameterNameProvider( parameterNameProvider );
		}
		return this;
	}

	@Override
	public HibernateValidatorContext failFast(boolean failFast) {
		this.failFast = failFast;
		return this;
	}

	@Override
	public HibernateValidatorContext addValidationValueHandler(ValidatedValueUnwrapper<?> handler) {
		this.validatedValueHandlers.add( handler );
		return this;
	}

	@Override
	public HibernateValidatorContext timeProvider(TimeProvider timeProvider) {
		if ( timeProvider == null ) {
			this.timeProvider = validatorFactory.getTimeProvider();
		}
		else {
			this.timeProvider = timeProvider;
		}
		return this;
	}

	@Override
	public HibernateValidatorContext allowOverridingMethodAlterParameterConstraint(boolean allow) {
		this.methodValidationConfiguration.allowOverridingMethodAlterParameterConstraint( allow );
		return this;
	}

	@Override
	public HibernateValidatorContext allowMultipleCascadedValidationOnReturnValues(boolean allow) {
		this.methodValidationConfiguration.allowMultipleCascadedValidationOnReturnValues( allow );
		return this;
	}

	@Override
	public HibernateValidatorContext allowParallelMethodsDefineParameterConstraints(boolean allow) {
		this.methodValidationConfiguration.allowParallelMethodsDefineParameterConstraints( allow );
		return this;
	}

	@Override
	public Validator getValidator() {
		return validatorFactory.createValidator(
				constraintValidatorFactory,
				messageInterpolator,
				traversableResolver,
				parameterNameProvider,
				failFast,
				validatedValueHandlers,
				timeProvider,
				methodValidationConfiguration
		);
	}
}
