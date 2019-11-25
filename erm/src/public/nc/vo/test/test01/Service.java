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
public class Service extends nc.vo.pub.SuperVO{
	
    private java.lang.String pk_servicequest0;
    private java.lang.String pk_group0;
    private java.lang.String pk_org0;
    private java.lang.String pk_org_v0;
    private java.lang.String vbillcode0;
    private nc.vo.pub.lang.UFDate dbilldate0;
    private java.lang.Integer fstatusflag0;
    private java.lang.String pk_busitype0;
    private java.lang.String pk_billtypeid0;
    private java.lang.String billtype0;
    private java.lang.String transtypecode0;
    private java.lang.String pk_transtype0;
    private java.lang.String billmaker0;
    private nc.vo.pub.lang.UFDate maketime0;
    private java.lang.String creator0;
    private nc.vo.pub.lang.UFDateTime creationtime0;
    private java.lang.String modifier0;
    private nc.vo.pub.lang.UFDateTime modifiedtime0;
    private java.lang.String approver0;
    private java.lang.Integer approvestatus0;
    private java.lang.String approvenote0;
    private nc.vo.pub.lang.UFDateTime approvedate0;
    private java.lang.String vnote0;
    private java.lang.String customers0;
    private java.lang.String srcbilltype0;
    private java.lang.String srcbillid0;
    private java.lang.String srccode0;
    private java.lang.String srcid0;
    private java.lang.String srcbid0;
    private java.lang.String srcrowno0;
    private java.lang.String srctrantype0;
    private java.lang.String vfirsttype0;
    private java.lang.String vfirstcode0;
    private java.lang.String vfirstid0;
    private java.lang.String vfirstbid0;
    private java.lang.String vfirstrowno0;
    private java.lang.String vfirsttrantype0;
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
    private java.lang.String def21;
    private java.lang.String def22;
    private java.lang.String def23;
    private java.lang.String def24;
    private java.lang.String def25;
    private java.lang.String def26;
    private java.lang.String def27;
    private java.lang.String def28;
    private java.lang.String def29;
    private java.lang.String def30;
    private java.lang.String dept0;
    private nc.vo.pub.lang.UFDateTime receptdate0;
    private java.lang.String projectno0;
    private java.lang.String projectname0;
    private java.lang.String productname0;
    private java.lang.String shipowner0;
    private java.lang.String shipno0;
    private java.lang.String shipname0;
    private java.lang.String shiptype0;
    private java.lang.String servicelocation0;
    private java.lang.String grindersdept0;
    private nc.vo.pub.lang.UFDateTime deliverytime0;
    private nc.vo.pub.lang.UFDateTime arrivaltime0;
    private java.lang.String serviceperiod0;
    private java.lang.String aftersalescust0;
    private java.lang.String linkman0;
    private java.lang.String phonenumber0;
    private java.lang.String guaranteeperiod0;
    private java.lang.Integer servicetype0;
    private java.lang.Integer businesstype0;
    private java.lang.Integer productstatus0;
    private java.lang.Integer processingmode0;
    private nc.vo.pub.lang.UFBoolean isgeneratedown0;
    private java.lang.String shipyard0;
    private java.lang.String sendtheunit0;
    private java.lang.String productattribute0;
    private java.lang.String shipclub0;
    private java.lang.String area0;
    private java.lang.String dutypersion0;
    private nc.vo.pub.lang.UFDate deliverydate0;
    private java.lang.String deliproduct0;
    private java.lang.String projectdeco0;
    private nc.vo.pub.lang.UFBoolean istechproposal0;
    private java.lang.Integer dr = 0;
    private nc.vo.pub.lang.UFDateTime ts;    
	
    private nc.vo.test.test01.Entity1[] id_entity1;
	
    public static final String PK_SERVICEQUEST0 = "pk_servicequest0";
    public static final String PK_GROUP0 = "pk_group0";
    public static final String PK_ORG0 = "pk_org0";
    public static final String PK_ORG_V0 = "pk_org_v0";
    public static final String VBILLCODE0 = "vbillcode0";
    public static final String DBILLDATE0 = "dbilldate0";
    public static final String FSTATUSFLAG0 = "fstatusflag0";
    public static final String PK_BUSITYPE0 = "pk_busitype0";
    public static final String PK_BILLTYPEID0 = "pk_billtypeid0";
    public static final String BILLTYPE0 = "billtype0";
    public static final String TRANSTYPECODE0 = "transtypecode0";
    public static final String PK_TRANSTYPE0 = "pk_transtype0";
    public static final String BILLMAKER0 = "billmaker0";
    public static final String MAKETIME0 = "maketime0";
    public static final String CREATOR0 = "creator0";
    public static final String CREATIONTIME0 = "creationtime0";
    public static final String MODIFIER0 = "modifier0";
    public static final String MODIFIEDTIME0 = "modifiedtime0";
    public static final String APPROVER0 = "approver0";
    public static final String APPROVESTATUS0 = "approvestatus0";
    public static final String APPROVENOTE0 = "approvenote0";
    public static final String APPROVEDATE0 = "approvedate0";
    public static final String VNOTE0 = "vnote0";
    public static final String CUSTOMERS0 = "customers0";
    public static final String SRCBILLTYPE0 = "srcbilltype0";
    public static final String SRCBILLID0 = "srcbillid0";
    public static final String SRCCODE0 = "srccode0";
    public static final String SRCID0 = "srcid0";
    public static final String SRCBID0 = "srcbid0";
    public static final String SRCROWNO0 = "srcrowno0";
    public static final String SRCTRANTYPE0 = "srctrantype0";
    public static final String VFIRSTTYPE0 = "vfirsttype0";
    public static final String VFIRSTCODE0 = "vfirstcode0";
    public static final String VFIRSTID0 = "vfirstid0";
    public static final String VFIRSTBID0 = "vfirstbid0";
    public static final String VFIRSTROWNO0 = "vfirstrowno0";
    public static final String VFIRSTTRANTYPE0 = "vfirsttrantype0";
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
    public static final String DEF21 = "def21";
    public static final String DEF22 = "def22";
    public static final String DEF23 = "def23";
    public static final String DEF24 = "def24";
    public static final String DEF25 = "def25";
    public static final String DEF26 = "def26";
    public static final String DEF27 = "def27";
    public static final String DEF28 = "def28";
    public static final String DEF29 = "def29";
    public static final String DEF30 = "def30";
    public static final String DEPT0 = "dept0";
    public static final String RECEPTDATE0 = "receptdate0";
    public static final String PROJECTNO0 = "projectno0";
    public static final String PROJECTNAME0 = "projectname0";
    public static final String PRODUCTNAME0 = "productname0";
    public static final String SHIPOWNER0 = "shipowner0";
    public static final String SHIPNO0 = "shipno0";
    public static final String SHIPNAME0 = "shipname0";
    public static final String SHIPTYPE0 = "shiptype0";
    public static final String SERVICELOCATION0 = "servicelocation0";
    public static final String GRINDERSDEPT0 = "grindersdept0";
    public static final String DELIVERYTIME0 = "deliverytime0";
    public static final String ARRIVALTIME0 = "arrivaltime0";
    public static final String SERVICEPERIOD0 = "serviceperiod0";
    public static final String AFTERSALESCUST0 = "aftersalescust0";
    public static final String LINKMAN0 = "linkman0";
    public static final String PHONENUMBER0 = "phonenumber0";
    public static final String GUARANTEEPERIOD0 = "guaranteeperiod0";
    public static final String SERVICETYPE0 = "servicetype0";
    public static final String BUSINESSTYPE0 = "businesstype0";
    public static final String PRODUCTSTATUS0 = "productstatus0";
    public static final String PROCESSINGMODE0 = "processingmode0";
    public static final String ISGENERATEDOWN0 = "isgeneratedown0";
    public static final String SHIPYARD0 = "shipyard0";
    public static final String SENDTHEUNIT0 = "sendtheunit0";
    public static final String PRODUCTATTRIBUTE0 = "productattribute0";
    public static final String SHIPCLUB0 = "shipclub0";
    public static final String AREA0 = "area0";
    public static final String DUTYPERSION0 = "dutypersion0";
    public static final String DELIVERYDATE0 = "deliverydate0";
    public static final String DELIPRODUCT0 = "deliproduct0";
    public static final String PROJECTDECO0 = "projectdeco0";
    public static final String ISTECHPROPOSAL0 = "istechproposal0";

	/**
	 * 属性 pk_servicequest0的Getter方法.属性名：主键0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_servicequest0 () {
		return pk_servicequest0;
	}   
	/**
	 * 属性pk_servicequest0的Setter方法.属性名：主键0
	 * 创建日期:2019-10-29
	 * @param newPk_servicequest0 java.lang.String
	 */
	public void setPk_servicequest0 (java.lang.String newPk_servicequest0 ) {
	 	this.pk_servicequest0 = newPk_servicequest0;
	} 	 
	
	/**
	 * 属性 pk_group0的Getter方法.属性名：集团0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group0 () {
		return pk_group0;
	}   
	/**
	 * 属性pk_group0的Setter方法.属性名：集团0
	 * 创建日期:2019-10-29
	 * @param newPk_group0 java.lang.String
	 */
	public void setPk_group0 (java.lang.String newPk_group0 ) {
	 	this.pk_group0 = newPk_group0;
	} 	 
	
	/**
	 * 属性 pk_org0的Getter方法.属性名：组织0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org0 () {
		return pk_org0;
	}   
	/**
	 * 属性pk_org0的Setter方法.属性名：组织0
	 * 创建日期:2019-10-29
	 * @param newPk_org0 java.lang.String
	 */
	public void setPk_org0 (java.lang.String newPk_org0 ) {
	 	this.pk_org0 = newPk_org0;
	} 	 
	
	/**
	 * 属性 pk_org_v0的Getter方法.属性名：组织版本0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v0 () {
		return pk_org_v0;
	}   
	/**
	 * 属性pk_org_v0的Setter方法.属性名：组织版本0
	 * 创建日期:2019-10-29
	 * @param newPk_org_v0 java.lang.String
	 */
	public void setPk_org_v0 (java.lang.String newPk_org_v0 ) {
	 	this.pk_org_v0 = newPk_org_v0;
	} 	 
	
	/**
	 * 属性 vbillcode0的Getter方法.属性名：单据号0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVbillcode0 () {
		return vbillcode0;
	}   
	/**
	 * 属性vbillcode0的Setter方法.属性名：单据号0
	 * 创建日期:2019-10-29
	 * @param newVbillcode0 java.lang.String
	 */
	public void setVbillcode0 (java.lang.String newVbillcode0 ) {
	 	this.vbillcode0 = newVbillcode0;
	} 	 
	
	/**
	 * 属性 dbilldate0的Getter方法.属性名：单据日期0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDbilldate0 () {
		return dbilldate0;
	}   
	/**
	 * 属性dbilldate0的Setter方法.属性名：单据日期0
	 * 创建日期:2019-10-29
	 * @param newDbilldate0 nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate0 (nc.vo.pub.lang.UFDate newDbilldate0 ) {
	 	this.dbilldate0 = newDbilldate0;
	} 	 
	
	/**
	 * 属性 fstatusflag0的Getter方法.属性名：单据状态0
	 *  创建日期:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getFstatusflag0 () {
		return fstatusflag0;
	}   
	/**
	 * 属性fstatusflag0的Setter方法.属性名：单据状态0
	 * 创建日期:2019-10-29
	 * @param newFstatusflag0 java.lang.Integer
	 */
	public void setFstatusflag0 (java.lang.Integer newFstatusflag0 ) {
	 	this.fstatusflag0 = newFstatusflag0;
	} 	 
	
	/**
	 * 属性 pk_busitype0的Getter方法.属性名：业务类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_busitype0 () {
		return pk_busitype0;
	}   
	/**
	 * 属性pk_busitype0的Setter方法.属性名：业务类型0
	 * 创建日期:2019-10-29
	 * @param newPk_busitype0 java.lang.String
	 */
	public void setPk_busitype0 (java.lang.String newPk_busitype0 ) {
	 	this.pk_busitype0 = newPk_busitype0;
	} 	 
	
	/**
	 * 属性 pk_billtypeid0的Getter方法.属性名：单据类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtypeid0 () {
		return pk_billtypeid0;
	}   
	/**
	 * 属性pk_billtypeid0的Setter方法.属性名：单据类型0
	 * 创建日期:2019-10-29
	 * @param newPk_billtypeid0 java.lang.String
	 */
	public void setPk_billtypeid0 (java.lang.String newPk_billtypeid0 ) {
	 	this.pk_billtypeid0 = newPk_billtypeid0;
	} 	 
	
	/**
	 * 属性 billtype0的Getter方法.属性名：单据类型编码0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getBilltype0 () {
		return billtype0;
	}   
	/**
	 * 属性billtype0的Setter方法.属性名：单据类型编码0
	 * 创建日期:2019-10-29
	 * @param newBilltype0 java.lang.String
	 */
	public void setBilltype0 (java.lang.String newBilltype0 ) {
	 	this.billtype0 = newBilltype0;
	} 	 
	
	/**
	 * 属性 transtypecode0的Getter方法.属性名：交易类型编码0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getTranstypecode0 () {
		return transtypecode0;
	}   
	/**
	 * 属性transtypecode0的Setter方法.属性名：交易类型编码0
	 * 创建日期:2019-10-29
	 * @param newTranstypecode0 java.lang.String
	 */
	public void setTranstypecode0 (java.lang.String newTranstypecode0 ) {
	 	this.transtypecode0 = newTranstypecode0;
	} 	 
	
	/**
	 * 属性 pk_transtype0的Getter方法.属性名：交易类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_transtype0 () {
		return pk_transtype0;
	}   
	/**
	 * 属性pk_transtype0的Setter方法.属性名：交易类型0
	 * 创建日期:2019-10-29
	 * @param newPk_transtype0 java.lang.String
	 */
	public void setPk_transtype0 (java.lang.String newPk_transtype0 ) {
	 	this.pk_transtype0 = newPk_transtype0;
	} 	 
	
	/**
	 * 属性 billmaker0的Getter方法.属性名：制单人0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getBillmaker0 () {
		return billmaker0;
	}   
	/**
	 * 属性billmaker0的Setter方法.属性名：制单人0
	 * 创建日期:2019-10-29
	 * @param newBillmaker0 java.lang.String
	 */
	public void setBillmaker0 (java.lang.String newBillmaker0 ) {
	 	this.billmaker0 = newBillmaker0;
	} 	 
	
	/**
	 * 属性 maketime0的Getter方法.属性名：制单时间0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getMaketime0 () {
		return maketime0;
	}   
	/**
	 * 属性maketime0的Setter方法.属性名：制单时间0
	 * 创建日期:2019-10-29
	 * @param newMaketime0 nc.vo.pub.lang.UFDate
	 */
	public void setMaketime0 (nc.vo.pub.lang.UFDate newMaketime0 ) {
	 	this.maketime0 = newMaketime0;
	} 	 
	
	/**
	 * 属性 creator0的Getter方法.属性名：创建人0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getCreator0 () {
		return creator0;
	}   
	/**
	 * 属性creator0的Setter方法.属性名：创建人0
	 * 创建日期:2019-10-29
	 * @param newCreator0 java.lang.String
	 */
	public void setCreator0 (java.lang.String newCreator0 ) {
	 	this.creator0 = newCreator0;
	} 	 
	
	/**
	 * 属性 creationtime0的Getter方法.属性名：创建时间0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime0 () {
		return creationtime0;
	}   
	/**
	 * 属性creationtime0的Setter方法.属性名：创建时间0
	 * 创建日期:2019-10-29
	 * @param newCreationtime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime0 (nc.vo.pub.lang.UFDateTime newCreationtime0 ) {
	 	this.creationtime0 = newCreationtime0;
	} 	 
	
	/**
	 * 属性 modifier0的Getter方法.属性名：修改人0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getModifier0 () {
		return modifier0;
	}   
	/**
	 * 属性modifier0的Setter方法.属性名：修改人0
	 * 创建日期:2019-10-29
	 * @param newModifier0 java.lang.String
	 */
	public void setModifier0 (java.lang.String newModifier0 ) {
	 	this.modifier0 = newModifier0;
	} 	 
	
	/**
	 * 属性 modifiedtime0的Getter方法.属性名：修改时间0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime0 () {
		return modifiedtime0;
	}   
	/**
	 * 属性modifiedtime0的Setter方法.属性名：修改时间0
	 * 创建日期:2019-10-29
	 * @param newModifiedtime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime0 (nc.vo.pub.lang.UFDateTime newModifiedtime0 ) {
	 	this.modifiedtime0 = newModifiedtime0;
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
	 * 属性 approvestatus0的Getter方法.属性名：审批状态0
	 *  创建日期:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getApprovestatus0 () {
		return approvestatus0;
	}   
	/**
	 * 属性approvestatus0的Setter方法.属性名：审批状态0
	 * 创建日期:2019-10-29
	 * @param newApprovestatus0 java.lang.Integer
	 */
	public void setApprovestatus0 (java.lang.Integer newApprovestatus0 ) {
	 	this.approvestatus0 = newApprovestatus0;
	} 	 
	
	/**
	 * 属性 approvenote0的Getter方法.属性名：审批批语0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getApprovenote0 () {
		return approvenote0;
	}   
	/**
	 * 属性approvenote0的Setter方法.属性名：审批批语0
	 * 创建日期:2019-10-29
	 * @param newApprovenote0 java.lang.String
	 */
	public void setApprovenote0 (java.lang.String newApprovenote0 ) {
	 	this.approvenote0 = newApprovenote0;
	} 	 
	
	/**
	 * 属性 approvedate0的Getter方法.属性名：审批时间0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getApprovedate0 () {
		return approvedate0;
	}   
	/**
	 * 属性approvedate0的Setter方法.属性名：审批时间0
	 * 创建日期:2019-10-29
	 * @param newApprovedate0 nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate0 (nc.vo.pub.lang.UFDateTime newApprovedate0 ) {
	 	this.approvedate0 = newApprovedate0;
	} 	 
	
	/**
	 * 属性 vnote0的Getter方法.属性名：备注0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVnote0 () {
		return vnote0;
	}   
	/**
	 * 属性vnote0的Setter方法.属性名：备注0
	 * 创建日期:2019-10-29
	 * @param newVnote0 java.lang.String
	 */
	public void setVnote0 (java.lang.String newVnote0 ) {
	 	this.vnote0 = newVnote0;
	} 	 
	
	/**
	 * 属性 customers0的Getter方法.属性名：客户0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getCustomers0 () {
		return customers0;
	}   
	/**
	 * 属性customers0的Setter方法.属性名：客户0
	 * 创建日期:2019-10-29
	 * @param newCustomers0 java.lang.String
	 */
	public void setCustomers0 (java.lang.String newCustomers0 ) {
	 	this.customers0 = newCustomers0;
	} 	 
	
	/**
	 * 属性 srcbilltype0的Getter方法.属性名：来源单据类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbilltype0 () {
		return srcbilltype0;
	}   
	/**
	 * 属性srcbilltype0的Setter方法.属性名：来源单据类型0
	 * 创建日期:2019-10-29
	 * @param newSrcbilltype0 java.lang.String
	 */
	public void setSrcbilltype0 (java.lang.String newSrcbilltype0 ) {
	 	this.srcbilltype0 = newSrcbilltype0;
	} 	 
	
	/**
	 * 属性 srcbillid0的Getter方法.属性名：来源单据id0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbillid0 () {
		return srcbillid0;
	}   
	/**
	 * 属性srcbillid0的Setter方法.属性名：来源单据id0
	 * 创建日期:2019-10-29
	 * @param newSrcbillid0 java.lang.String
	 */
	public void setSrcbillid0 (java.lang.String newSrcbillid0 ) {
	 	this.srcbillid0 = newSrcbillid0;
	} 	 
	
	/**
	 * 属性 srccode0的Getter方法.属性名：来源单据号0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrccode0 () {
		return srccode0;
	}   
	/**
	 * 属性srccode0的Setter方法.属性名：来源单据号0
	 * 创建日期:2019-10-29
	 * @param newSrccode0 java.lang.String
	 */
	public void setSrccode0 (java.lang.String newSrccode0 ) {
	 	this.srccode0 = newSrccode0;
	} 	 
	
	/**
	 * 属性 srcid0的Getter方法.属性名：来源单据主表id0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcid0 () {
		return srcid0;
	}   
	/**
	 * 属性srcid0的Setter方法.属性名：来源单据主表id0
	 * 创建日期:2019-10-29
	 * @param newSrcid0 java.lang.String
	 */
	public void setSrcid0 (java.lang.String newSrcid0 ) {
	 	this.srcid0 = newSrcid0;
	} 	 
	
	/**
	 * 属性 srcbid0的Getter方法.属性名：来源单据子表id0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbid0 () {
		return srcbid0;
	}   
	/**
	 * 属性srcbid0的Setter方法.属性名：来源单据子表id0
	 * 创建日期:2019-10-29
	 * @param newSrcbid0 java.lang.String
	 */
	public void setSrcbid0 (java.lang.String newSrcbid0 ) {
	 	this.srcbid0 = newSrcbid0;
	} 	 
	
	/**
	 * 属性 srcrowno0的Getter方法.属性名：来源单据行号0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcrowno0 () {
		return srcrowno0;
	}   
	/**
	 * 属性srcrowno0的Setter方法.属性名：来源单据行号0
	 * 创建日期:2019-10-29
	 * @param newSrcrowno0 java.lang.String
	 */
	public void setSrcrowno0 (java.lang.String newSrcrowno0 ) {
	 	this.srcrowno0 = newSrcrowno0;
	} 	 
	
	/**
	 * 属性 srctrantype0的Getter方法.属性名：来源交易类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrctrantype0 () {
		return srctrantype0;
	}   
	/**
	 * 属性srctrantype0的Setter方法.属性名：来源交易类型0
	 * 创建日期:2019-10-29
	 * @param newSrctrantype0 java.lang.String
	 */
	public void setSrctrantype0 (java.lang.String newSrctrantype0 ) {
	 	this.srctrantype0 = newSrctrantype0;
	} 	 
	
	/**
	 * 属性 vfirsttype0的Getter方法.属性名：源头单据类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirsttype0 () {
		return vfirsttype0;
	}   
	/**
	 * 属性vfirsttype0的Setter方法.属性名：源头单据类型0
	 * 创建日期:2019-10-29
	 * @param newVfirsttype0 java.lang.String
	 */
	public void setVfirsttype0 (java.lang.String newVfirsttype0 ) {
	 	this.vfirsttype0 = newVfirsttype0;
	} 	 
	
	/**
	 * 属性 vfirstcode0的Getter方法.属性名：源头单据号0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstcode0 () {
		return vfirstcode0;
	}   
	/**
	 * 属性vfirstcode0的Setter方法.属性名：源头单据号0
	 * 创建日期:2019-10-29
	 * @param newVfirstcode0 java.lang.String
	 */
	public void setVfirstcode0 (java.lang.String newVfirstcode0 ) {
	 	this.vfirstcode0 = newVfirstcode0;
	} 	 
	
	/**
	 * 属性 vfirstid0的Getter方法.属性名：源头单据主表id0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstid0 () {
		return vfirstid0;
	}   
	/**
	 * 属性vfirstid0的Setter方法.属性名：源头单据主表id0
	 * 创建日期:2019-10-29
	 * @param newVfirstid0 java.lang.String
	 */
	public void setVfirstid0 (java.lang.String newVfirstid0 ) {
	 	this.vfirstid0 = newVfirstid0;
	} 	 
	
	/**
	 * 属性 vfirstbid0的Getter方法.属性名：源头单据子表id0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstbid0 () {
		return vfirstbid0;
	}   
	/**
	 * 属性vfirstbid0的Setter方法.属性名：源头单据子表id0
	 * 创建日期:2019-10-29
	 * @param newVfirstbid0 java.lang.String
	 */
	public void setVfirstbid0 (java.lang.String newVfirstbid0 ) {
	 	this.vfirstbid0 = newVfirstbid0;
	} 	 
	
	/**
	 * 属性 vfirstrowno0的Getter方法.属性名：源头单据行号0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstrowno0 () {
		return vfirstrowno0;
	}   
	/**
	 * 属性vfirstrowno0的Setter方法.属性名：源头单据行号0
	 * 创建日期:2019-10-29
	 * @param newVfirstrowno0 java.lang.String
	 */
	public void setVfirstrowno0 (java.lang.String newVfirstrowno0 ) {
	 	this.vfirstrowno0 = newVfirstrowno0;
	} 	 
	
	/**
	 * 属性 vfirsttrantype0的Getter方法.属性名：源头交易类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirsttrantype0 () {
		return vfirsttrantype0;
	}   
	/**
	 * 属性vfirsttrantype0的Setter方法.属性名：源头交易类型0
	 * 创建日期:2019-10-29
	 * @param newVfirsttrantype0 java.lang.String
	 */
	public void setVfirsttrantype0 (java.lang.String newVfirsttrantype0 ) {
	 	this.vfirsttrantype0 = newVfirsttrantype0;
	} 	 
	
	/**
	 * 属性 def0的Getter方法.属性名：自定义项0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef0 () {
		return def0;
	}   
	/**
	 * 属性def0的Setter方法.属性名：自定义项0
	 * 创建日期:2019-10-29
	 * @param newDef0 java.lang.String
	 */
	public void setDef0 (java.lang.String newDef0 ) {
	 	this.def0 = newDef0;
	} 	 
	
	/**
	 * 属性 def1的Getter方法.属性名：自定义项1
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * 属性def1的Setter方法.属性名：自定义项1
	 * 创建日期:2019-10-29
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * 属性 def2的Getter方法.属性名：自定义项2
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * 属性def2的Setter方法.属性名：自定义项2
	 * 创建日期:2019-10-29
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * 属性 def3的Getter方法.属性名：自定义项3
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * 属性def3的Setter方法.属性名：自定义项3
	 * 创建日期:2019-10-29
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * 属性 def4的Getter方法.属性名：自定义项4
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * 属性def4的Setter方法.属性名：自定义项4
	 * 创建日期:2019-10-29
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * 属性 def5的Getter方法.属性名：自定义项5
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * 属性def5的Setter方法.属性名：自定义项5
	 * 创建日期:2019-10-29
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * 属性 def6的Getter方法.属性名：自定义项6
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * 属性def6的Setter方法.属性名：自定义项6
	 * 创建日期:2019-10-29
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * 属性 def7的Getter方法.属性名：自定义项7
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * 属性def7的Setter方法.属性名：自定义项7
	 * 创建日期:2019-10-29
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * 属性 def8的Getter方法.属性名：自定义项8
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * 属性def8的Setter方法.属性名：自定义项8
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
	 * 属性 def21的Getter方法.属性名：自定义项21
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef21 () {
		return def21;
	}   
	/**
	 * 属性def21的Setter方法.属性名：自定义项21
	 * 创建日期:2019-10-29
	 * @param newDef21 java.lang.String
	 */
	public void setDef21 (java.lang.String newDef21 ) {
	 	this.def21 = newDef21;
	} 	 
	
	/**
	 * 属性 def22的Getter方法.属性名：自定义项22
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef22 () {
		return def22;
	}   
	/**
	 * 属性def22的Setter方法.属性名：自定义项22
	 * 创建日期:2019-10-29
	 * @param newDef22 java.lang.String
	 */
	public void setDef22 (java.lang.String newDef22 ) {
	 	this.def22 = newDef22;
	} 	 
	
	/**
	 * 属性 def23的Getter方法.属性名：自定义项23
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef23 () {
		return def23;
	}   
	/**
	 * 属性def23的Setter方法.属性名：自定义项23
	 * 创建日期:2019-10-29
	 * @param newDef23 java.lang.String
	 */
	public void setDef23 (java.lang.String newDef23 ) {
	 	this.def23 = newDef23;
	} 	 
	
	/**
	 * 属性 def24的Getter方法.属性名：自定义项24
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef24 () {
		return def24;
	}   
	/**
	 * 属性def24的Setter方法.属性名：自定义项24
	 * 创建日期:2019-10-29
	 * @param newDef24 java.lang.String
	 */
	public void setDef24 (java.lang.String newDef24 ) {
	 	this.def24 = newDef24;
	} 	 
	
	/**
	 * 属性 def25的Getter方法.属性名：自定义项25
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef25 () {
		return def25;
	}   
	/**
	 * 属性def25的Setter方法.属性名：自定义项25
	 * 创建日期:2019-10-29
	 * @param newDef25 java.lang.String
	 */
	public void setDef25 (java.lang.String newDef25 ) {
	 	this.def25 = newDef25;
	} 	 
	
	/**
	 * 属性 def26的Getter方法.属性名：自定义项26
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef26 () {
		return def26;
	}   
	/**
	 * 属性def26的Setter方法.属性名：自定义项26
	 * 创建日期:2019-10-29
	 * @param newDef26 java.lang.String
	 */
	public void setDef26 (java.lang.String newDef26 ) {
	 	this.def26 = newDef26;
	} 	 
	
	/**
	 * 属性 def27的Getter方法.属性名：自定义项27
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef27 () {
		return def27;
	}   
	/**
	 * 属性def27的Setter方法.属性名：自定义项27
	 * 创建日期:2019-10-29
	 * @param newDef27 java.lang.String
	 */
	public void setDef27 (java.lang.String newDef27 ) {
	 	this.def27 = newDef27;
	} 	 
	
	/**
	 * 属性 def28的Getter方法.属性名：自定义项28
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef28 () {
		return def28;
	}   
	/**
	 * 属性def28的Setter方法.属性名：自定义项28
	 * 创建日期:2019-10-29
	 * @param newDef28 java.lang.String
	 */
	public void setDef28 (java.lang.String newDef28 ) {
	 	this.def28 = newDef28;
	} 	 
	
	/**
	 * 属性 def29的Getter方法.属性名：自定义项29
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef29 () {
		return def29;
	}   
	/**
	 * 属性def29的Setter方法.属性名：自定义项29
	 * 创建日期:2019-10-29
	 * @param newDef29 java.lang.String
	 */
	public void setDef29 (java.lang.String newDef29 ) {
	 	this.def29 = newDef29;
	} 	 
	
	/**
	 * 属性 def30的Getter方法.属性名：自定义项30
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef30 () {
		return def30;
	}   
	/**
	 * 属性def30的Setter方法.属性名：自定义项30
	 * 创建日期:2019-10-29
	 * @param newDef30 java.lang.String
	 */
	public void setDef30 (java.lang.String newDef30 ) {
	 	this.def30 = newDef30;
	} 	 
	
	/**
	 * 属性 dept0的Getter方法.属性名：单据编制人部门0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDept0 () {
		return dept0;
	}   
	/**
	 * 属性dept0的Setter方法.属性名：单据编制人部门0
	 * 创建日期:2019-10-29
	 * @param newDept0 java.lang.String
	 */
	public void setDept0 (java.lang.String newDept0 ) {
	 	this.dept0 = newDept0;
	} 	 
	
	/**
	 * 属性 receptdate0的Getter方法.属性名：接受日期0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getReceptdate0 () {
		return receptdate0;
	}   
	/**
	 * 属性receptdate0的Setter方法.属性名：接受日期0
	 * 创建日期:2019-10-29
	 * @param newReceptdate0 nc.vo.pub.lang.UFDateTime
	 */
	public void setReceptdate0 (nc.vo.pub.lang.UFDateTime newReceptdate0 ) {
	 	this.receptdate0 = newReceptdate0;
	} 	 
	
	/**
	 * 属性 projectno0的Getter方法.属性名：工号编码0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProjectno0 () {
		return projectno0;
	}   
	/**
	 * 属性projectno0的Setter方法.属性名：工号编码0
	 * 创建日期:2019-10-29
	 * @param newProjectno0 java.lang.String
	 */
	public void setProjectno0 (java.lang.String newProjectno0 ) {
	 	this.projectno0 = newProjectno0;
	} 	 
	
	/**
	 * 属性 projectname0的Getter方法.属性名：工号名称0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProjectname0 () {
		return projectname0;
	}   
	/**
	 * 属性projectname0的Setter方法.属性名：工号名称0
	 * 创建日期:2019-10-29
	 * @param newProjectname0 java.lang.String
	 */
	public void setProjectname0 (java.lang.String newProjectname0 ) {
	 	this.projectname0 = newProjectname0;
	} 	 
	
	/**
	 * 属性 productname0的Getter方法.属性名：产品名称0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProductname0 () {
		return productname0;
	}   
	/**
	 * 属性productname0的Setter方法.属性名：产品名称0
	 * 创建日期:2019-10-29
	 * @param newProductname0 java.lang.String
	 */
	public void setProductname0 (java.lang.String newProductname0 ) {
	 	this.productname0 = newProductname0;
	} 	 
	
	/**
	 * 属性 shipowner0的Getter方法.属性名：船东0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipowner0 () {
		return shipowner0;
	}   
	/**
	 * 属性shipowner0的Setter方法.属性名：船东0
	 * 创建日期:2019-10-29
	 * @param newShipowner0 java.lang.String
	 */
	public void setShipowner0 (java.lang.String newShipowner0 ) {
	 	this.shipowner0 = newShipowner0;
	} 	 
	
	/**
	 * 属性 shipno0的Getter方法.属性名：船号0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipno0 () {
		return shipno0;
	}   
	/**
	 * 属性shipno0的Setter方法.属性名：船号0
	 * 创建日期:2019-10-29
	 * @param newShipno0 java.lang.String
	 */
	public void setShipno0 (java.lang.String newShipno0 ) {
	 	this.shipno0 = newShipno0;
	} 	 
	
	/**
	 * 属性 shipname0的Getter方法.属性名：船名0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipname0 () {
		return shipname0;
	}   
	/**
	 * 属性shipname0的Setter方法.属性名：船名0
	 * 创建日期:2019-10-29
	 * @param newShipname0 java.lang.String
	 */
	public void setShipname0 (java.lang.String newShipname0 ) {
	 	this.shipname0 = newShipname0;
	} 	 
	
	/**
	 * 属性 shiptype0的Getter方法.属性名：船型0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShiptype0 () {
		return shiptype0;
	}   
	/**
	 * 属性shiptype0的Setter方法.属性名：船型0
	 * 创建日期:2019-10-29
	 * @param newShiptype0 java.lang.String
	 */
	public void setShiptype0 (java.lang.String newShiptype0 ) {
	 	this.shiptype0 = newShiptype0;
	} 	 
	
	/**
	 * 属性 servicelocation0的Getter方法.属性名：服务地点0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getServicelocation0 () {
		return servicelocation0;
	}   
	/**
	 * 属性servicelocation0的Setter方法.属性名：服务地点0
	 * 创建日期:2019-10-29
	 * @param newServicelocation0 java.lang.String
	 */
	public void setServicelocation0 (java.lang.String newServicelocation0 ) {
	 	this.servicelocation0 = newServicelocation0;
	} 	 
	
	/**
	 * 属性 grindersdept0的Getter方法.属性名：承制部门0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getGrindersdept0 () {
		return grindersdept0;
	}   
	/**
	 * 属性grindersdept0的Setter方法.属性名：承制部门0
	 * 创建日期:2019-10-29
	 * @param newGrindersdept0 java.lang.String
	 */
	public void setGrindersdept0 (java.lang.String newGrindersdept0 ) {
	 	this.grindersdept0 = newGrindersdept0;
	} 	 
	
	/**
	 * 属性 deliverytime0的Getter方法.属性名：产品交付船厂时间0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getDeliverytime0 () {
		return deliverytime0;
	}   
	/**
	 * 属性deliverytime0的Setter方法.属性名：产品交付船厂时间0
	 * 创建日期:2019-10-29
	 * @param newDeliverytime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setDeliverytime0 (nc.vo.pub.lang.UFDateTime newDeliverytime0 ) {
	 	this.deliverytime0 = newDeliverytime0;
	} 	 
	
	/**
	 * 属性 arrivaltime0的Getter方法.属性名：要求抵达时间0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getArrivaltime0 () {
		return arrivaltime0;
	}   
	/**
	 * 属性arrivaltime0的Setter方法.属性名：要求抵达时间0
	 * 创建日期:2019-10-29
	 * @param newArrivaltime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setArrivaltime0 (nc.vo.pub.lang.UFDateTime newArrivaltime0 ) {
	 	this.arrivaltime0 = newArrivaltime0;
	} 	 
	
	/**
	 * 属性 serviceperiod0的Getter方法.属性名：拟定服务周期0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getServiceperiod0 () {
		return serviceperiod0;
	}   
	/**
	 * 属性serviceperiod0的Setter方法.属性名：拟定服务周期0
	 * 创建日期:2019-10-29
	 * @param newServiceperiod0 java.lang.String
	 */
	public void setServiceperiod0 (java.lang.String newServiceperiod0 ) {
	 	this.serviceperiod0 = newServiceperiod0;
	} 	 
	
	/**
	 * 属性 aftersalescust0的Getter方法.属性名：售后服务客户0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getAftersalescust0 () {
		return aftersalescust0;
	}   
	/**
	 * 属性aftersalescust0的Setter方法.属性名：售后服务客户0
	 * 创建日期:2019-10-29
	 * @param newAftersalescust0 java.lang.String
	 */
	public void setAftersalescust0 (java.lang.String newAftersalescust0 ) {
	 	this.aftersalescust0 = newAftersalescust0;
	} 	 
	
	/**
	 * 属性 linkman0的Getter方法.属性名：联系人0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getLinkman0 () {
		return linkman0;
	}   
	/**
	 * 属性linkman0的Setter方法.属性名：联系人0
	 * 创建日期:2019-10-29
	 * @param newLinkman0 java.lang.String
	 */
	public void setLinkman0 (java.lang.String newLinkman0 ) {
	 	this.linkman0 = newLinkman0;
	} 	 
	
	/**
	 * 属性 phonenumber0的Getter方法.属性名：联系电话0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPhonenumber0 () {
		return phonenumber0;
	}   
	/**
	 * 属性phonenumber0的Setter方法.属性名：联系电话0
	 * 创建日期:2019-10-29
	 * @param newPhonenumber0 java.lang.String
	 */
	public void setPhonenumber0 (java.lang.String newPhonenumber0 ) {
	 	this.phonenumber0 = newPhonenumber0;
	} 	 
	
	/**
	 * 属性 guaranteeperiod0的Getter方法.属性名：质保期0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getGuaranteeperiod0 () {
		return guaranteeperiod0;
	}   
	/**
	 * 属性guaranteeperiod0的Setter方法.属性名：质保期0
	 * 创建日期:2019-10-29
	 * @param newGuaranteeperiod0 java.lang.String
	 */
	public void setGuaranteeperiod0 (java.lang.String newGuaranteeperiod0 ) {
	 	this.guaranteeperiod0 = newGuaranteeperiod0;
	} 	 
	
	/**
	 * 属性 servicetype0的Getter方法.属性名：服务类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getServicetype0 () {
		return servicetype0;
	}   
	/**
	 * 属性servicetype0的Setter方法.属性名：服务类型0
	 * 创建日期:2019-10-29
	 * @param newServicetype0 java.lang.Integer
	 */
	public void setServicetype0 (java.lang.Integer newServicetype0 ) {
	 	this.servicetype0 = newServicetype0;
	} 	 
	
	/**
	 * 属性 businesstype0的Getter方法.属性名：具体业务类型0
	 *  创建日期:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getBusinesstype0 () {
		return businesstype0;
	}   
	/**
	 * 属性businesstype0的Setter方法.属性名：具体业务类型0
	 * 创建日期:2019-10-29
	 * @param newBusinesstype0 java.lang.Integer
	 */
	public void setBusinesstype0 (java.lang.Integer newBusinesstype0 ) {
	 	this.businesstype0 = newBusinesstype0;
	} 	 
	
	/**
	 * 属性 productstatus0的Getter方法.属性名：产品状态0
	 *  创建日期:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getProductstatus0 () {
		return productstatus0;
	}   
	/**
	 * 属性productstatus0的Setter方法.属性名：产品状态0
	 * 创建日期:2019-10-29
	 * @param newProductstatus0 java.lang.Integer
	 */
	public void setProductstatus0 (java.lang.Integer newProductstatus0 ) {
	 	this.productstatus0 = newProductstatus0;
	} 	 
	
	/**
	 * 属性 processingmode0的Getter方法.属性名：后续处理方式0
	 *  创建日期:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getProcessingmode0 () {
		return processingmode0;
	}   
	/**
	 * 属性processingmode0的Setter方法.属性名：后续处理方式0
	 * 创建日期:2019-10-29
	 * @param newProcessingmode0 java.lang.Integer
	 */
	public void setProcessingmode0 (java.lang.Integer newProcessingmode0 ) {
	 	this.processingmode0 = newProcessingmode0;
	} 	 
	
	/**
	 * 属性 isgeneratedown0的Getter方法.属性名：是否生成下游单据0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIsgeneratedown0 () {
		return isgeneratedown0;
	}   
	/**
	 * 属性isgeneratedown0的Setter方法.属性名：是否生成下游单据0
	 * 创建日期:2019-10-29
	 * @param newIsgeneratedown0 nc.vo.pub.lang.UFBoolean
	 */
	public void setIsgeneratedown0 (nc.vo.pub.lang.UFBoolean newIsgeneratedown0 ) {
	 	this.isgeneratedown0 = newIsgeneratedown0;
	} 	 
	
	/**
	 * 属性 shipyard0的Getter方法.属性名：船厂0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipyard0 () {
		return shipyard0;
	}   
	/**
	 * 属性shipyard0的Setter方法.属性名：船厂0
	 * 创建日期:2019-10-29
	 * @param newShipyard0 java.lang.String
	 */
	public void setShipyard0 (java.lang.String newShipyard0 ) {
	 	this.shipyard0 = newShipyard0;
	} 	 
	
	/**
	 * 属性 sendtheunit0的Getter方法.属性名：发送单位0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSendtheunit0 () {
		return sendtheunit0;
	}   
	/**
	 * 属性sendtheunit0的Setter方法.属性名：发送单位0
	 * 创建日期:2019-10-29
	 * @param newSendtheunit0 java.lang.String
	 */
	public void setSendtheunit0 (java.lang.String newSendtheunit0 ) {
	 	this.sendtheunit0 = newSendtheunit0;
	} 	 
	
	/**
	 * 属性 productattribute0的Getter方法.属性名：产品属性0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProductattribute0 () {
		return productattribute0;
	}   
	/**
	 * 属性productattribute0的Setter方法.属性名：产品属性0
	 * 创建日期:2019-10-29
	 * @param newProductattribute0 java.lang.String
	 */
	public void setProductattribute0 (java.lang.String newProductattribute0 ) {
	 	this.productattribute0 = newProductattribute0;
	} 	 
	
	/**
	 * 属性 shipclub0的Getter方法.属性名：船级社0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipclub0 () {
		return shipclub0;
	}   
	/**
	 * 属性shipclub0的Setter方法.属性名：船级社0
	 * 创建日期:2019-10-29
	 * @param newShipclub0 java.lang.String
	 */
	public void setShipclub0 (java.lang.String newShipclub0 ) {
	 	this.shipclub0 = newShipclub0;
	} 	 
	
	/**
	 * 属性 area0的Getter方法.属性名：区域0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getArea0 () {
		return area0;
	}   
	/**
	 * 属性area0的Setter方法.属性名：区域0
	 * 创建日期:2019-10-29
	 * @param newArea0 java.lang.String
	 */
	public void setArea0 (java.lang.String newArea0 ) {
	 	this.area0 = newArea0;
	} 	 
	
	/**
	 * 属性 dutypersion0的Getter方法.属性名：责任人0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDutypersion0 () {
		return dutypersion0;
	}   
	/**
	 * 属性dutypersion0的Setter方法.属性名：责任人0
	 * 创建日期:2019-10-29
	 * @param newDutypersion0 java.lang.String
	 */
	public void setDutypersion0 (java.lang.String newDutypersion0 ) {
	 	this.dutypersion0 = newDutypersion0;
	} 	 
	
	/**
	 * 属性 deliverydate0的Getter方法.属性名：交船时间0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDeliverydate0 () {
		return deliverydate0;
	}   
	/**
	 * 属性deliverydate0的Setter方法.属性名：交船时间0
	 * 创建日期:2019-10-29
	 * @param newDeliverydate0 nc.vo.pub.lang.UFDate
	 */
	public void setDeliverydate0 (nc.vo.pub.lang.UFDate newDeliverydate0 ) {
	 	this.deliverydate0 = newDeliverydate0;
	} 	 
	
	/**
	 * 属性 deliproduct0的Getter方法.属性名：交付产品0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDeliproduct0 () {
		return deliproduct0;
	}   
	/**
	 * 属性deliproduct0的Setter方法.属性名：交付产品0
	 * 创建日期:2019-10-29
	 * @param newDeliproduct0 java.lang.String
	 */
	public void setDeliproduct0 (java.lang.String newDeliproduct0 ) {
	 	this.deliproduct0 = newDeliproduct0;
	} 	 
	
	/**
	 * 属性 projectdeco0的Getter方法.属性名：项目分组0
	 *  创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProjectdeco0 () {
		return projectdeco0;
	}   
	/**
	 * 属性projectdeco0的Setter方法.属性名：项目分组0
	 * 创建日期:2019-10-29
	 * @param newProjectdeco0 java.lang.String
	 */
	public void setProjectdeco0 (java.lang.String newProjectdeco0 ) {
	 	this.projectdeco0 = newProjectdeco0;
	} 	 
	
	/**
	 * 属性 istechproposal0的Getter方法.属性名：是否生成技术方案0
	 *  创建日期:2019-10-29
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIstechproposal0 () {
		return istechproposal0;
	}   
	/**
	 * 属性istechproposal0的Setter方法.属性名：是否生成技术方案0
	 * 创建日期:2019-10-29
	 * @param newIstechproposal0 nc.vo.pub.lang.UFBoolean
	 */
	public void setIstechproposal0 (nc.vo.pub.lang.UFBoolean newIstechproposal0 ) {
	 	this.istechproposal0 = newIstechproposal0;
	} 	 
	
	/**
	 * 属性 id_entity1的Getter方法.属性名：id_Entity1
	 *  创建日期:2019-10-29
	 * @return nc.vo.test.test01.Entity1[]
	 */
	public nc.vo.test.test01.Entity1[] getId_entity1 () {
		return id_entity1;
	}   
	/**
	 * 属性id_entity1的Setter方法.属性名：id_Entity1
	 * 创建日期:2019-10-29
	 * @param newId_entity1 nc.vo.test.test01.Entity1[]
	 */
	public void setId_entity1 (nc.vo.test.test01.Entity1[] newId_entity1 ) {
	 	this.id_entity1 = newId_entity1;
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
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2019-10-29
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_servicequest0";
	}
    
	/**
	 * <p>返回表名称
	 * <p>
	 * 创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "test_service";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2019-10-29
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "test_service";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2019-10-29
	  */
     public Service() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.test.test01.Service" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("test.service");
		
   	}
     
}