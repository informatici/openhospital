package org.isf.utils.exception;

import java.util.List;

import org.isf.utils.exception.model.OHExceptionMessage;

public class OHServiceValidationException extends OHServiceException {

	public OHServiceValidationException(List<OHExceptionMessage> messages) {
		super(messages);
		// TODO Auto-generated constructor stub
	}

}
