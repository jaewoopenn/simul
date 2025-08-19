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
		ConfigGen eg=new ConfigGen(null);
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("p_lb","20");
		eg.setParam("p_ub","150");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.2");
		eg.setParam("r_lb","0.25");
		eg.setParam("r_ub","1.0");
		eg.setParam("mo_lb","0.25");
		eg.setParam("mo_ub","1.0");
		eg.setParam("prob_hi","0.5");
		eg.setParam("num","10");
		eg.setParam("subfix","exp");
		eg.setParam("mod","t");
		return eg;
		
	}
	public static ConfigGen getPredefinedMC()	{
		ConfigGen eg=new ConfigGen(null);
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("p_lb","20");
		eg.setParam("p_ub","150");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.2");
		eg.setParam("r_lb","0.25");
		eg.setParam("r_ub","1.0");
		eg.setParam("mo_lb","0.0");
		eg.setParam("mo_ub","0.0");
		eg.setParam("prob_hi","0.5");
		eg.setParam("num","10");
		eg.setParam("subfix","exp");
		eg.setParam("mod","t");
		return eg;
	}

}
