package com.arellomobile.mvp.memory_leak_test;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.memory_leak_test.resources.TestViewImplementation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import static org.junit.Assert.assertTrue;

/**
 * Date: 29.12.2016
 * Time: 14:29
 *
 * @author Yuri Shmakov
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MemoryLeakTest {
	@Test
	public void test() {
		TestViewImplementation viewImplementation = new TestViewImplementation();

		viewImplementation.delegate = new MvpDelegate<>(viewImplementation);

		viewImplementation.delegate.onCreate(new Bundle());

		viewImplementation.delegate.onDestroy();

		WeakReference viewImplementationReference = new WeakReference(viewImplementation);
		WeakReference presenterReference = new WeakReference(viewImplementation.presenter);

		/**
		 * Remove local reference to this object. Test will been failed if reference to the implemented view or
		 * to presenter was being saved in Moxy
		 */
		//noinspection UnusedAssignment
		viewImplementation = null;

		long delay = 0;

		while (delay < TimeUnit.SECONDS.toMillis(2)) {
			System.gc();

			if (viewImplementationReference.get() == null && presenterReference.get() == null) {
				return;
			}

			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			delay += 100;
		}

		assertTrue(false);
	}
}