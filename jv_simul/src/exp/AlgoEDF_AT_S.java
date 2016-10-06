package exp;

import basic.Task;
import basic.TaskMng;

public class AlgoEDF_AT_S implements ISchAlgo {

	@Override
	public void initMode(TaskMng tm) {
		Task[] tasks=tm.getTasks();
		for(Task t:tasks){
			if(!t.is_HI)
				continue;
			if(t.getHiUtil()<t.getLoRUtil())
				t.is_HM=true;
		}
	}

}
