package nc.vo.test.test01;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�������������Ϣ
 * </p>
 *  ��������:2019-10-29
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
	 * ���� pk_servicequest��Getter����.��������parentPK
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_servicequest () {
		return pk_servicequest;
	}   
	/**
	 * ����pk_servicequest��Setter����.��������parentPK
	 * ��������:2019-10-29
	 * @param newPk_servicequest java.lang.String
	 */
	public void setPk_servicequest (java.lang.String newPk_servicequest ) {
	 	this.pk_servicequest = newPk_servicequest;
	} 	 
	
	/**
	 * ���� pk_sqcustdemand0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_sqcustdemand0 () {
		return pk_sqcustdemand0;
	}   
	/**
	 * ����pk_sqcustdemand0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newPk_sqcustdemand0 java.lang.String
	 */
	public void setPk_sqcustdemand0 (java.lang.String newPk_sqcustdemand0 ) {
	 	this.pk_sqcustdemand0 = newPk_sqcustdemand0;
	} 	 
	
	/**
	 * ���� rowno0��Getter����.�����������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getRowno0 () {
		return rowno0;
	}   
	/**
	 * ����rowno0��Setter����.�����������0
	 * ��������:2019-10-29
	 * @param newRowno0 java.lang.String
	 */
	public void setRowno0 (java.lang.String newRowno0 ) {
	 	this.rowno0 = newRowno0;
	} 	 
	
	/**
	 * ���� custrequirement0��Getter����.���������ͻ�����0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getCustrequirement0 () {
		return custrequirement0;
	}   
	/**
	 * ����custrequirement0��Setter����.���������ͻ�����0
	 * ��������:2019-10-29
	 * @param newCustrequirement0 java.lang.String
	 */
	public void setCustrequirement0 (java.lang.String newCustrequirement0 ) {
	 	this.custrequirement0 = newCustrequirement0;
	} 	 
	
	/**
	 * ���� billmaker0��Getter����.����������д��0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getBillmaker0 () {
		return billmaker0;
	}   
	/**
	 * ����billmaker0��Setter����.����������д��0
	 * ��������:2019-10-29
	 * @param newBillmaker0 java.lang.String
	 */
	public void setBillmaker0 (java.lang.String newBillmaker0 ) {
	 	this.billmaker0 = newBillmaker0;
	} 	 
	
	/**
	 * ���� maketime0��Getter����.����������д����0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getMaketime0 () {
		return maketime0;
	}   
	/**
	 * ����maketime0��Setter����.����������д����0
	 * ��������:2019-10-29
	 * @param newMaketime0 nc.vo.pub.lang.UFDate
	 */
	public void setMaketime0 (nc.vo.pub.lang.UFDate newMaketime0 ) {
	 	this.maketime0 = newMaketime0;
	} 	 
	
	/**
	 * ���� approver0��Getter����.��������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getApprover0 () {
		return approver0;
	}   
	/**
	 * ����approver0��Setter����.��������������0
	 * ��������:2019-10-29
	 * @param newApprover0 java.lang.String
	 */
	public void setApprover0 (java.lang.String newApprover0 ) {
	 	this.approver0 = newApprover0;
	} 	 
	
	/**
	 * ���� approvedate0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getApprovedate0 () {
		return approvedate0;
	}   
	/**
	 * ����approvedate0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newApprovedate0 nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate0 (nc.vo.pub.lang.UFDateTime newApprovedate0 ) {
	 	this.approvedate0 = newApprovedate0;
	} 	 
	
	/**
	 * ���� def0��Getter����.����������д��Ա0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef0 () {
		return def0;
	}   
	/**
	 * ����def0��Setter����.����������д��Ա0
	 * ��������:2019-10-29
	 * @param newDef0 java.lang.String
	 */
	public void setDef0 (java.lang.String newDef0 ) {
	 	this.def0 = newDef0;
	} 	 
	
	/**
	 * ���� def1��Getter����.���������Զ�����0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * ����def1��Setter����.���������Զ�����0
	 * ��������:2019-10-29
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * ���� def2��Getter����.���������Զ�����1
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * ����def2��Setter����.���������Զ�����1
	 * ��������:2019-10-29
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * ���� def3��Getter����.���������Զ�����2
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * ����def3��Setter����.���������Զ�����2
	 * ��������:2019-10-29
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * ���� def4��Getter����.���������Զ�����3
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * ����def4��Setter����.���������Զ�����3
	 * ��������:2019-10-29
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * ���� def5��Getter����.���������Զ�����4
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * ����def5��Setter����.���������Զ�����4
	 * ��������:2019-10-29
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * ���� def6��Getter����.���������Զ�����5
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * ����def6��Setter����.���������Զ�����5
	 * ��������:2019-10-29
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * ���� def7��Getter����.���������Զ�����6
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * ����def7��Setter����.���������Զ�����6
	 * ��������:2019-10-29
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * ���� def8��Getter����.���������Զ�����7
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * ����def8��Setter����.���������Զ�����7
	 * ��������:2019-10-29
	 * @param newDef8 java.lang.String
	 */
	public void setDef8 (java.lang.String newDef8 ) {
	 	this.def8 = newDef8;
	} 	 
	
	/**
	 * ���� def10��Getter����.���������Զ�����10
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef10 () {
		return def10;
	}   
	/**
	 * ����def10��Setter����.���������Զ�����10
	 * ��������:2019-10-29
	 * @param newDef10 java.lang.String
	 */
	public void setDef10 (java.lang.String newDef10 ) {
	 	this.def10 = newDef10;
	} 	 
	
	/**
	 * ���� def11��Getter����.���������Զ�����11
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef11 () {
		return def11;
	}   
	/**
	 * ����def11��Setter����.���������Զ�����11
	 * ��������:2019-10-29
	 * @param newDef11 java.lang.String
	 */
	public void setDef11 (java.lang.String newDef11 ) {
	 	this.def11 = newDef11;
	} 	 
	
	/**
	 * ���� def12��Getter����.���������Զ�����12
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef12 () {
		return def12;
	}   
	/**
	 * ����def12��Setter����.���������Զ�����12
	 * ��������:2019-10-29
	 * @param newDef12 java.lang.String
	 */
	public void setDef12 (java.lang.String newDef12 ) {
	 	this.def12 = newDef12;
	} 	 
	
	/**
	 * ���� def13��Getter����.���������Զ�����13
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef13 () {
		return def13;
	}   
	/**
	 * ����def13��Setter����.���������Զ�����13
	 * ��������:2019-10-29
	 * @param newDef13 java.lang.String
	 */
	public void setDef13 (java.lang.String newDef13 ) {
	 	this.def13 = newDef13;
	} 	 
	
	/**
	 * ���� def14��Getter����.���������Զ�����14
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef14 () {
		return def14;
	}   
	/**
	 * ����def14��Setter����.���������Զ�����14
	 * ��������:2019-10-29
	 * @param newDef14 java.lang.String
	 */
	public void setDef14 (java.lang.String newDef14 ) {
	 	this.def14 = newDef14;
	} 	 
	
	/**
	 * ���� def15��Getter����.���������Զ�����15
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef15 () {
		return def15;
	}   
	/**
	 * ����def15��Setter����.���������Զ�����15
	 * ��������:2019-10-29
	 * @param newDef15 java.lang.String
	 */
	public void setDef15 (java.lang.String newDef15 ) {
	 	this.def15 = newDef15;
	} 	 
	
	/**
	 * ���� def16��Getter����.���������Զ�����16
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef16 () {
		return def16;
	}   
	/**
	 * ����def16��Setter����.���������Զ�����16
	 * ��������:2019-10-29
	 * @param newDef16 java.lang.String
	 */
	public void setDef16 (java.lang.String newDef16 ) {
	 	this.def16 = newDef16;
	} 	 
	
	/**
	 * ���� def17��Getter����.���������Զ�����17
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef17 () {
		return def17;
	}   
	/**
	 * ����def17��Setter����.���������Զ�����17
	 * ��������:2019-10-29
	 * @param newDef17 java.lang.String
	 */
	public void setDef17 (java.lang.String newDef17 ) {
	 	this.def17 = newDef17;
	} 	 
	
	/**
	 * ���� def18��Getter����.���������Զ�����18
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef18 () {
		return def18;
	}   
	/**
	 * ����def18��Setter����.���������Զ�����18
	 * ��������:2019-10-29
	 * @param newDef18 java.lang.String
	 */
	public void setDef18 (java.lang.String newDef18 ) {
	 	this.def18 = newDef18;
	} 	 
	
	/**
	 * ���� def19��Getter����.���������Զ�����19
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef19 () {
		return def19;
	}   
	/**
	 * ����def19��Setter����.���������Զ�����19
	 * ��������:2019-10-29
	 * @param newDef19 java.lang.String
	 */
	public void setDef19 (java.lang.String newDef19 ) {
	 	this.def19 = newDef19;
	} 	 
	
	/**
	 * ���� def20��Getter����.���������Զ�����20
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef20 () {
		return def20;
	}   
	/**
	 * ����def20��Setter����.���������Զ�����20
	 * ��������:2019-10-29
	 * @param newDef20 java.lang.String
	 */
	public void setDef20 (java.lang.String newDef20 ) {
	 	this.def20 = newDef20;
	} 	 
	
	/**
	 * ���� dr��Getter����.��������dr
	 *  ��������:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.��������dr
	 * ��������:2019-10-29
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * ���� ts��Getter����.��������ts
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.��������ts
	 * ��������:2019-10-29
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2019-10-29
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {		
		return "pk_servicequest";
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2019-10-29
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_sqcustdemand0";
	}
    
	/**
	 * <p>���ر�����
	 * <p>
	 * ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "test_Entity1";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2019-10-29
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "test_Entity1";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2019-10-29
	  */
     public Entity1() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.test.test01.Entity1" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("test.Entity1");
		
   	}
     
}