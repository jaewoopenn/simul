package gen;


public class ConfigPre {
	public static ConfigGen getPredefined()	{
		return getPredefinedMC();
		
	}
	public static ConfigGen getPredefined(boolean isMC)	{
		if(isMC) {
			return getPredefinedMC();
		} else {
			return getPredefinedIMC();
			
		}
	}
	public static ConfigGen getPredefinedIMC()	{
		ConfigGen eg=new ConfigGen();
		eg.setPar("u_lb","0.95");
		eg.setPar("u_ub","1.0");
		eg.setPar("p_lb","20");
		eg.setPar("p_ub","150");
		eg.setPar("tu_lb","0.02");
		eg.setPar("tu_ub","0.2");
		eg.setPar("r_lb","0.25");
		eg.setPar("r_ub","1.0");
		eg.setPar("mo_lb","0.25");
		eg.setPar("mo_ub","1.0");
		eg.setPar("prob_hi","0.5");
		eg.setPar("num","10");
		eg.setPar("subfix","exp");
		eg.setPar("mod","t");
		return eg;
		
	}
	public static ConfigGen getPredefinedMC()	{
		ConfigGen eg=new ConfigGen();
		eg.setPar("u_lb","0.95");
		eg.setPar("u_ub","1.0");
		eg.setPar("p_lb","20");
		eg.setPar("p_ub","150");
		eg.setPar("tu_lb","0.02");
		eg.setPar("tu_ub","0.2");
		eg.setPar("r_lb","0.25");
		eg.setPar("r_ub","1.0");
		eg.setPar("mo_lb","0.0");
		eg.setPar("mo_ub","0.0");
		eg.setPar("prob_hi","0.5");
		eg.setPar("num","10");
		eg.setPar("subfix","exp");
		eg.setPar("mod","t");
		return eg;
	}

}
