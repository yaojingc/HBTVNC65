package nc.bs.er.exp.cmd;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


/**
 * 新建一个VO 用来查询与计算劳务费相关复杂参数时做交互
 * @author yao
 */
public class ServiceFeeCalcVO extends SuperVO {

	private static final long serialVersionUID = 6325485899252502575L;
	
	public static final String APPROVER = "approver";
	public static final String IDCARDNO = "idcardno";
	public static final String YFJE = "yfje";
	public static final String LJYF = "ljyf";
	public static final String BCYJKS = "bcyjks";
	public static final String ZBKS = "zbks";
	public static final String SFJE = "sfje";
	public static final String BODYDATE = "bodydate";
	
	
	// 审批人
	private String approver;
	
	// 当前报销人身份证号
	private String idcardno;
	
	// 应发金额
	private UFDouble yfje;
	
	// 累计应发
	private UFDouble ljyf;
	
	// 本次预计扣税
	private UFDouble bcyjks;
	
	// 增补扣税
	private UFDouble zbks;
	
	// 实发金额
	private UFDouble sfje;
	
	// 时间戳
	private UFDate bodydate;
	
	

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getIdcardno() {
		return idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public UFDouble getYfje() {
		return yfje;
	}

	public void setYfje(UFDouble yfje) {
		this.yfje = yfje;
	}

	public UFDouble getLjyf() {
		return ljyf;
	}

	public void setLjyf(UFDouble ljyf) {
		this.ljyf = ljyf;
	}

	public UFDouble getBcyjks() {
		return bcyjks;
	}

	public void setBcyjks(UFDouble bcyjks) {
		this.bcyjks = bcyjks;
	}

	public UFDouble getZbks() {
		return zbks;
	}

	public void setZbks(UFDouble zbks) {
		this.zbks = zbks;
	}

	public UFDouble getSfje() {
		return sfje;
	}

	public void setSfje(UFDouble sfje) {
		this.sfje = sfje;
	}

	public UFDate getDate() {
		return bodydate;
	}

	public void setDate(UFDate bodydate) {
		this.bodydate = bodydate;
	}
	
}
