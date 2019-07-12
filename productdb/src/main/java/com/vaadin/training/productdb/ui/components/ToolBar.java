package com.vaadin.training.productdb.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DebouncePhase;

public class ToolBar extends Composite<HorizontalLayout> implements HasSize {
	
	TextField textField = new TextField();
	Button newItemButton = new Button(VaadinIcon.PLUS.create());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected HorizontalLayout initContent() {
		HorizontalLayout hl = new HorizontalLayout();
		textField.setPlaceholder("Search...");
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.setPrefixComponent(VaadinIcon.SEARCH.create());

		Icon closeIcon = new Icon("lumo", "cross");
		closeIcon.setVisible(false);
		ComponentUtil.addListener(closeIcon, ClickEvent.class,
				(ComponentEventListener) e -> textField.clear());
		textField.setSuffixComponent(closeIcon);

		textField.getElement().addEventListener("value-changed", event -> {
			closeIcon.setVisible(!textField.getValue().isEmpty());
			fireEvent(new FilterChanged(this, false));
		}).debounce(300, DebouncePhase.TRAILING);
		
		newItemButton.getElement().setAttribute("new-button", true);
		newItemButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		hl.setWidth("100%");
		hl.setAlignItems(Alignment.CENTER);
				
		hl.setFlexGrow(9, textField);
		hl.setFlexGrow(1, newItemButton);
		
		hl.add(textField, newItemButton);
		return hl;
	}
	
	public String getFilter() {
		return textField.getValue();
	}
	
	public void setPlaceHolder(String placeHolder) {
		textField.setPlaceholder(placeHolder);
	}

	public void setActionText(String actionText) {
		newItemButton.setText(actionText);
	}

	public void addFilterChangeListener(ComponentEventListener<FilterChanged> listener) {
		this.addListener(FilterChanged.class, listener);
	}

	public void addActionClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		newItemButton.addClickListener(listener);
	}
	
	public static class FilterChanged extends ComponentEvent<ToolBar> {
		public FilterChanged(ToolBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}
}
