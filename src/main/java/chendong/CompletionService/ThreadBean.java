package chendong.CompletionService;

import java.util.Random;
import java.util.concurrent.Callable;

public class ThreadBean implements Callable<Bean>{

	int num;
	
	
	public ThreadBean(int num) {
		super();
		this.num = num;
	}


	@Override
	public Bean call() throws Exception {
		Bean bean = new Bean();
		bean.setRandom(new Random().nextDouble());
		bean.setThreadNum(num);
		return bean;
	}
}
