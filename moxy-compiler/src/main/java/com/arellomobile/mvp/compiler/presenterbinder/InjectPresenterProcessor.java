package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.compiler.ElementProcessor;
import com.arellomobile.mvp.compiler.Util;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class InjectPresenterProcessor extends ElementProcessor<VariableElement, TargetClassInfo> {
	private static final String PRESENTER_FIELD_ANNOTATION = InjectPresenter.class.getName();
	private static final String PROVIDE_PRESENTER_ANNOTATION = ProvidePresenter.class.getName();
	private static final String PROVIDE_PRESENTER_TAG_ANNOTATION = ProvidePresenterTag.class.getName();

	private final List<TypeElement> presentersContainers = new ArrayList<>();

	public List<TypeElement> getPresentersContainers() {
		return new ArrayList<>(presentersContainers);
	}

	@Override
	public TargetClassInfo process(VariableElement variableElement) {
		final Element enclosingElement = variableElement.getEnclosingElement();

		if (!(enclosingElement instanceof TypeElement)) {
			throw new RuntimeException("Only class fields could be annotated as @InjectPresenter: " +
					variableElement + " at " + enclosingElement);
		}

		if (presentersContainers.contains(enclosingElement)) {
			return null;
		}

		final TypeElement presentersContainer = (TypeElement) enclosingElement;
		presentersContainers.add(presentersContainer);

		// gather presenter fields info
		List<TargetPresenterField> fields = collectFields(presentersContainer);
		bindProvidersToFields(fields, collectPresenterProviders(presentersContainer));
		bindTagProvidersToFields(fields, collectTagProviders(presentersContainer));

		return new TargetClassInfo(presentersContainer, fields);
	}

	private static List<TargetPresenterField> collectFields(TypeElement presentersContainer) {
		List<TargetPresenterField> fields = new ArrayList<>();

		for (Element element : presentersContainer.getEnclosedElements()) {
			if (element.getKind() != ElementKind.FIELD) {
				continue;
			}

			AnnotationMirror annotation = Util.getAnnotation(element, PRESENTER_FIELD_ANNOTATION);

			if (annotation == null) {
				continue;
			}

			// TODO: simplify?
			TypeMirror clazz = ((DeclaredType) element.asType()).asElement().asType();

			String name = element.toString();

			String type = Util.getAnnotationValueAsString(annotation, "type");
			String tag = Util.getAnnotationValueAsString(annotation, "tag");
			String presenterId = Util.getAnnotationValueAsString(annotation, "presenterId");

			TargetPresenterField field = new TargetPresenterField(clazz, name, type, tag, presenterId);
			fields.add(field);
		}
		return fields;
	}

	private static List<PresenterProviderMethod> collectPresenterProviders(TypeElement presentersContainer) {
		List<PresenterProviderMethod> providers = new ArrayList<>();

		for (Element element : presentersContainer.getEnclosedElements()) {
			if (element.getKind() != ElementKind.METHOD) {
				continue;
			}

			final ExecutableElement providerMethod = (ExecutableElement) element;

			final AnnotationMirror annotation = Util.getAnnotation(element, PROVIDE_PRESENTER_ANNOTATION);

			if (annotation == null) {
				continue;
			}

			final String name = providerMethod.getSimpleName().toString();
			final DeclaredType kind = ((DeclaredType) providerMethod.getReturnType());

			String type = Util.getAnnotationValueAsString(annotation, "type");
			String tag = Util.getAnnotationValueAsString(annotation, "tag");
			String presenterId = Util.getAnnotationValueAsString(annotation, "presenterId");

			providers.add(new PresenterProviderMethod(kind, name, type, tag, presenterId));
		}
		return providers;
	}

	private static List<TagProviderMethod> collectTagProviders(TypeElement presentersContainer) {
		List<TagProviderMethod> providers = new ArrayList<>();

		for (Element element : presentersContainer.getEnclosedElements()) {
			if (element.getKind() != ElementKind.METHOD) {
				continue;
			}

			final ExecutableElement providerMethod = (ExecutableElement) element;

			final AnnotationMirror annotation = Util.getAnnotation(element, PROVIDE_PRESENTER_TAG_ANNOTATION);

			if (annotation == null) {
				continue;
			}

			final String name = providerMethod.getSimpleName().toString();

			TypeMirror presenterClass = Util.getAnnotationValueAsTypeMirror(annotation, "presenterClass");
			String type = Util.getAnnotationValueAsString(annotation, "type");
			String presenterId = Util.getAnnotationValueAsString(annotation, "presenterId");

			providers.add(new TagProviderMethod(presenterClass, name, type, presenterId));
		}
		return providers;
	}

	private static void bindProvidersToFields(List<TargetPresenterField> fields,
	                                          List<PresenterProviderMethod> presenterProviders) {
		if (fields.isEmpty() || presenterProviders.isEmpty()) {
			return;
		}

		for (PresenterProviderMethod presenterProvider : presenterProviders) {
			TypeMirror providerTypeMirror = presenterProvider.getClazz().asElement().asType();

			for (TargetPresenterField field : fields) {
				if ((field.getClazz()).equals(providerTypeMirror)) {
					if (field.getPresenterType() != presenterProvider.getPresenterType()) {
						continue;
					}

					if (field.getTag() == null && presenterProvider.getTag() != null) {
						continue;
					}
					if (field.getTag() != null && !field.getTag().equals(presenterProvider.getTag())) {
						continue;
					}

					if (field.getPresenterId() == null && presenterProvider.getPresenterId() != null) {
						continue;
					}
					if (field.getPresenterId() != null && !field.getPresenterId().equals(presenterProvider.getPresenterId())) {
						continue;
					}

					field.setPresenterProviderMethodName(presenterProvider.getName());
				}
			}

		}
	}

	private static void bindTagProvidersToFields(List<TargetPresenterField> fields,
	                                             List<TagProviderMethod> tagProviders) {
		if (fields.isEmpty() || tagProviders.isEmpty()) {
			return;
		}

		for (TagProviderMethod tagProvider : tagProviders) {
			for (TargetPresenterField field : fields) {
				if ((field.getClazz()).equals(tagProvider.getPresenterClass())) {
					if (field.getPresenterType() != tagProvider.getType()) {
						continue;
					}

					if (field.getPresenterId() == null && tagProvider.getPresenterId() != null) {
						continue;
					}
					if (field.getPresenterId() != null && !field.getPresenterId().equals(tagProvider.getPresenterId())) {
						continue;
					}

					field.setPresenterTagProviderMethodName(tagProvider.getMethodName());
				}
			}
		}
	}
}
