package com.arellomobile.mvp.compiler.viewstateprovider;

import com.arellomobile.mvp.DefaultView;
import com.arellomobile.mvp.DefaultViewState;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.compiler.ElementProcessor;
import com.arellomobile.mvp.compiler.MvpCompiler;
import com.arellomobile.mvp.compiler.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.arellomobile.mvp.compiler.Util.fillGenerics;

public class InjectViewStateProcessor extends ElementProcessor<TypeElement, PresenterInfo> {
	private static final String MVP_PRESENTER_CLASS = MvpPresenter.class.getCanonicalName();

	private final Set<TypeElement> usedViews = new HashSet<>();
	private final List<TypeElement> presenterClassNames = new ArrayList<>();

	public Set<TypeElement> getUsedViews() {
		return usedViews;
	}

	public List<TypeElement> getPresenterClassNames() {
		return presenterClassNames;
	}

	@Override
	public PresenterInfo process(TypeElement element) {
		presenterClassNames.add(element);
		return new PresenterInfo(element, getViewStateClassName(element));
	}

	private String getViewStateClassName(TypeElement typeElement) {
		String viewState = getViewStateClassFromAnnotationParams(typeElement);
		if (viewState == null) {
			String view = getViewClassFromAnnotationParams(typeElement);
			if (view == null) {
				view = getViewClassFromGeneric(typeElement);
			}

			if (view != null) {
				// Remove generic from view class name
				if (view.contains("<")) {
					view = view.substring(0, view.indexOf("<"));
				}

				TypeElement viewTypeElement = MvpCompiler.getElementUtils().getTypeElement(view);
				if (viewTypeElement == null) {
					throw new IllegalArgumentException("View \"" + view + "\" for " + typeElement + " cannot be found");
				}

				usedViews.add(viewTypeElement);
				viewState = Util.getFullClassName(viewTypeElement) + MvpProcessor.VIEW_STATE_SUFFIX;
			}
		}

		if (viewState != null) {
			return viewState;
		} else {
			return null;
		}
	}

	private String getViewClassFromAnnotationParams(TypeElement typeElement) {
		InjectViewState annotation = typeElement.getAnnotation(InjectViewState.class);
		String mvpViewClassName = "";

		if (annotation != null) {
			TypeMirror value = null;
			try {
				annotation.view();
			} catch (MirroredTypeException mte) {
				value = mte.getTypeMirror();
			}

			mvpViewClassName = Util.getFullClassName(value);
		}

		if (mvpViewClassName.isEmpty() || DefaultView.class.getName().equals(mvpViewClassName)) {
			return null;
		}

		return mvpViewClassName;
	}

	private String getViewStateClassFromAnnotationParams(TypeElement typeElement) {
		InjectViewState annotation = typeElement.getAnnotation(InjectViewState.class);
		String mvpViewStateClassName = "";

		if (annotation != null) {
			TypeMirror value;
			try {
				annotation.value();
			} catch (MirroredTypeException mte) {
				value = mte.getTypeMirror();
				mvpViewStateClassName = value.toString();
			}
		}

		if (mvpViewStateClassName.isEmpty() || DefaultViewState.class.getName().equals(mvpViewStateClassName)) {
			return null;
		}

		return mvpViewStateClassName;
	}

	private String getViewClassFromGeneric(TypeElement typeElement) {
		TypeMirror superclass = typeElement.asType();

		Map<String, String> parentTypes = Collections.emptyMap();

		if (!typeElement.getTypeParameters().isEmpty()) {
			MvpCompiler.getMessager().printMessage(Diagnostic.Kind.WARNING, "Your " + typeElement.getSimpleName() + " is typed. @InjectViewState may generate wrong code. Your can set view/view state class manually.");
		}

		while (superclass.getKind() != TypeKind.NONE) {
			TypeElement superclassElement = (TypeElement) ((DeclaredType) superclass).asElement();

			final List<? extends TypeMirror> typeArguments = ((DeclaredType) superclass).getTypeArguments();
			final List<? extends TypeParameterElement> typeParameters = superclassElement.getTypeParameters();

			if (typeArguments.size() > typeParameters.size()) {
				throw new IllegalArgumentException("Code generation for interface " + typeElement.getSimpleName() + " failed. Simplify your generics. (" + typeArguments + " vs " + typeParameters + ")");
			}

			Map<String, String> types = new HashMap<>();
			for (int i = 0; i < typeArguments.size(); i++) {
				types.put(typeParameters.get(i).toString(), fillGenerics(parentTypes, typeArguments.get(i)));
			}

			if (superclassElement.toString().equals(MVP_PRESENTER_CLASS)) {
				// MvpPresenter is typed only on View class
				return fillGenerics(parentTypes, typeArguments);
			}

			parentTypes = types;

			superclass = superclassElement.getSuperclass();
		}

		return "";
	}
}
