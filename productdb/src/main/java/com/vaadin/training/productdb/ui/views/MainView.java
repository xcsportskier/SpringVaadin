package com.vaadin.training.productdb.ui.views;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

public class MainView extends VerticalLayout implements RouterLayout {

	private Div childWrapper = new Div();

	public MainView(){
		setSizeFull();
		add(childWrapper);
		childWrapper.setSizeFull();
	}
	
	@Override
	public void showRouterLayoutContent(HasElement content) {
		childWrapper.getElement().appendChild(content.getElement());
	}

}
