package com.example.intern;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ExecutorProvider {
	final static ExecutorService executorService = Executors.newFixedThreadPool(4);
	public static final ExecutorService getExecutorService(){
		return executorService;
	}
}
