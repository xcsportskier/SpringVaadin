package com.vaadin.training.productdb.ui.utils.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.vaadin.flow.templatemodel.ModelEncoder;
import com.vaadin.training.productdb.ui.dataproviders.DataProviderUtil;
import com.vaadin.training.productdb.ui.utils.FormattingUtils;

public class CurrencyFormatter implements ModelEncoder<BigDecimal, String> {

	@Override
	public String encode(BigDecimal modelValue) {
		return DataProviderUtil.convertIfNotNull(modelValue, FormattingUtils::formatAsCurrency);
	}

	@Override
	public BigDecimal decode(String presentationValue) {
		throw new UnsupportedOperationException();
	}
}