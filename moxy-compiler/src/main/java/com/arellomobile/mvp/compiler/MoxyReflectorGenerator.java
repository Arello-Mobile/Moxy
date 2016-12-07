package com.arellomobile.mvp.compiler;

import java.util.List;

import com.arellomobile.mvp.MvpProcessor;

/**
 * Date: 07.12.2016
 * Time: 19:05
 *
 * @author Yuri Shmakov
 */

public class MoxyReflectorGenerator {

	public static String generate(List<String> presenterClassNames) {
		String builder = "package com.arellomobile.mvp;\n" +
		                 "\n" +
		                 "import java.util.HashMap;\n" +
		                 "import java.util.Map;\n" +
		                 "\n" +
		                 "import com.arellomobile.mvp.viewstate.MvpViewState;\n" +
		                 "\n" +
		                 "class MoxyReflector {\n\n" +
		                 "\tprivate static Map<Class<?>, Object> sViewStateProviders;\n" +
		                 "\n" +
		                 "\tstatic {\n" +
		                 "\t\tsViewStateProviders = new HashMap<>();\n" +
		                 "\t\t\n";

		for (String presenterClassName : presenterClassNames) {
			  builder += "\t\tsViewStateProviders.put(" + presenterClassName + ".class, new " + presenterClassName + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX + "());\n";
		}

              builder += "\t}\n" +
		                 "\t\n" +
		                 "\tpublic static MvpViewState getViewStateProvider(Class<?> presenterClass) {\n" +
		                 "\t\treturn ((ViewStateProvider) sViewStateProviders.get(presenterClass)).getViewState();\n" +
		                 "\t}\n" +
		                 "\n" +
		                 "\tpublic static Object getPresenterBinders(Class delegated) {\n" +
		                 "\t\tthrow new RuntimeException(\"Stub!\");\n" +
		                 "\t}\n" +
		                 "}\n";

		return builder;
	}
}
