package com.vaadin.training.productdb.ui.views;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.training.productdb.ui.utils.AppUIConst;

public interface HasNotifications extends HasElement {
	default void showNotifocation(String message) {
		showNotification(message, false);
	}

	default void showNotification(String message, boolean persistent) {
		if (persistent) {
			Button close = new Button("Close");
			close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			Notification notification = new Notification(new Text(message), close);
			notification.setPosition(Position.BOTTOM_START);
			notification.setDuration(0);
			close.addClickListener(event -> notification.close());
			notification.open();
		} else {
			Notification.show(message, AppUIConst.NOTIFICATION_DURATION, Position.BOTTOM_STRETCH);
		}
	}
}