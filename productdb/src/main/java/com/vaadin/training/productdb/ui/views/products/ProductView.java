package com.vaadin.training.productdb.ui.views.products;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.customcrud.BinderCrudEditor;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.training.productdb.ui.utils.converters.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.validator.BigDecimalRangeValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.training.productdb.backend.data.entity.Product;
import com.vaadin.training.productdb.backend.service.ProductService;
import com.vaadin.training.productdb.ui.crud.AbstractCrudView;
import com.vaadin.training.productdb.ui.utils.AppUIConst;
import com.vaadin.training.productdb.ui.utils.converters.CurrencyFormatter;
import com.vaadin.training.productdb.ui.views.MainView;
import com.vaadin.training.productdb.ui.views.products.PriceConverter;

@Route(value = AppUIConst.PAGE_PRODUCTS, layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
public class ProductView extends AbstractCrudView<Product> {

	private CurrencyFormatter currencyFormatter = new CurrencyFormatter();
	
	@Autowired
	public ProductView(ProductService service) {
		super(Product.class, service, new Grid<>(), createForm());
	}

	@Override
	protected String getBasePage() {
		return AppUIConst.PAGE_PRODUCTS;
	}

	@Override
	protected void setupGrid(Grid<Product> grid) {
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.addColumn(Product::getName).setHeader("Product Name").setFlexGrow(5).setSortable(true).setKey("name");
		grid.addColumn(Product::getQuantity).setHeader("Quantity").setTextAlign(ColumnTextAlign.CENTER).setSortable(true).setKey("quantity");
		grid.addColumn(p -> currencyFormatter.encode(p.getPrice())).setHeader("Unit Price").setTextAlign(ColumnTextAlign.CENTER).setSortable(true).setKey("price");
		grid.addColumn(new LocalDateRenderer<>(Product::getManufacturedDate, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Manufactured Date").setTextAlign(ColumnTextAlign.CENTER).setSortable(true).setKey("manufacturedDate");
		grid.setHeight("100%");
		grid.setWidth("100%");
	}

	private static BinderCrudEditor<Product> createForm() {
		TextField name = new TextField("Product name");
		name.getElement().setAttribute("colspan", "2");
		TextField quantity = new TextField("Quantity");
		DatePicker manufacturedDate = new DatePicker("Manufactured Date");
		TextField price = new TextField("Unit price");
		price.getElement().setAttribute("colspan", "2");

		FormLayout form = new FormLayout(name, quantity, manufacturedDate, price);


		BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

		binder.forField(name).bind("name");
		
		binder.forField(quantity).withConverter(new StringToIntegerConverter("Please enter the number")).bind("quantity");
		
		binder.forField(manufacturedDate).bind("manufacturedDate");
		
		binder.forField(price).withConverter(new PriceConverter()).withValidator(new BigDecimalRangeValidator("Invalid value range", new  BigDecimal(0), new  BigDecimal(10000))).bind("price");
		price.setPattern("\\d+(\\.\\d?\\d?)?$");
		price.setPreventInvalidInput(true);

	    String currencySymbol = Currency.getInstance(AppUIConst.APP_LOCALE).getSymbol();
		price.setPrefixComponent(new Span(currencySymbol));

		return new BinderCrudEditor<Product>(binder, form) {
			@Override
			public boolean validate() { // isValid
				return binder.validate().isOk();
			}
		};
	}

}