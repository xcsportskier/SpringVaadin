package com.vaadin.training.productdb.ui.views.products;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.training.productdb.ui.dataproviders.DataProviderUtil;
import com.vaadin.training.productdb.ui.utils.FormattingUtils;

class PriceConverter implements Converter<String, BigDecimal> {

	private final DecimalFormat df = FormattingUtils.getUiPriceFormatter();

	@Override
	public Result<BigDecimal> convertToModel(String presentationValue, ValueContext valueContext) {
		try {
			return Result.ok((new BigDecimal(df.parse(presentationValue).doubleValue())));
		} catch (ParseException e) {
			return Result.error("Invalid value");
		}
	}

	@Override
	public String convertToPresentation(BigDecimal modelValue, ValueContext valueContext) {
		return DataProviderUtil.convertIfNotNull(modelValue, i -> df.format(modelValue), () -> "");
	}
}