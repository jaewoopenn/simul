package gen;


public class SysGenMC extends SysGen {
	public SysGenMC(ConfigGen cfg) {
		super(cfg);
	}

	
	@Override
	protected int check() {
		if(!g_isCheck)
			return 1;
		
		return 1;
	}



}
