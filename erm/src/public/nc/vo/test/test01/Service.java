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
	 * ���� pk_servicequest0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_servicequest0 () {
		return pk_servicequest0;
	}   
	/**
	 * ����pk_servicequest0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newPk_servicequest0 java.lang.String
	 */
	public void setPk_servicequest0 (java.lang.String newPk_servicequest0 ) {
	 	this.pk_servicequest0 = newPk_servicequest0;
	} 	 
	
	/**
	 * ���� pk_group0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group0 () {
		return pk_group0;
	}   
	/**
	 * ����pk_group0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newPk_group0 java.lang.String
	 */
	public void setPk_group0 (java.lang.String newPk_group0 ) {
	 	this.pk_group0 = newPk_group0;
	} 	 
	
	/**
	 * ���� pk_org0��Getter����.����������֯0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org0 () {
		return pk_org0;
	}   
	/**
	 * ����pk_org0��Setter����.����������֯0
	 * ��������:2019-10-29
	 * @param newPk_org0 java.lang.String
	 */
	public void setPk_org0 (java.lang.String newPk_org0 ) {
	 	this.pk_org0 = newPk_org0;
	} 	 
	
	/**
	 * ���� pk_org_v0��Getter����.����������֯�汾0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v0 () {
		return pk_org_v0;
	}   
	/**
	 * ����pk_org_v0��Setter����.����������֯�汾0
	 * ��������:2019-10-29
	 * @param newPk_org_v0 java.lang.String
	 */
	public void setPk_org_v0 (java.lang.String newPk_org_v0 ) {
	 	this.pk_org_v0 = newPk_org_v0;
	} 	 
	
	/**
	 * ���� vbillcode0��Getter����.�����������ݺ�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVbillcode0 () {
		return vbillcode0;
	}   
	/**
	 * ����vbillcode0��Setter����.�����������ݺ�0
	 * ��������:2019-10-29
	 * @param newVbillcode0 java.lang.String
	 */
	public void setVbillcode0 (java.lang.String newVbillcode0 ) {
	 	this.vbillcode0 = newVbillcode0;
	} 	 
	
	/**
	 * ���� dbilldate0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDbilldate0 () {
		return dbilldate0;
	}   
	/**
	 * ����dbilldate0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newDbilldate0 nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate0 (nc.vo.pub.lang.UFDate newDbilldate0 ) {
	 	this.dbilldate0 = newDbilldate0;
	} 	 
	
	/**
	 * ���� fstatusflag0��Getter����.������������״̬0
	 *  ��������:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getFstatusflag0 () {
		return fstatusflag0;
	}   
	/**
	 * ����fstatusflag0��Setter����.������������״̬0
	 * ��������:2019-10-29
	 * @param newFstatusflag0 java.lang.Integer
	 */
	public void setFstatusflag0 (java.lang.Integer newFstatusflag0 ) {
	 	this.fstatusflag0 = newFstatusflag0;
	} 	 
	
	/**
	 * ���� pk_busitype0��Getter����.��������ҵ������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_busitype0 () {
		return pk_busitype0;
	}   
	/**
	 * ����pk_busitype0��Setter����.��������ҵ������0
	 * ��������:2019-10-29
	 * @param newPk_busitype0 java.lang.String
	 */
	public void setPk_busitype0 (java.lang.String newPk_busitype0 ) {
	 	this.pk_busitype0 = newPk_busitype0;
	} 	 
	
	/**
	 * ���� pk_billtypeid0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtypeid0 () {
		return pk_billtypeid0;
	}   
	/**
	 * ����pk_billtypeid0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newPk_billtypeid0 java.lang.String
	 */
	public void setPk_billtypeid0 (java.lang.String newPk_billtypeid0 ) {
	 	this.pk_billtypeid0 = newPk_billtypeid0;
	} 	 
	
	/**
	 * ���� billtype0��Getter����.���������������ͱ���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getBilltype0 () {
		return billtype0;
	}   
	/**
	 * ����billtype0��Setter����.���������������ͱ���0
	 * ��������:2019-10-29
	 * @param newBilltype0 java.lang.String
	 */
	public void setBilltype0 (java.lang.String newBilltype0 ) {
	 	this.billtype0 = newBilltype0;
	} 	 
	
	/**
	 * ���� transtypecode0��Getter����.���������������ͱ���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getTranstypecode0 () {
		return transtypecode0;
	}   
	/**
	 * ����transtypecode0��Setter����.���������������ͱ���0
	 * ��������:2019-10-29
	 * @param newTranstypecode0 java.lang.String
	 */
	public void setTranstypecode0 (java.lang.String newTranstypecode0 ) {
	 	this.transtypecode0 = newTranstypecode0;
	} 	 
	
	/**
	 * ���� pk_transtype0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPk_transtype0 () {
		return pk_transtype0;
	}   
	/**
	 * ����pk_transtype0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newPk_transtype0 java.lang.String
	 */
	public void setPk_transtype0 (java.lang.String newPk_transtype0 ) {
	 	this.pk_transtype0 = newPk_transtype0;
	} 	 
	
	/**
	 * ���� billmaker0��Getter����.���������Ƶ���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getBillmaker0 () {
		return billmaker0;
	}   
	/**
	 * ����billmaker0��Setter����.���������Ƶ���0
	 * ��������:2019-10-29
	 * @param newBillmaker0 java.lang.String
	 */
	public void setBillmaker0 (java.lang.String newBillmaker0 ) {
	 	this.billmaker0 = newBillmaker0;
	} 	 
	
	/**
	 * ���� maketime0��Getter����.���������Ƶ�ʱ��0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getMaketime0 () {
		return maketime0;
	}   
	/**
	 * ����maketime0��Setter����.���������Ƶ�ʱ��0
	 * ��������:2019-10-29
	 * @param newMaketime0 nc.vo.pub.lang.UFDate
	 */
	public void setMaketime0 (nc.vo.pub.lang.UFDate newMaketime0 ) {
	 	this.maketime0 = newMaketime0;
	} 	 
	
	/**
	 * ���� creator0��Getter����.��������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getCreator0 () {
		return creator0;
	}   
	/**
	 * ����creator0��Setter����.��������������0
	 * ��������:2019-10-29
	 * @param newCreator0 java.lang.String
	 */
	public void setCreator0 (java.lang.String newCreator0 ) {
	 	this.creator0 = newCreator0;
	} 	 
	
	/**
	 * ���� creationtime0��Getter����.������������ʱ��0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime0 () {
		return creationtime0;
	}   
	/**
	 * ����creationtime0��Setter����.������������ʱ��0
	 * ��������:2019-10-29
	 * @param newCreationtime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime0 (nc.vo.pub.lang.UFDateTime newCreationtime0 ) {
	 	this.creationtime0 = newCreationtime0;
	} 	 
	
	/**
	 * ���� modifier0��Getter����.���������޸���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getModifier0 () {
		return modifier0;
	}   
	/**
	 * ����modifier0��Setter����.���������޸���0
	 * ��������:2019-10-29
	 * @param newModifier0 java.lang.String
	 */
	public void setModifier0 (java.lang.String newModifier0 ) {
	 	this.modifier0 = newModifier0;
	} 	 
	
	/**
	 * ���� modifiedtime0��Getter����.���������޸�ʱ��0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime0 () {
		return modifiedtime0;
	}   
	/**
	 * ����modifiedtime0��Setter����.���������޸�ʱ��0
	 * ��������:2019-10-29
	 * @param newModifiedtime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime0 (nc.vo.pub.lang.UFDateTime newModifiedtime0 ) {
	 	this.modifiedtime0 = newModifiedtime0;
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
	 * ���� approvestatus0��Getter����.������������״̬0
	 *  ��������:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getApprovestatus0 () {
		return approvestatus0;
	}   
	/**
	 * ����approvestatus0��Setter����.������������״̬0
	 * ��������:2019-10-29
	 * @param newApprovestatus0 java.lang.Integer
	 */
	public void setApprovestatus0 (java.lang.Integer newApprovestatus0 ) {
	 	this.approvestatus0 = newApprovestatus0;
	} 	 
	
	/**
	 * ���� approvenote0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getApprovenote0 () {
		return approvenote0;
	}   
	/**
	 * ����approvenote0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newApprovenote0 java.lang.String
	 */
	public void setApprovenote0 (java.lang.String newApprovenote0 ) {
	 	this.approvenote0 = newApprovenote0;
	} 	 
	
	/**
	 * ���� approvedate0��Getter����.������������ʱ��0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getApprovedate0 () {
		return approvedate0;
	}   
	/**
	 * ����approvedate0��Setter����.������������ʱ��0
	 * ��������:2019-10-29
	 * @param newApprovedate0 nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate0 (nc.vo.pub.lang.UFDateTime newApprovedate0 ) {
	 	this.approvedate0 = newApprovedate0;
	} 	 
	
	/**
	 * ���� vnote0��Getter����.����������ע0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVnote0 () {
		return vnote0;
	}   
	/**
	 * ����vnote0��Setter����.����������ע0
	 * ��������:2019-10-29
	 * @param newVnote0 java.lang.String
	 */
	public void setVnote0 (java.lang.String newVnote0 ) {
	 	this.vnote0 = newVnote0;
	} 	 
	
	/**
	 * ���� customers0��Getter����.���������ͻ�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getCustomers0 () {
		return customers0;
	}   
	/**
	 * ����customers0��Setter����.���������ͻ�0
	 * ��������:2019-10-29
	 * @param newCustomers0 java.lang.String
	 */
	public void setCustomers0 (java.lang.String newCustomers0 ) {
	 	this.customers0 = newCustomers0;
	} 	 
	
	/**
	 * ���� srcbilltype0��Getter����.����������Դ��������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbilltype0 () {
		return srcbilltype0;
	}   
	/**
	 * ����srcbilltype0��Setter����.����������Դ��������0
	 * ��������:2019-10-29
	 * @param newSrcbilltype0 java.lang.String
	 */
	public void setSrcbilltype0 (java.lang.String newSrcbilltype0 ) {
	 	this.srcbilltype0 = newSrcbilltype0;
	} 	 
	
	/**
	 * ���� srcbillid0��Getter����.����������Դ����id0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbillid0 () {
		return srcbillid0;
	}   
	/**
	 * ����srcbillid0��Setter����.����������Դ����id0
	 * ��������:2019-10-29
	 * @param newSrcbillid0 java.lang.String
	 */
	public void setSrcbillid0 (java.lang.String newSrcbillid0 ) {
	 	this.srcbillid0 = newSrcbillid0;
	} 	 
	
	/**
	 * ���� srccode0��Getter����.����������Դ���ݺ�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrccode0 () {
		return srccode0;
	}   
	/**
	 * ����srccode0��Setter����.����������Դ���ݺ�0
	 * ��������:2019-10-29
	 * @param newSrccode0 java.lang.String
	 */
	public void setSrccode0 (java.lang.String newSrccode0 ) {
	 	this.srccode0 = newSrccode0;
	} 	 
	
	/**
	 * ���� srcid0��Getter����.����������Դ��������id0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcid0 () {
		return srcid0;
	}   
	/**
	 * ����srcid0��Setter����.����������Դ��������id0
	 * ��������:2019-10-29
	 * @param newSrcid0 java.lang.String
	 */
	public void setSrcid0 (java.lang.String newSrcid0 ) {
	 	this.srcid0 = newSrcid0;
	} 	 
	
	/**
	 * ���� srcbid0��Getter����.����������Դ�����ӱ�id0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbid0 () {
		return srcbid0;
	}   
	/**
	 * ����srcbid0��Setter����.����������Դ�����ӱ�id0
	 * ��������:2019-10-29
	 * @param newSrcbid0 java.lang.String
	 */
	public void setSrcbid0 (java.lang.String newSrcbid0 ) {
	 	this.srcbid0 = newSrcbid0;
	} 	 
	
	/**
	 * ���� srcrowno0��Getter����.����������Դ�����к�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrcrowno0 () {
		return srcrowno0;
	}   
	/**
	 * ����srcrowno0��Setter����.����������Դ�����к�0
	 * ��������:2019-10-29
	 * @param newSrcrowno0 java.lang.String
	 */
	public void setSrcrowno0 (java.lang.String newSrcrowno0 ) {
	 	this.srcrowno0 = newSrcrowno0;
	} 	 
	
	/**
	 * ���� srctrantype0��Getter����.����������Դ��������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSrctrantype0 () {
		return srctrantype0;
	}   
	/**
	 * ����srctrantype0��Setter����.����������Դ��������0
	 * ��������:2019-10-29
	 * @param newSrctrantype0 java.lang.String
	 */
	public void setSrctrantype0 (java.lang.String newSrctrantype0 ) {
	 	this.srctrantype0 = newSrctrantype0;
	} 	 
	
	/**
	 * ���� vfirsttype0��Getter����.��������Դͷ��������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirsttype0 () {
		return vfirsttype0;
	}   
	/**
	 * ����vfirsttype0��Setter����.��������Դͷ��������0
	 * ��������:2019-10-29
	 * @param newVfirsttype0 java.lang.String
	 */
	public void setVfirsttype0 (java.lang.String newVfirsttype0 ) {
	 	this.vfirsttype0 = newVfirsttype0;
	} 	 
	
	/**
	 * ���� vfirstcode0��Getter����.��������Դͷ���ݺ�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstcode0 () {
		return vfirstcode0;
	}   
	/**
	 * ����vfirstcode0��Setter����.��������Դͷ���ݺ�0
	 * ��������:2019-10-29
	 * @param newVfirstcode0 java.lang.String
	 */
	public void setVfirstcode0 (java.lang.String newVfirstcode0 ) {
	 	this.vfirstcode0 = newVfirstcode0;
	} 	 
	
	/**
	 * ���� vfirstid0��Getter����.��������Դͷ��������id0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstid0 () {
		return vfirstid0;
	}   
	/**
	 * ����vfirstid0��Setter����.��������Դͷ��������id0
	 * ��������:2019-10-29
	 * @param newVfirstid0 java.lang.String
	 */
	public void setVfirstid0 (java.lang.String newVfirstid0 ) {
	 	this.vfirstid0 = newVfirstid0;
	} 	 
	
	/**
	 * ���� vfirstbid0��Getter����.��������Դͷ�����ӱ�id0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstbid0 () {
		return vfirstbid0;
	}   
	/**
	 * ����vfirstbid0��Setter����.��������Դͷ�����ӱ�id0
	 * ��������:2019-10-29
	 * @param newVfirstbid0 java.lang.String
	 */
	public void setVfirstbid0 (java.lang.String newVfirstbid0 ) {
	 	this.vfirstbid0 = newVfirstbid0;
	} 	 
	
	/**
	 * ���� vfirstrowno0��Getter����.��������Դͷ�����к�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirstrowno0 () {
		return vfirstrowno0;
	}   
	/**
	 * ����vfirstrowno0��Setter����.��������Դͷ�����к�0
	 * ��������:2019-10-29
	 * @param newVfirstrowno0 java.lang.String
	 */
	public void setVfirstrowno0 (java.lang.String newVfirstrowno0 ) {
	 	this.vfirstrowno0 = newVfirstrowno0;
	} 	 
	
	/**
	 * ���� vfirsttrantype0��Getter����.��������Դͷ��������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getVfirsttrantype0 () {
		return vfirsttrantype0;
	}   
	/**
	 * ����vfirsttrantype0��Setter����.��������Դͷ��������0
	 * ��������:2019-10-29
	 * @param newVfirsttrantype0 java.lang.String
	 */
	public void setVfirsttrantype0 (java.lang.String newVfirsttrantype0 ) {
	 	this.vfirsttrantype0 = newVfirsttrantype0;
	} 	 
	
	/**
	 * ���� def0��Getter����.���������Զ�����0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef0 () {
		return def0;
	}   
	/**
	 * ����def0��Setter����.���������Զ�����0
	 * ��������:2019-10-29
	 * @param newDef0 java.lang.String
	 */
	public void setDef0 (java.lang.String newDef0 ) {
	 	this.def0 = newDef0;
	} 	 
	
	/**
	 * ���� def1��Getter����.���������Զ�����1
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * ����def1��Setter����.���������Զ�����1
	 * ��������:2019-10-29
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * ���� def2��Getter����.���������Զ�����2
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * ����def2��Setter����.���������Զ�����2
	 * ��������:2019-10-29
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * ���� def3��Getter����.���������Զ�����3
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * ����def3��Setter����.���������Զ�����3
	 * ��������:2019-10-29
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * ���� def4��Getter����.���������Զ�����4
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * ����def4��Setter����.���������Զ�����4
	 * ��������:2019-10-29
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * ���� def5��Getter����.���������Զ�����5
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * ����def5��Setter����.���������Զ�����5
	 * ��������:2019-10-29
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * ���� def6��Getter����.���������Զ�����6
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * ����def6��Setter����.���������Զ�����6
	 * ��������:2019-10-29
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * ���� def7��Getter����.���������Զ�����7
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * ����def7��Setter����.���������Զ�����7
	 * ��������:2019-10-29
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * ���� def8��Getter����.���������Զ�����8
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * ����def8��Setter����.���������Զ�����8
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
	 * ���� def21��Getter����.���������Զ�����21
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef21 () {
		return def21;
	}   
	/**
	 * ����def21��Setter����.���������Զ�����21
	 * ��������:2019-10-29
	 * @param newDef21 java.lang.String
	 */
	public void setDef21 (java.lang.String newDef21 ) {
	 	this.def21 = newDef21;
	} 	 
	
	/**
	 * ���� def22��Getter����.���������Զ�����22
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef22 () {
		return def22;
	}   
	/**
	 * ����def22��Setter����.���������Զ�����22
	 * ��������:2019-10-29
	 * @param newDef22 java.lang.String
	 */
	public void setDef22 (java.lang.String newDef22 ) {
	 	this.def22 = newDef22;
	} 	 
	
	/**
	 * ���� def23��Getter����.���������Զ�����23
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef23 () {
		return def23;
	}   
	/**
	 * ����def23��Setter����.���������Զ�����23
	 * ��������:2019-10-29
	 * @param newDef23 java.lang.String
	 */
	public void setDef23 (java.lang.String newDef23 ) {
	 	this.def23 = newDef23;
	} 	 
	
	/**
	 * ���� def24��Getter����.���������Զ�����24
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef24 () {
		return def24;
	}   
	/**
	 * ����def24��Setter����.���������Զ�����24
	 * ��������:2019-10-29
	 * @param newDef24 java.lang.String
	 */
	public void setDef24 (java.lang.String newDef24 ) {
	 	this.def24 = newDef24;
	} 	 
	
	/**
	 * ���� def25��Getter����.���������Զ�����25
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef25 () {
		return def25;
	}   
	/**
	 * ����def25��Setter����.���������Զ�����25
	 * ��������:2019-10-29
	 * @param newDef25 java.lang.String
	 */
	public void setDef25 (java.lang.String newDef25 ) {
	 	this.def25 = newDef25;
	} 	 
	
	/**
	 * ���� def26��Getter����.���������Զ�����26
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef26 () {
		return def26;
	}   
	/**
	 * ����def26��Setter����.���������Զ�����26
	 * ��������:2019-10-29
	 * @param newDef26 java.lang.String
	 */
	public void setDef26 (java.lang.String newDef26 ) {
	 	this.def26 = newDef26;
	} 	 
	
	/**
	 * ���� def27��Getter����.���������Զ�����27
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef27 () {
		return def27;
	}   
	/**
	 * ����def27��Setter����.���������Զ�����27
	 * ��������:2019-10-29
	 * @param newDef27 java.lang.String
	 */
	public void setDef27 (java.lang.String newDef27 ) {
	 	this.def27 = newDef27;
	} 	 
	
	/**
	 * ���� def28��Getter����.���������Զ�����28
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef28 () {
		return def28;
	}   
	/**
	 * ����def28��Setter����.���������Զ�����28
	 * ��������:2019-10-29
	 * @param newDef28 java.lang.String
	 */
	public void setDef28 (java.lang.String newDef28 ) {
	 	this.def28 = newDef28;
	} 	 
	
	/**
	 * ���� def29��Getter����.���������Զ�����29
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef29 () {
		return def29;
	}   
	/**
	 * ����def29��Setter����.���������Զ�����29
	 * ��������:2019-10-29
	 * @param newDef29 java.lang.String
	 */
	public void setDef29 (java.lang.String newDef29 ) {
	 	this.def29 = newDef29;
	} 	 
	
	/**
	 * ���� def30��Getter����.���������Զ�����30
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDef30 () {
		return def30;
	}   
	/**
	 * ����def30��Setter����.���������Զ�����30
	 * ��������:2019-10-29
	 * @param newDef30 java.lang.String
	 */
	public void setDef30 (java.lang.String newDef30 ) {
	 	this.def30 = newDef30;
	} 	 
	
	/**
	 * ���� dept0��Getter����.�����������ݱ����˲���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDept0 () {
		return dept0;
	}   
	/**
	 * ����dept0��Setter����.�����������ݱ����˲���0
	 * ��������:2019-10-29
	 * @param newDept0 java.lang.String
	 */
	public void setDept0 (java.lang.String newDept0 ) {
	 	this.dept0 = newDept0;
	} 	 
	
	/**
	 * ���� receptdate0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getReceptdate0 () {
		return receptdate0;
	}   
	/**
	 * ����receptdate0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newReceptdate0 nc.vo.pub.lang.UFDateTime
	 */
	public void setReceptdate0 (nc.vo.pub.lang.UFDateTime newReceptdate0 ) {
	 	this.receptdate0 = newReceptdate0;
	} 	 
	
	/**
	 * ���� projectno0��Getter����.�����������ű���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProjectno0 () {
		return projectno0;
	}   
	/**
	 * ����projectno0��Setter����.�����������ű���0
	 * ��������:2019-10-29
	 * @param newProjectno0 java.lang.String
	 */
	public void setProjectno0 (java.lang.String newProjectno0 ) {
	 	this.projectno0 = newProjectno0;
	} 	 
	
	/**
	 * ���� projectname0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProjectname0 () {
		return projectname0;
	}   
	/**
	 * ����projectname0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newProjectname0 java.lang.String
	 */
	public void setProjectname0 (java.lang.String newProjectname0 ) {
	 	this.projectname0 = newProjectname0;
	} 	 
	
	/**
	 * ���� productname0��Getter����.����������Ʒ����0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProductname0 () {
		return productname0;
	}   
	/**
	 * ����productname0��Setter����.����������Ʒ����0
	 * ��������:2019-10-29
	 * @param newProductname0 java.lang.String
	 */
	public void setProductname0 (java.lang.String newProductname0 ) {
	 	this.productname0 = newProductname0;
	} 	 
	
	/**
	 * ���� shipowner0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipowner0 () {
		return shipowner0;
	}   
	/**
	 * ����shipowner0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newShipowner0 java.lang.String
	 */
	public void setShipowner0 (java.lang.String newShipowner0 ) {
	 	this.shipowner0 = newShipowner0;
	} 	 
	
	/**
	 * ���� shipno0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipno0 () {
		return shipno0;
	}   
	/**
	 * ����shipno0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newShipno0 java.lang.String
	 */
	public void setShipno0 (java.lang.String newShipno0 ) {
	 	this.shipno0 = newShipno0;
	} 	 
	
	/**
	 * ���� shipname0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipname0 () {
		return shipname0;
	}   
	/**
	 * ����shipname0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newShipname0 java.lang.String
	 */
	public void setShipname0 (java.lang.String newShipname0 ) {
	 	this.shipname0 = newShipname0;
	} 	 
	
	/**
	 * ���� shiptype0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShiptype0 () {
		return shiptype0;
	}   
	/**
	 * ����shiptype0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newShiptype0 java.lang.String
	 */
	public void setShiptype0 (java.lang.String newShiptype0 ) {
	 	this.shiptype0 = newShiptype0;
	} 	 
	
	/**
	 * ���� servicelocation0��Getter����.������������ص�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getServicelocation0 () {
		return servicelocation0;
	}   
	/**
	 * ����servicelocation0��Setter����.������������ص�0
	 * ��������:2019-10-29
	 * @param newServicelocation0 java.lang.String
	 */
	public void setServicelocation0 (java.lang.String newServicelocation0 ) {
	 	this.servicelocation0 = newServicelocation0;
	} 	 
	
	/**
	 * ���� grindersdept0��Getter����.�����������Ʋ���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getGrindersdept0 () {
		return grindersdept0;
	}   
	/**
	 * ����grindersdept0��Setter����.�����������Ʋ���0
	 * ��������:2019-10-29
	 * @param newGrindersdept0 java.lang.String
	 */
	public void setGrindersdept0 (java.lang.String newGrindersdept0 ) {
	 	this.grindersdept0 = newGrindersdept0;
	} 	 
	
	/**
	 * ���� deliverytime0��Getter����.����������Ʒ��������ʱ��0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getDeliverytime0 () {
		return deliverytime0;
	}   
	/**
	 * ����deliverytime0��Setter����.����������Ʒ��������ʱ��0
	 * ��������:2019-10-29
	 * @param newDeliverytime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setDeliverytime0 (nc.vo.pub.lang.UFDateTime newDeliverytime0 ) {
	 	this.deliverytime0 = newDeliverytime0;
	} 	 
	
	/**
	 * ���� arrivaltime0��Getter����.��������Ҫ��ִ�ʱ��0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getArrivaltime0 () {
		return arrivaltime0;
	}   
	/**
	 * ����arrivaltime0��Setter����.��������Ҫ��ִ�ʱ��0
	 * ��������:2019-10-29
	 * @param newArrivaltime0 nc.vo.pub.lang.UFDateTime
	 */
	public void setArrivaltime0 (nc.vo.pub.lang.UFDateTime newArrivaltime0 ) {
	 	this.arrivaltime0 = newArrivaltime0;
	} 	 
	
	/**
	 * ���� serviceperiod0��Getter����.���������ⶨ��������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getServiceperiod0 () {
		return serviceperiod0;
	}   
	/**
	 * ����serviceperiod0��Setter����.���������ⶨ��������0
	 * ��������:2019-10-29
	 * @param newServiceperiod0 java.lang.String
	 */
	public void setServiceperiod0 (java.lang.String newServiceperiod0 ) {
	 	this.serviceperiod0 = newServiceperiod0;
	} 	 
	
	/**
	 * ���� aftersalescust0��Getter����.���������ۺ����ͻ�0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getAftersalescust0 () {
		return aftersalescust0;
	}   
	/**
	 * ����aftersalescust0��Setter����.���������ۺ����ͻ�0
	 * ��������:2019-10-29
	 * @param newAftersalescust0 java.lang.String
	 */
	public void setAftersalescust0 (java.lang.String newAftersalescust0 ) {
	 	this.aftersalescust0 = newAftersalescust0;
	} 	 
	
	/**
	 * ���� linkman0��Getter����.����������ϵ��0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getLinkman0 () {
		return linkman0;
	}   
	/**
	 * ����linkman0��Setter����.����������ϵ��0
	 * ��������:2019-10-29
	 * @param newLinkman0 java.lang.String
	 */
	public void setLinkman0 (java.lang.String newLinkman0 ) {
	 	this.linkman0 = newLinkman0;
	} 	 
	
	/**
	 * ���� phonenumber0��Getter����.����������ϵ�绰0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getPhonenumber0 () {
		return phonenumber0;
	}   
	/**
	 * ����phonenumber0��Setter����.����������ϵ�绰0
	 * ��������:2019-10-29
	 * @param newPhonenumber0 java.lang.String
	 */
	public void setPhonenumber0 (java.lang.String newPhonenumber0 ) {
	 	this.phonenumber0 = newPhonenumber0;
	} 	 
	
	/**
	 * ���� guaranteeperiod0��Getter����.���������ʱ���0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getGuaranteeperiod0 () {
		return guaranteeperiod0;
	}   
	/**
	 * ����guaranteeperiod0��Setter����.���������ʱ���0
	 * ��������:2019-10-29
	 * @param newGuaranteeperiod0 java.lang.String
	 */
	public void setGuaranteeperiod0 (java.lang.String newGuaranteeperiod0 ) {
	 	this.guaranteeperiod0 = newGuaranteeperiod0;
	} 	 
	
	/**
	 * ���� servicetype0��Getter����.����������������0
	 *  ��������:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getServicetype0 () {
		return servicetype0;
	}   
	/**
	 * ����servicetype0��Setter����.����������������0
	 * ��������:2019-10-29
	 * @param newServicetype0 java.lang.Integer
	 */
	public void setServicetype0 (java.lang.Integer newServicetype0 ) {
	 	this.servicetype0 = newServicetype0;
	} 	 
	
	/**
	 * ���� businesstype0��Getter����.������������ҵ������0
	 *  ��������:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getBusinesstype0 () {
		return businesstype0;
	}   
	/**
	 * ����businesstype0��Setter����.������������ҵ������0
	 * ��������:2019-10-29
	 * @param newBusinesstype0 java.lang.Integer
	 */
	public void setBusinesstype0 (java.lang.Integer newBusinesstype0 ) {
	 	this.businesstype0 = newBusinesstype0;
	} 	 
	
	/**
	 * ���� productstatus0��Getter����.����������Ʒ״̬0
	 *  ��������:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getProductstatus0 () {
		return productstatus0;
	}   
	/**
	 * ����productstatus0��Setter����.����������Ʒ״̬0
	 * ��������:2019-10-29
	 * @param newProductstatus0 java.lang.Integer
	 */
	public void setProductstatus0 (java.lang.Integer newProductstatus0 ) {
	 	this.productstatus0 = newProductstatus0;
	} 	 
	
	/**
	 * ���� processingmode0��Getter����.����������������ʽ0
	 *  ��������:2019-10-29
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getProcessingmode0 () {
		return processingmode0;
	}   
	/**
	 * ����processingmode0��Setter����.����������������ʽ0
	 * ��������:2019-10-29
	 * @param newProcessingmode0 java.lang.Integer
	 */
	public void setProcessingmode0 (java.lang.Integer newProcessingmode0 ) {
	 	this.processingmode0 = newProcessingmode0;
	} 	 
	
	/**
	 * ���� isgeneratedown0��Getter����.���������Ƿ��������ε���0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIsgeneratedown0 () {
		return isgeneratedown0;
	}   
	/**
	 * ����isgeneratedown0��Setter����.���������Ƿ��������ε���0
	 * ��������:2019-10-29
	 * @param newIsgeneratedown0 nc.vo.pub.lang.UFBoolean
	 */
	public void setIsgeneratedown0 (nc.vo.pub.lang.UFBoolean newIsgeneratedown0 ) {
	 	this.isgeneratedown0 = newIsgeneratedown0;
	} 	 
	
	/**
	 * ���� shipyard0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipyard0 () {
		return shipyard0;
	}   
	/**
	 * ����shipyard0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newShipyard0 java.lang.String
	 */
	public void setShipyard0 (java.lang.String newShipyard0 ) {
	 	this.shipyard0 = newShipyard0;
	} 	 
	
	/**
	 * ���� sendtheunit0��Getter����.�����������͵�λ0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getSendtheunit0 () {
		return sendtheunit0;
	}   
	/**
	 * ����sendtheunit0��Setter����.�����������͵�λ0
	 * ��������:2019-10-29
	 * @param newSendtheunit0 java.lang.String
	 */
	public void setSendtheunit0 (java.lang.String newSendtheunit0 ) {
	 	this.sendtheunit0 = newSendtheunit0;
	} 	 
	
	/**
	 * ���� productattribute0��Getter����.����������Ʒ����0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProductattribute0 () {
		return productattribute0;
	}   
	/**
	 * ����productattribute0��Setter����.����������Ʒ����0
	 * ��������:2019-10-29
	 * @param newProductattribute0 java.lang.String
	 */
	public void setProductattribute0 (java.lang.String newProductattribute0 ) {
	 	this.productattribute0 = newProductattribute0;
	} 	 
	
	/**
	 * ���� shipclub0��Getter����.��������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getShipclub0 () {
		return shipclub0;
	}   
	/**
	 * ����shipclub0��Setter����.��������������0
	 * ��������:2019-10-29
	 * @param newShipclub0 java.lang.String
	 */
	public void setShipclub0 (java.lang.String newShipclub0 ) {
	 	this.shipclub0 = newShipclub0;
	} 	 
	
	/**
	 * ���� area0��Getter����.������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getArea0 () {
		return area0;
	}   
	/**
	 * ����area0��Setter����.������������0
	 * ��������:2019-10-29
	 * @param newArea0 java.lang.String
	 */
	public void setArea0 (java.lang.String newArea0 ) {
	 	this.area0 = newArea0;
	} 	 
	
	/**
	 * ���� dutypersion0��Getter����.��������������0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDutypersion0 () {
		return dutypersion0;
	}   
	/**
	 * ����dutypersion0��Setter����.��������������0
	 * ��������:2019-10-29
	 * @param newDutypersion0 java.lang.String
	 */
	public void setDutypersion0 (java.lang.String newDutypersion0 ) {
	 	this.dutypersion0 = newDutypersion0;
	} 	 
	
	/**
	 * ���� deliverydate0��Getter����.������������ʱ��0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDeliverydate0 () {
		return deliverydate0;
	}   
	/**
	 * ����deliverydate0��Setter����.������������ʱ��0
	 * ��������:2019-10-29
	 * @param newDeliverydate0 nc.vo.pub.lang.UFDate
	 */
	public void setDeliverydate0 (nc.vo.pub.lang.UFDate newDeliverydate0 ) {
	 	this.deliverydate0 = newDeliverydate0;
	} 	 
	
	/**
	 * ���� deliproduct0��Getter����.��������������Ʒ0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getDeliproduct0 () {
		return deliproduct0;
	}   
	/**
	 * ����deliproduct0��Setter����.��������������Ʒ0
	 * ��������:2019-10-29
	 * @param newDeliproduct0 java.lang.String
	 */
	public void setDeliproduct0 (java.lang.String newDeliproduct0 ) {
	 	this.deliproduct0 = newDeliproduct0;
	} 	 
	
	/**
	 * ���� projectdeco0��Getter����.����������Ŀ����0
	 *  ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getProjectdeco0 () {
		return projectdeco0;
	}   
	/**
	 * ����projectdeco0��Setter����.����������Ŀ����0
	 * ��������:2019-10-29
	 * @param newProjectdeco0 java.lang.String
	 */
	public void setProjectdeco0 (java.lang.String newProjectdeco0 ) {
	 	this.projectdeco0 = newProjectdeco0;
	} 	 
	
	/**
	 * ���� istechproposal0��Getter����.���������Ƿ����ɼ�������0
	 *  ��������:2019-10-29
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIstechproposal0 () {
		return istechproposal0;
	}   
	/**
	 * ����istechproposal0��Setter����.���������Ƿ����ɼ�������0
	 * ��������:2019-10-29
	 * @param newIstechproposal0 nc.vo.pub.lang.UFBoolean
	 */
	public void setIstechproposal0 (nc.vo.pub.lang.UFBoolean newIstechproposal0 ) {
	 	this.istechproposal0 = newIstechproposal0;
	} 	 
	
	/**
	 * ���� id_entity1��Getter����.��������id_Entity1
	 *  ��������:2019-10-29
	 * @return nc.vo.test.test01.Entity1[]
	 */
	public nc.vo.test.test01.Entity1[] getId_entity1 () {
		return id_entity1;
	}   
	/**
	 * ����id_entity1��Setter����.��������id_Entity1
	 * ��������:2019-10-29
	 * @param newId_entity1 nc.vo.test.test01.Entity1[]
	 */
	public void setId_entity1 (nc.vo.test.test01.Entity1[] newId_entity1 ) {
	 	this.id_entity1 = newId_entity1;
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
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2019-10-29
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_servicequest0";
	}
    
	/**
	 * <p>���ر�����
	 * <p>
	 * ��������:2019-10-29
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "test_service";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2019-10-29
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "test_service";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2019-10-29
	  */
     public Service() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.test.test01.Service" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("test.service");
		
   	}
     
}