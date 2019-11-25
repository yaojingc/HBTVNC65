package nc.vo.test.test01;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加类的描述信息
 * </p>
 *  创建日期:2019-10-29
 * @author YONYOU NC
 * @version NCPrj ??
 */
public class Entity1 extends nc.vo.pub.SuperVO{
	
    private java.lang.String pk_servicequest;
    private java.lang.String pk_sqcustdemand0;
    private java.lang.String rowno0;
    private java.lang.String custrequirement0;
    private java.lang.String billmaker0;
    private nc.vo.pub.lang.UFDate maketime0;
    private java.lang.String approver0;
    private nc.vo.pub.lang.UFDateTime approvedate0;
    private java.lang.String def0;
    private java.lang.String def1;
    private java.lang.String def2;
    private java.lang.String def3;
    private java.lang.String def4;
    private java.lang.String def5;
    private java.lang.String def6;
    private java.lang.String def7;
    private java.lang.String def8;
    private java.lang.String def10;
    private java.lang.String def11;
    private java.lang.String def12;
    private java.lang.String def13;
    private java.lang.String def14;
    private java.lang.String def15;
    private java.lang.String def16;
    private java.lang.String def17;
    private java.lang.String def18;
    private java.lang.String def19;
    private java.lang.String def20;
    private java.lang.Integer dr = 0;
    private nc.vo.pub.lang.UFDateTime ts;    
	
	
    public static final String PK_SERVICEQUEST = "pk_servicequest";
    public static final String PK_SQCUSTDEMAND0 = "pk_sqcustdemand0";
    public static final String ROWNO0 = "rowno0";
    public static final String CUSTREQUIREMENT0 = "custrequirement0";
    public static final String BILLMAKER0 = "billmaker0";
    public static final String MAKETIME0 = "maketime0";
    public static final String APPROVER0 = "approver0";
    public static final String APPROVEDATE0 = "approvedate0";
    public static final String DEF0 = "def0";
    public static final String DEF1 = "def1";
    public static final String DEF2 = "def2";
    public static final String DEF3 = "def3";
    public static final String DEF4 = "def4";
    public static final String DEF5 = "def5";
    public static final String DEF6 = "def6";
    public static final String DEF7 = "def7";
    public static final String DEF8 = "def8";
    public static final String DEF10 = "def10";
    public static final String DEF11 = "def11";
    public static final String DEF12 = "def12";
    public static final String DEF13 = "def13";
    public static final String DEF14 = "def14";
    public static final String DEF15 = "def15";
    public static final String DEF16 = "def16";
    public static final String DEF17 = "def17";
    public static final String DEF18 = "def18";
    public static final String DEF19 = "def19";
    public static final String DEF20 = "def20";

	/**
	 * 属性 pk_servicequest的Getter方法.属性名：parentPK
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_servicequest () {
		return pk_servicequest;
	}   
	/**
	 * 属性pk_servicequest的Setter方法.属性名：parentPK
	 * 创建日期:2019-10-29
	 * @param newPk_servicequest java.lang.String
	 */
	public void setPk_servicequest (java.lang.String newPk_servicequest ) {
	 	this.pk_servicequest = newPk_servicequest;
	} 	 
	
	/**
	 * 属性 pk_sqcustdemand0的Getter方法.属性名：主键0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_sqcustdemand0 () {
		return pk_sqcustdemand0;
	}   
	/**
	 * 属性pk_sqcustdemand0的Setter方法.属性名：主键0
	 * 创建日期:2019-10-29
	 * @param newPk_sqcustdemand0 java.lang.String
	 */
	public void setPk_sqcustdemand0 (java.lang.String newPk_sqcustdemand0 ) {
	 	this.pk_sqcustdemand0 = newPk_sqcustdemand0;
	} 	 
	
	/**
	 * 属性 rowno0的Getter方法.属性名：序号0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getRowno0 () {
		return rowno0;
	}   
	/**
	 * 属性rowno0的Setter方法.属性名：序号0
	 * 创建日期:2019-10-29
	 * @param newRowno0 java.lang.String
	 */
	public void setRowno0 (java.lang.String newRowno0 ) {
	 	this.rowno0 = newRowno0;
	} 	 
	
	/**
	 * 属性 custrequirement0的Getter方法.属性名：客户需求0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getCustrequirement0 () {
		return custrequirement0;
	}   
	/**
	 * 属性custrequirement0的Setter方法.属性名：客户需求0
	 * 创建日期:2019-10-29
	 * @param newCustrequirement0 java.lang.String
	 */
	public void setCustrequirement0 (java.lang.String newCustrequirement0 ) {
	 	this.custrequirement0 = newCustrequirement0;
	} 	 
	
	/**
	 * 属性 billmaker0的Getter方法.属性名：填写人0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getBillmaker0 () {
		return billmaker0;
	}   
	/**
	 * 属性billmaker0的Setter方法.属性名：填写人0
	 * 创建日期:2019-10-29
	 * @param newBillmaker0 java.lang.String
	 */
	public void setBillmaker0 (java.lang.String newBillmaker0 ) {
	 	this.billmaker0 = newBillmaker0;
	} 	 
	
	/**
	 * 属性 maketime0的Getter方法.属性名：填写日期0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getMaketime0 () {
		return maketime0;
	}   
	/**
	 * 属性maketime0的Setter方法.属性名：填写日期0
	 * 创建日期:2019-10-29
	 * @param newMaketime0 nc.vo.pub.lang.UFDate
	 */
	public void setMaketime0 (nc.vo.pub.lang.UFDate newMaketime0 ) {
	 	this.maketime0 = newMaketime0;
	} 	 
	
	/**
	 * 属性 approver0的Getter方法.属性名：审批人0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getApprover0 () {
		return approver0;
	}   
	/**
	 * 属性approver0的Setter方法.属性名：审批人0
	 * 创建日期:2019-10-29
	 * @param newApprover0 java.lang.String
	 */
	public void setApprover0 (java.lang.String newApprover0 ) {
	 	this.approver0 = newApprover0;
	} 	 
	
	/**
	 * 属性 approvedate0的Getter方法.属性名：审批日期0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getApprovedate0 () {
		return approvedate0;
	}   
	/**
	 * 属性approvedate0的Setter方法.属性名：审批日期0
	 * 创建日期:2019-10-29
	 * @param newApprovedate0 nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate0 (nc.vo.pub.lang.UFDateTime newApprovedate0 ) {
	 	this.approvedate0 = newApprovedate0;
	} 	 
	
	/**
	 * 属性 def0的Getter方法.属性名：填写人员0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef0 () {
		return def0;
	}   
	/**
	 * 属性def0的Setter方法.属性名：填写人员0
	 * 创建日期:2019-10-29
	 * @param newDef0 java.lang.String
	 */
	public void setDef0 (java.lang.String newDef0 ) {
	 	this.def0 = newDef0;
	} 	 
	
	/**
	 * 属性 def1的Getter方法.属性名：自定义项0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * 属性def1的Setter方法.属性名：自定义项0
	 * 创建日期:2019-10-29
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * 属性 def2的Getter方法.属性名：自定义项1
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * 属性def2的Setter方法.属性名：自定义项1
	 * 创建日期:2019-10-29
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * 属性 def3的Getter方法.属性名：自定义项2
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * 属性def3的Setter方法.属性名：自定义项2
	 * 创建日期:2019-10-29
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * 属性 def4的Getter方法.属性名：自定义项3
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * 属性def4的Setter方法.属性名：自定义项3
	 * 创建日期:2019-10-29
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * 属性 def5的Getter方法.属性名：自定义项4
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * 属性def5的Setter方法.属性名：自定义项4
	 * 创建日期:2019-10-29
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * 属性 def6的Getter方法.属性名：自定义项5
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * 属性def6的Setter方法.属性名：自定义项5
	 * 创建日期:2019-10-29
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * 属性 def7的Getter方法.属性名：自定义项6
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * 属性def7的Setter方法.属性名：自定义项6
	 * 创建日期:2019-10-29
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * 属性 def8的Getter方法.属性名：自定义项7
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * 属性def8的Setter方法.属性名：自定义项7
	 * 创建日期:2019-10-29
	 * @param newDef8 java.lang.String
	 */
	public void setDef8 (java.lang.String newDef8 ) {
	 	this.def8 = newDef8;
	} 	 
	
	/**
	 * 属性 def10的Getter方法.属性名：自定义项10
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef10 () {
		return def10;
	}   
	/**
	 * 属性def10的Setter方法.属性名：自定义项10
	 * 创建日期:2019-10-29
	 * @param newDef10 java.lang.String
	 */
	public void setDef10 (java.lang.String newDef10 ) {
	 	this.def10 = newDef10;
	} 	 
	
	/**
	 * 属性 def11的Getter方法.属性名：自定义项11
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef11 () {
		return def11;
	}   
	/**
	 * 属性def11的Setter方法.属性名：自定义项11
	 * 创建日期:2019-10-29
	 * @param newDef11 java.lang.String
	 */
	public void setDef11 (java.lang.String newDef11 ) {
	 	this.def11 = newDef11;
	} 	 
	
	/**
	 * 属性 def12的Getter方法.属性名：自定义项12
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef12 () {
		return def12;
	}   
	/**
	 * 属性def12的Setter方法.属性名：自定义项12
	 * 创建日期:2019-10-29
	 * @param newDef12 java.lang.String
	 */
	public void setDef12 (java.lang.String newDef12 ) {
	 	this.def12 = newDef12;
	} 	 
	
	/**
	 * 属性 def13的Getter方法.属性名：自定义项13
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef13 () {
		return def13;
	}   
	/**
	 * 属性def13的Setter方法.属性名：自定义项13
	 * 创建日期:2019-10-29
	 * @param newDef13 java.lang.String
	 */
	public void setDef13 (java.lang.String newDef13 ) {
	 	this.def13 = newDef13;
	} 	 
	
	/**
	 * 属性 def14的Getter方法.属性名：自定义项14
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef14 () {
		return def14;
	}   
	/**
	 * 属性def14的Setter方法.属性名：自定义项14
	 * 创建日期:2019-10-29
	 * @param newDef14 java.lang.String
	 */
	public void setDef14 (java.lang.String newDef14 ) {
	 	this.def14 = newDef14;
	} 	 
	
	/**
	 * 属性 def15的Getter方法.属性名：自定义项15
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef15 () {
		return def15;
	}   
	/**
	 * 属性def15的Setter方法.属性名：自定义项15
	 * 创建日期:2019-10-29
	 * @param newDef15 java.lang.String
	 */
	public void setDef15 (java.lang.String newDef15 ) {
	 	this.def15 = newDef15;
	} 	 
	
	/**
	 * 属性 def16的Getter方法.属性名：自定义项16
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef16 () {
		return def16;
	}   
	/**
	 * 属性def16的Setter方法.属性名：自定义项16
	 * 创建日期:2019-10-29
	 * @param newDef16 java.lang.String
	 */
	public void setDef16 (java.lang.String newDef16 ) {
	 	this.def16 = newDef16;
	} 	 
	
	/**
	 * 属性 def17的Getter方法.属性名：自定义项17
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef17 () {
		return def17;
	}   
	/**
	 * 属性def17的Setter方法.属性名：自定义项17
	 * 创建日期:2019-10-29
	 * @param newDef17 java.lang.String
	 */
	public void setDef17 (java.lang.String newDef17 ) {
	 	this.def17 = newDef17;
	} 	 
	
	/**
	 * 属性 def18的Getter方法.属性名：自定义项18
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef18 () {
		return def18;
	}   
	/**
	 * 属性def18的Setter方法.属性名：自定义项18
	 * 创建日期:2019-10-29
	 * @param newDef18 java.lang.String
	 */
	public void setDef18 (java.lang.String newDef18 ) {
	 	this.def18 = newDef18;
	} 	 
	
	/**
	 * 属性 def19的Getter方法.属性名：自定义项19
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef19 () {
		return def19;
	}   
	/**
	 * 属性def19的Setter方法.属性名：自定义项19
	 * 创建日期:2019-10-29
	 * @param newDef19 java.lang.String
	 */
	public void setDef19 (java.lang.String newDef19 ) {
	 	this.def19 = newDef19;
	} 	 
	
	/**
	 * 属性 def20的Getter方法.属性名：自定义项20
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef20 () {
		return def20;
	}   
	/**
	 * 属性def20的Setter方法.属性名：自定义项20
	 * 创建日期:2019-10-29
	 * @param newDef20 java.lang.String
	 */
	public void setDef20 (java.lang.String newDef20 ) {
	 	this.def20 = newDef20;
	} 	 
	
	/**
	 * 属性 dr的Getter方法.属性名：dr
	 *  创建日期:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:2019-10-29
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * 属性 ts的Getter方法.属性名：ts
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.属性名：ts
	 * 创建日期:2019-10-29
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2019-10-29
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {		
		return "pk_servicequest";
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2019-10-29
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_sqcustdemand0";
	}
    
	/**
	 * <p>返回表名称
	 * <p>
	 * 创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "test_Entity1";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "test_Entity1";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2019-10-29
	  */
     public Entity1() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.test.test01.Entity1" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("test.Entity1");
		
   	}
     
}