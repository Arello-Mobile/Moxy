package com.arellomobile.mvp.view;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 10.02.2016
 * Time: 13:22
 *
 * @author Savin Mikhail
 */
public class CounterTestView implements TestView {

	public final Map<String, Integer> counterEvents = new HashMap<>();

	@Override
	public void testEvent() {
		String testEvent = "testEvent";
		if (counterEvents.containsKey(testEvent)) {
			counterEvents.put(testEvent, counterEvents.get(testEvent) + 1);
		} else {
			counterEvents.put(testEvent, 1);
		}
	}
}
