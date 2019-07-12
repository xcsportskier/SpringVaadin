package com.vaadin.training.productdb.ui.utils.converters;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Integer> {
		
		private String errorMessage;
		
		public StringToIntegerConverter(String errorMessage) {
	        this.errorMessage = errorMessage;
	    }
		
		// Should never fail, so returns PRESENTATION type
		public String convertToPresentation(Integer value, ValueContext context) {
			if (value != null) return String.format(context.getLocale().get(), "%d", value);
			return "";
		}

		// Might fail, so returns a Result instead of throwing an Exception
		public Result<Integer> convertToModel(String value, ValueContext context) {
			try {
				return Result.ok(Integer.parseInt(value));
			} catch (NumberFormatException ex) {
				return Result.error(errorMessage);
			}
		}
	}
