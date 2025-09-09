package gen;

public class TGUtil {
	public static void setMC(TaskGenInd t, ConfigGen cfg) {
		setUtil(t,cfg);
		setTUtil(t,cfg);
		setRatioLH(t,cfg);
		setPeriod(t,cfg);
		setProbHI(t,cfg);
	}
	public static void setUtil(TaskGenInd t, ConfigGen cfg) {
		double l=cfg.readDbl("u_lb");
		double u=cfg.readDbl("u_ub");
		t.setUtil(l,u);
	}
	public static void setTUtil(TaskGenInd t, ConfigGen cfg) {
		double l=cfg.readDbl("tu_lb");
		double u=cfg.readDbl("tu_ub");
		t.setTUtil(l,u);
	}
	public static void setRatioLH(TaskGenInd t,ConfigGen cfg) {
		t.setRatioLH(cfg.readDbl("r_lb"),cfg.readDbl("r_ub"));
	}
	public static void setMoLH(TaskGenInd t,ConfigGen cfg) {
		t.setMoLH(cfg.readDbl("mo_lb"),cfg.readDbl("mo_ub"));
	}
	public static void setPeriod(TaskGenInd t,ConfigGen cfg) {
		t.setPeriod(cfg.readInt("p_lb"),cfg.readInt("p_ub"));
	}
	public static void setProbHI(TaskGenInd tgp, ConfigGen cfg) {
		tgp.setProbHI(cfg.readDbl("prob_hi"));		
	}
	public static TaskGenInd getDefaultMC(){
		TaskGenInd tgp=new TaskGenInd();
		tgp.setRatioLH(0.25, 1);
		tgp.setUtil(0.45,0.5);
		tgp.setPeriod(300,500);
		tgp.setTUtil(0.01,0.1);
		tgp.setProbHI(0.5);
		return tgp;
		
	}
}
