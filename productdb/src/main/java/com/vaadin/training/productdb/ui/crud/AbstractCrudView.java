package com.vaadin.training.productdb.ui.crud;

import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.flow.component.customcrud.Crud;
import com.vaadin.flow.component.customcrud.CrudEditor;
import com.vaadin.flow.component.customcrud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.training.productdb.app.HasLogger;
import com.vaadin.training.productdb.backend.data.entity.AbstractEntity;
import com.vaadin.training.productdb.backend.data.entity.util.EntityUtil;
import com.vaadin.training.productdb.backend.service.FilterableCrudService;
import com.vaadin.training.productdb.ui.views.HasNotifications;
import com.vaadin.training.productdb.ui.components.ToolBar;
import com.vaadin.training.productdb.ui.crud.CrudEntityDataProvider;
import com.vaadin.training.productdb.ui.utils.TemplateUtil;
import com.vaadin.training.productdb.ui.crud.CrudEntityPresenter;

import elemental.json.Json;

@SuppressWarnings("serial")
public abstract class AbstractCrudView<E extends AbstractEntity> extends Crud<E>
		implements HasUrlParameter<Integer>, HasNotifications, HasLogger {
	private static final String DISCARD_MESSAGE = "There are unsaved modifications to the %s. Discard changes?";
	private static final String DELETE_MESSAGE = "Are you sure you want to delete the selected %s? This action cannot be undone.";

	private final Grid<E> grid;

	private final CrudEntityPresenter<E> entityPresenter;

	protected abstract String getBasePage();

	protected abstract void setupGrid(Grid<E> grid);

	@SuppressWarnings("unchecked")
	public AbstractCrudView(Class<E> beanType, FilterableCrudService<E> service, Grid<E> grid, CrudEditor<E> editor) {
		super(beanType, grid, editor);
		this.grid = grid;

		CrudI18n crudI18n = CrudI18n.createDefault();
		String entityName = EntityUtil.getName(beanType);
		crudI18n.setNewItem("New " + entityName);
		crudI18n.setEditItem("Edit " + entityName);
		crudI18n.setEditLabel("Edit " + entityName);
		crudI18n.getConfirm().getCancel().setContent(String.format(DISCARD_MESSAGE, entityName));
		crudI18n.getConfirm().getDelete().setContent(String.format(DELETE_MESSAGE, entityName));
		crudI18n.setDeleteItem("Delete");
		setI18n(crudI18n);

		CrudEntityDataProvider<E> dataProvider = new CrudEntityDataProvider<>(service);
		grid.setDataProvider(dataProvider);
		setupGrid(grid);
		Crud.addEditColumn(grid);
		setHeight("100%");
		setWidth("100%");

		ToolBar toolbar = new ToolBar();
		toolbar.setActionText("New " + entityName);
		toolbar.addFilterChangeListener(e -> dataProvider.setFilter(toolbar.getFilter()));
		setToolbar(toolbar);
		getElement().getStyle().set("flex-direction", "column-reverse");

		entityPresenter = new CrudEntityPresenter<>(service, this);

		setupCrudEventListeners(entityPresenter);
	}

	private void setupCrudEventListeners(CrudEntityPresenter<E> entityPresenter) {
		Consumer<E> onSuccess = entity -> navigateToEntity(null);

		Consumer<E> onFail = entity -> {
			// TODO: https://github.com/vaadin/vaadin-crud-flow/issues/76
			// Throw an exception whenever it is supported by component
		};

		addEditListener(e -> entityPresenter.loadEntity(e.getItem().getId(),
				entity -> navigateToEntity(entity.getId().toString())));

		addCancelListener(e -> {
			navigateToEntity(null);
			grid.select(e.getItem());
		});

		addSaveListener(e -> {
			E entity = e.getItem();
			entityPresenter.save(entity, onSuccess, onFail);
			grid.select(entity);
		});

		addDeleteListener(e -> entityPresenter.delete(e.getItem(), onSuccess, onFail));
	}

	protected void navigateToEntity(String id) {
		getUI().ifPresent(ui -> ui.navigate(TemplateUtil.generateLocation(getBasePage(), id)));
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
		if (id != null) {
			E item = getEditor().getItem();
			if (item != null && id.equals(item.getId())) {
				return;
			}
			entityPresenter.loadEntity(id, this::edit);
		}
	}

	private void edit(E entity) {
		// TODO: Use component API after
		// https://github.com/vaadin/vaadin-crud-flow/issues/68
		getElement().callFunction("__edit",
				Json.instance().parse("{\"key\":\"" + grid.getDataCommunicator().getKeyMapper().key(entity) + "\"}"));
	}
}